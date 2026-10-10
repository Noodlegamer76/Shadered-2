
package com.noodlegamer76.shadered.client.renderer.assimp;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MinecraftLightUvData {
    private static final int SUBCHUNK_BYTES = 16 * 16 * 16;
    private static final int PAGE_INDEX_BYTES = Integer.BYTES;
    private final Set<Integer> freeAddresses = new HashSet<>();
    private final ConcurrentHashMap<SectionPos, SubchunkUvs> dirtySubchunks = new ConcurrentHashMap<>();
    private int previousRenderDistance = -1;
    private SectionPos previousCameraPos;
    private int totalSubchunks;
    private int headerBytes;
    private int payloadOffset;

    public final int ssbo;
    private ByteBuffer buffer;

    public MinecraftLightUvData() {
        ssbo = GL15.glGenBuffers();

        previousCameraPos = SectionPos.of(
                Minecraft.getInstance().gameRenderer.getMainCamera().getBlockPosition()
        );

        resize();
    }

    public void resize() {
        int renderDistance = (int) (Minecraft.getInstance().gameRenderer.getRenderDistance() / 16);

        if (renderDistance == previousRenderDistance) {
            return;
        }

        int length = renderDistance * 2 + 1;

        totalSubchunks = length * length * length;
        headerBytes = totalSubchunks * PAGE_INDEX_BYTES;
        payloadOffset = headerBytes;

        int size = headerBytes + (totalSubchunks * SUBCHUNK_BYTES);

        buffer = BufferUtils.createByteBuffer(size);

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, ssbo);
        GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, size, GL15.GL_DYNAMIC_DRAW);

        freeAddresses.clear();

        for (int i = 0; i < totalSubchunks; i++) {
            buffer.putInt(i * PAGE_INDEX_BYTES, -1);

            freeAddresses.add(payloadOffset + (i * SUBCHUNK_BYTES));
        }

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        previousCameraPos = SectionPos.of(camera.getBlockPosition());

        buffer.position(0);
        buffer.limit(headerBytes);

        GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, 0, buffer);

        buffer.clear();

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);

        previousRenderDistance = renderDistance;
    }

    public void bind(int binding) {
        GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, binding, ssbo);
    }

    public int getSubchunkIndex(SectionPos pos) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        SectionPos cameraSection = SectionPos.of(camera.getBlockPosition());

        int length = previousRenderDistance * 2 + 1;

        Vec3i offset = pos.subtract(cameraSection);

        int x = offset.getX() + previousRenderDistance;
        int y = offset.getY() + previousRenderDistance;
        int z = offset.getZ() + previousRenderDistance;

        if (x < 0 || x >= length
                || y < 0 || y >= length
                || z < 0 || z >= length) {
            return -1;
        }

        return x + (y * length) + (z * length * length);
    }

    public SectionPos getIndexSubchunk(int index, boolean previousCamera) {
        if (index < 0 || index >= totalSubchunks) {
            throw new IndexOutOfBoundsException("Invalid subchunk index: " + index);
        }

        SectionPos cameraPos;

        if (previousCamera) {
            cameraPos = previousCameraPos;
        } else {
            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

            cameraPos = SectionPos.of(camera.getBlockPosition());
        }

        int length = previousRenderDistance * 2 + 1;
        int area = length * length;

        int z = index / area;
        int rem = index % area;
        int y = rem / length;
        int x = rem % length;

        int relX = x - previousRenderDistance;
        int relY = y - previousRenderDistance;
        int relZ = z - previousRenderDistance;

        return SectionPos.of(
                cameraPos.getX() + relX,
                cameraPos.getY() + relY,
                cameraPos.getZ() + relZ
        );
    }

    public void addDirtySubchunk(SectionPos pos, SubchunkUvs uvs) {
        dirtySubchunks.put(pos, uvs);
    }

    public void uploadForFrame() {
        remapPages();

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, ssbo);

        Map<SectionPos, SubchunkUvs> pending = new HashMap<>(dirtySubchunks);

        for (Map.Entry<SectionPos, SubchunkUvs> entry: pending.entrySet()) {
            SectionPos pos = entry.getKey();
            SubchunkUvs uvs = entry.getValue();

            int pageIndex = getSubchunkIndex(pos);

            if (pageIndex < 0 || pageIndex >= totalSubchunks) {
                dirtySubchunks.remove(pos, uvs);
                continue;
            }

            int headerOffset = pageIndex * PAGE_INDEX_BYTES;
            int pageAddress = buffer.getInt(headerOffset);

            if (pageAddress < 0) {
                if (freeAddresses.isEmpty()) {
                    continue;
                }

                Iterator<Integer> freeAddressIterator = freeAddresses.iterator();

                pageAddress = freeAddressIterator.next();
                freeAddressIterator.remove();

                buffer.putInt(headerOffset, pageAddress);
            }

            if (!isValidPageAddress(pageAddress)) {
                buffer.putInt(headerOffset, -1);
                continue;
            }

            buffer.position(pageAddress);
            buffer.put(uvs.uvs);

            buffer.position(pageAddress);
            buffer.limit(pageAddress + SUBCHUNK_BYTES);

            GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, pageAddress, buffer);

            buffer.clear();

            dirtySubchunks.remove(pos, uvs);
        }

        buffer.position(0);
        buffer.limit(headerBytes);

        GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, 0, buffer);

        buffer.clear();

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
    }

    private boolean isValidPageAddress(int address) {
        return address >= payloadOffset
                && address < buffer.capacity()
                && (address - payloadOffset) % SUBCHUNK_BYTES == 0;
    }

    public void remapPages() {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        SectionPos cameraPos = SectionPos.of(camera.getBlockPosition());

        if (cameraPos.equals(previousCameraPos)) {
            return;
        }

        Map<SectionPos, Integer> oldPageAddresses = new HashMap<>();

        for (int i = 0; i < totalSubchunks; i++) {
            SectionPos pos = getIndexSubchunk(i, true);
            int address = buffer.getInt(i * PAGE_INDEX_BYTES);

            if (isValidPageAddress(address)) {
                oldPageAddresses.put(pos, address);
            }
        }

        previousCameraPos = cameraPos;

        Set<Integer> retainedAddresses = new HashSet<>();

        for (int i = 0; i < totalSubchunks; i++) {
            int headerOffset = i * PAGE_INDEX_BYTES;
            SectionPos indexPos = getIndexSubchunk(i, false);

            Integer oldAddress = oldPageAddresses.get(indexPos);

            if (oldAddress != null
                    && isValidPageAddress(oldAddress)
                    && retainedAddresses.add(oldAddress)) {
                buffer.putInt(headerOffset, oldAddress);
            } else {
                buffer.putInt(headerOffset, -1);
            }
        }

        freeAddresses.clear();

        for (int i = 0; i < totalSubchunks; i++) {
            int address = payloadOffset + i * SUBCHUNK_BYTES;

            if (!retainedAddresses.contains(address)) {
                freeAddresses.add(address);
            }
        }
    }

    public int getPreviousRenderDistance() {
        return previousRenderDistance;
    }

    public record SubchunkUvs(byte[] uvs) {
        public SubchunkUvs() {
            this(new byte[16 * 16 * 16]);
        }

        public SubchunkUvs {
            if (uvs.length != 16 * 16 * 16) {
                throw new IllegalArgumentException("SubchunkUvs must not be any size other than 4096");
            }
        }

        public int getIndex(int x, int y, int z) {
            x &= 15;
            y &= 15;
            z &= 15;

            return x + (y * 16) + (z * 16 * 16);
        }

        public byte getValue(int x, int y, int z) {
            int index = getIndex(x, y, z);
            return uvs[index];
        }

        public void setValue(int x, int y, int z, byte value) {
            int index = getIndex(x, y, z);
            uvs[index] = value;
        }

        public void setValue(BlockPos pos, byte value) {
            setValue(pos.getX(), pos.getY(), pos.getZ(), value);
        }
    }
}
