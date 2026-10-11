package com.noodlegamer76.shadered.client.renderer.assimp;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.math.Axis;
import com.noodlegamer76.shadered.client.assimp.AssimpMaterial;
import com.noodlegamer76.shadered.client.assimp.AssimpModel;
import com.noodlegamer76.shadered.client.util.TextureUtils;
import com.noodlegamer76.shadered.event.RegisterShaders;
import com.noodlegamer76.shadered.mixin.accessor.LightTextureAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL43;

import java.util.*;

public class AssimpRenderer {
    private static final AssimpRenderer INSTANCE = new AssimpRenderer();

    public static AssimpRenderer getInstance() {
        return INSTANCE;
    }

    private static final Matrix4f IDENTITY = new Matrix4f();
    private final Map<AssimpMaterial, Map<ViewMatrix, Map<VertexBuffer, List<RenderEntry>>>> opaqueEntries = new HashMap<>();
    private final Map<RenderEntry, ViewMatrix> transparentEntriesData = new HashMap<>();
    private final List<RenderEntry> transparentEntries = new ArrayList<>();
    private final BoneMatrixSsbo boneMatrixSsbo = new BoneMatrixSsbo();
    private final MinecraftLightUvData minecraftLightUvData = new MinecraftLightUvData();
    private final Set<RenderableModel> models = new HashSet<>();
    private final Map<RenderableModel, Integer> modelOffsets = new LinkedHashMap<>();

    private AssimpRenderer() {}

    public void addModel(RenderableModel model) {
        models.add(model);

        Vector3f cameraPos = Minecraft.getInstance()
                .gameRenderer
                .getMainCamera()
                .getPosition()
                .toVector3f();

        for (AssimpModel part : model.getModel()) {
            for (AssimpModel.MeshInstanceTransform inst : part.instances) {

                Matrix4f worldMatrix = new Matrix4f(model.modelMatrix).mul(inst.global());

                ViewMatrix viewMatrix = ViewMatrix.getViewMatrix(model.shouldUseViewMatrix());
                if (part.getMaterial().useAlpha()) {
                    Vector3f pos = new Vector3f();
                    worldMatrix.getTranslation(pos);
                    float distSq = pos.distanceSquared(cameraPos);

                    RenderEntry entry = new RenderEntry(part, model, distSq);
                    transparentEntriesData.put(entry, viewMatrix);
                    transparentEntries.add(entry);
                } else {
                    opaqueEntries
                            .computeIfAbsent(part.getMaterial(), k -> new HashMap<>())
                            .computeIfAbsent(viewMatrix, k -> new HashMap<>())
                            .computeIfAbsent(part.getVertexBuffer(), k -> new ArrayList<>())
                            .add(new RenderEntry(part, model, 0));
                }
            }
        }
    }

    public void render(float partialTick, int renderTick) {
        PoseStack modelViewMat = new PoseStack();
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();

        modelViewMat.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        modelViewMat.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0F));
        modelViewMat.translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);

        List<Matrix4f> allBones = new ArrayList<>();

        modelOffsets.clear();
        int cursor = 0;

        for (RenderableModel model : models) {
            if (model.animator != null) {
                float renderTime = (mc.level.getGameTime() + partialTick) / 20.0f;
                model.animator.update(renderTime);

                Matrix4f[] bones = model.animator.getFinalMatrices();

                modelOffsets.put(model, cursor);

                for (Matrix4f bone : bones) {
                    allBones.add(new Matrix4f(bone));
                }
                cursor += bones.length;
            } else {
                modelOffsets.put(model, -1);
            }
        }

        boneMatrixSsbo.upload(allBones);
        minecraftLightUvData.uploadForFrame();

        GL43.glMemoryBarrier(GL43.GL_SHADER_STORAGE_BARRIER_BIT);

        RenderSystem.setShader(RegisterShaders::getPbr);

        setUniforms();

        RenderSystem.enableDepthTest();
        Matrix4f viewMat = new Matrix4f(modelViewMat.last().pose());
        renderOpaque(new PoseStack(), modelViewMat, viewMat);
        renderTransparent(new PoseStack(), modelViewMat, viewMat);

        VertexBuffer.unbind();
        opaqueEntries.clear();
        transparentEntries.clear();
        transparentEntriesData.clear();
        models.clear();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private void renderOpaque(PoseStack poseStack, PoseStack modelViewMat, Matrix4f viewMat) {

        boneMatrixSsbo.bind(0);
        minecraftLightUvData.bind(1);

        for (var matEntry : opaqueEntries.entrySet()) {
            matEntry.getKey().bind();
            for (var viewMatEntry: matEntry.getValue().entrySet()) {
                modelViewMat.pushPose();

                ViewMatrix viewMatrix = viewMatEntry.getKey();
                boolean useViewMatrix = ViewMatrix.useViewMatrix(viewMatrix);

                setViewMatrix(modelViewMat, viewMat, useViewMatrix);

                for (var bufferEntry : viewMatEntry.getValue().entrySet()) {
                    VertexBuffer buffer = bufferEntry.getKey();
                    buffer.bind();
                    for (RenderEntry entry : bufferEntry.getValue()) {
                        draw(poseStack, entry, buffer, modelViewMat);
                    }
                }

                modelViewMat.popPose();
            }
        }
    }

    private void renderTransparent(PoseStack poseStack, PoseStack modelViewMat, Matrix4f viewMat) {
        transparentEntries.sort((a, b) -> Float.compare(b.distanceSq(), a.distanceSq()));

        boneMatrixSsbo.bind(0);
        minecraftLightUvData.bind(1);

        for (RenderEntry entry : transparentEntries) {
            entry.model().getMaterial().bind();
            VertexBuffer buffer = entry.model().getVertexBuffer();
            buffer.bind();

            modelViewMat.pushPose();

            ViewMatrix viewMatrix = transparentEntriesData.getOrDefault(entry, ViewMatrix.NORMAL);
            boolean useViewMatrix = ViewMatrix.useViewMatrix(viewMatrix);
            setViewMatrix(modelViewMat, viewMat, useViewMatrix);

            draw(poseStack, entry, buffer, modelViewMat);

            modelViewMat.popPose();
        }
    }

    private void setViewMatrix(PoseStack modelViewMat, Matrix4f viewMat, boolean useViewMatrix) {
        ShaderInstance shader = RegisterShaders.getPbr();

        Matrix4f inverseViewMat = new Matrix4f(viewMat).invert();

        Uniform inverseViewMatUniform = shader.getUniform("InverseViewMat");
        if (inverseViewMatUniform != null) {
            inverseViewMatUniform.set(inverseViewMat);
        }

        Uniform viewMatUniform = shader.getUniform("ViewMat");
        if (viewMatUniform != null) {
            if (useViewMatrix) {
                viewMatUniform.set(viewMat);
            }
            else {
                viewMatUniform.set(IDENTITY);
            }
        }

        Uniform useViewMatrixUniform = shader.getUniform("UseViewMatrix");
        if (useViewMatrixUniform != null) {
            useViewMatrixUniform.set(useViewMatrix ? 1 : 0);
        }
    }

    private void draw(PoseStack poseStack, RenderEntry entry, VertexBuffer buffer, PoseStack modelViewMat) {
        for (AssimpModel.MeshInstanceTransform t : entry.model().instances) {
            poseStack.pushPose();
            modelViewMat.pushPose();

            poseStack.mulPoseMatrix(entry.modelWrapper().modelMatrix);
            poseStack.mulPoseMatrix(t.global());

            modelViewMat.mulPoseMatrix(entry.modelWrapper().modelMatrix);
            modelViewMat.mulPoseMatrix(t.global());

            Uniform modelMat = RegisterShaders.getPbr().getUniform("ModelMat");
            if (modelMat != null) {
                modelMat.set(poseStack.last().pose());
            }

            Integer offset = modelOffsets.get(entry.modelWrapper());
            Uniform off = RegisterShaders.getPbr().getUniform("Offset");
            if (off != null) {
                off.set(offset == null ? -1 : offset);
            }

            buffer.drawWithShader(
                    modelViewMat.last().pose(),
                    RenderSystem.getProjectionMatrix(),
                    RenderSystem.getShader()
            );

            modelViewMat.popPose();
            poseStack.popPose();
        }
    }

    private void setUniforms() {
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();

        ShaderInstance shader = RegisterShaders.getPbr();

        ResourceLocation lightTextureLocation = ((LightTextureAccessor) mc.gameRenderer.lightTexture()).shadered$getLightTextureLocation();
        int lightTextureId = TextureUtils.getTextureId(lightTextureLocation);
        shader.setSampler("MinecraftLightTexture", lightTextureId);

        Uniform cameraPos = shader.getUniform("CameraPos");
        if (cameraPos != null) {
            cameraPos.set((float) camera.getPosition().x, (float) camera.getPosition().y, (float) camera.getPosition().z);
        }

        Vec3 lightDir = new Vec3(0.35, -1.0, 0.25).normalize();
        Uniform lightDirection = shader.getUniform("LightDirection");
        if (lightDirection != null) {
            lightDirection.set((float) lightDir.x, (float) lightDir.y, (float) lightDir.z);
        }

        Uniform lightColor = shader.getUniform("LightColor");
        if (lightColor != null) {
            lightColor.set(2.5f, 2.4f, 2.2f);
        }

        Uniform exposure = shader.getUniform("Exposure");
        if (exposure != null) {
            exposure.set(1.0f);
        }

        Uniform ambientStrength = shader.getUniform("AmbientStrength");
        if (ambientStrength != null) {
            ambientStrength.set(1.0f);
        }

        Uniform renderDistanceUniform = shader.getUniform("RenderDistance");
        if (renderDistanceUniform != null) {
            renderDistanceUniform.set(minecraftLightUvData.getPreviousRenderDistance());
        }
    }

    public MinecraftLightUvData getMinecraftLightUvData() {
        return minecraftLightUvData;
    }

    public BoneMatrixSsbo getBoneMatrixSsbo() {
        return boneMatrixSsbo;
    }

    public record RenderEntry(
            AssimpModel model,
            RenderableModel modelWrapper,
            float distanceSq
    ) {}

    public enum ViewMatrix {
        IDENTITY,
        NORMAL;

        public static ViewMatrix getViewMatrix(boolean useViewMatrix) {
            return useViewMatrix ? NORMAL : IDENTITY;
        }

        public static boolean useViewMatrix(ViewMatrix viewMatrix) {
            return viewMatrix == NORMAL;
        }
    }
}