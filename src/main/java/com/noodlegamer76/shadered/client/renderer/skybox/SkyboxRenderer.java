package com.noodlegamer76.shadered.client.renderer.skybox;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.client.renderer.complexpass.ComplexPassRenderer;
import com.noodlegamer76.shadered.client.renderer.complexpass.RenderStage;
import com.noodlegamer76.shadered.client.renderer.complexpass.passes.SkyboxRenderPass;
import com.noodlegamer76.shadered.event.RegisterShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkyboxRenderer {
    private static final SkyboxRenderer INSTANCE = new SkyboxRenderer();

    public static SkyboxRenderer getInstance() {
        return INSTANCE;
    }

    public static final ResourceLocation NEBULA = ResourceLocation.fromNamespaceAndPath(Shadered.MODID, "textures/environment/nebula");
    public static final ResourceLocation STORMY = ResourceLocation.fromNamespaceAndPath(Shadered.MODID, "textures/environment/stormy");
    public static final ResourceLocation OCEAN = ResourceLocation.fromNamespaceAndPath(Shadered.MODID, "textures/environment/ocean");
    public static final ResourceLocation ECLIPSE = ResourceLocation.fromNamespaceAndPath(Shadered.MODID, "textures/environment/eclipse");
    public static final ResourceLocation LIGHT = ResourceLocation.fromNamespaceAndPath(Shadered.MODID, "textures/environment/light");
    public static final ResourceLocation FOREST = ResourceLocation.fromNamespaceAndPath(Shadered.MODID, "textures/environment/forest");
    public static final ResourceLocation IRIDIA = ResourceLocation.fromNamespaceAndPath(Shadered.MODID, "textures/environment/iridia");
    public static final ResourceLocation PIXEL = ResourceLocation.fromNamespaceAndPath(Shadered.MODID, "textures/misc/pixel.png");

    private final Map<Integer, Integer> skyboxIDToRenderID = new HashMap<>();
    private final List<SkyboxRenderPass> skyboxes = new ArrayList<>();

    private SkyboxRenderer() {

    }

    public int getSkyboxTextureId(int stateId) {
        return skyboxIDToRenderID.getOrDefault(stateId, -1);
    }

    public void registerSkybox(SkyboxRenderPass pass, int id) {
        ComplexPassRenderer renderer = ComplexPassRenderer.getInstance();

        renderer.add(RenderStage.AFTER_SKY, pass);

        if (skyboxIDToRenderID.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate Skybox ID: [" + id + "]");
        }
        if (id < 0 || id > 10) {
            throw new IllegalArgumentException("Invalid ID: [" + id + "], ID must be 0-10");
        }

        skyboxIDToRenderID.put(id, skyboxIDToRenderID.size());
        skyboxes.add(pass);
    }

    public void preRender() {

    }

    public void bindSkyblockTextures() {
        ShaderInstance shader = RegisterShaders.getSkyblock();
        if (shader == null) {
            return;
        }
        for (int i = 0; i < skyboxes.size(); i++) {
            int textureId = skyboxes.get(i).getSkyboxTarget().getColorTextureId();

            String name = "skybox" + i;
            shader.setSampler(name, textureId);
        }

        int pixelTextureId = Minecraft.getInstance()
                .getTextureManager()
                .getTexture(PIXEL)
                .getId();
        shader.setSampler("pixel", pixelTextureId);

        Window window = Minecraft.getInstance().getWindow();
        Uniform screenSize = shader.SCREEN_SIZE;
        if (screenSize != null) {
            screenSize.set((float) window.getWidth(), (float) window.getHeight());
        }
    }

    public void setup() {
        //TODO: Resource Pack Skyblock registry.
        Vector3f skyboxRotationSpeed = new Vector3f(0.007f, 0.01f, 0.004f);
        SkyboxRenderPass spaceRenderPass = new SkyboxRenderPass(
                NEBULA,
                new SkyboxTranslation(), skyboxRotationSpeed, GameRenderer::getPositionTexColorShader
        );

        SkyboxRenderPass stormyRenderPass = new SkyboxRenderPass(
                STORMY,
                new SkyboxTranslation()
        );

        SkyboxRenderPass oceanRenderPass = new SkyboxRenderPass(
                OCEAN,
                new SkyboxTranslation()
                        .setAllFlip(SkyboxTranslation.SkyboxFlip.NONE)
                        .setAllRot(SkyboxTranslation.SkyboxRotation.ROTATE_90_CCW)
        );

        //TODO: custom skybox effects
        SkyboxRenderPass eclipseRenderPass = new SkyboxRenderPass(
                ECLIPSE,
                new SkyboxTranslation(), skyboxRotationSpeed, GameRenderer::getPositionTexShader
        );

        SkyboxRenderPass iridiaRenderPass = new SkyboxRenderPass(
                IRIDIA,
                new SkyboxTranslation()
                        .setTopBottomRot(SkyboxTranslation.SkyboxRotation.ROTATE_90_CW)
        );

        SkyboxRenderPass forestRenderPass = new SkyboxRenderPass(
                FOREST,
                new SkyboxTranslation()
                        .setAllFlip(SkyboxTranslation.SkyboxFlip.NONE)
                        .setAllRot(SkyboxTranslation.SkyboxRotation.ROTATE_90_CCW)
        );

        SkyboxRenderPass lightRenderPass = new SkyboxRenderPass(
                LIGHT,
                new SkyboxTranslation()
        );

        registerSkybox(spaceRenderPass, 0);
        registerSkybox(stormyRenderPass, 1);
        registerSkybox(oceanRenderPass, 2);
        registerSkybox(eclipseRenderPass, 3);
        registerSkybox(iridiaRenderPass, 4);
        registerSkybox(forestRenderPass, 5);
        registerSkybox(lightRenderPass, 6);
    }

    public static void renderBlockSkybox(PoseStack poseStack, SkyboxTranslation translation,
                                         ShaderInstance shaderInstance,
                                         int r, int g, int b, int a,
                                         ResourceLocation frontTexture, ResourceLocation backTexture, ResourceLocation leftTexture,
                                         ResourceLocation rightTexture, ResourceLocation topTexture, ResourceLocation bottomTexture) {
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(() -> shaderInstance);

        float far = Minecraft.getInstance().gameRenderer.getRenderDistance();

        poseStack.pushPose();

        //Front face (−Z, north)
        RenderSystem.setShaderTexture(0, frontTexture);
        drawQuad(poseStack,
                -far, -far, -far,
                far, -far, -far,
                far, far, -far,
                -far, far, -far,
                r, g, b, a,
                translation.frontRot,
                translation.frontFlip
        );

        //Back face (+Z, south)
        RenderSystem.setShaderTexture(0, backTexture);
        drawQuad(poseStack,
                far, -far, far,
                -far, -far, far,
                -far, far, far,
                far, far, far,
                r, g, b, a,
                translation.backRot,
                translation.backFlip
        );

        //Left face (+X, east)
        RenderSystem.setShaderTexture(0, leftTexture);
        drawQuad(poseStack,
                far, -far, -far,
                far, -far, far,
                far, far, far,
                far, far, -far,
                r, g, b, a,
                translation.leftRot,
                translation.leftFlip
        );

        //Right face (−X, west)
        RenderSystem.setShaderTexture(0, rightTexture);
        drawQuad(poseStack,
                -far, -far, far,
                -far, -far, -far,
                -far, far, -far,
                -far, far, far,
                r, g, b, a,
                translation.rightRot,
                translation.rightFlip
        );

        //Top face (+Y)
        RenderSystem.setShaderTexture(0, topTexture);
        drawQuad(poseStack,
                -far, far, -far,
                far, far, -far,
                far, far, far,
                -far, far, far,
                r, g, b, a,
                translation.topRot,
                translation.topFlip
        );

        //Bottom face (−Y)
        RenderSystem.setShaderTexture(0, bottomTexture);
        drawQuad(poseStack,
                -far, -far, far,
                far, -far, far,
                far, -far, -far,
                -far, -far, -far,
                r, g, b, a,
                translation.bottomRot,
                translation.bottomFlip
        );

        poseStack.popPose();

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    public static VertexConsumer startQuad() {
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        return bufferBuilder;
    }

    public static void endQuad() {
        Tesselator.getInstance().end();
    }

    private static void vertexUV(VertexConsumer vc, Matrix4f m,
                                 float x, float y, float z,
                                 float u, float v,
                                 int r, int g, int b, int a,
                                 SkyboxTranslation.SkyboxRotation rotation,
                                 SkyboxTranslation.SkyboxFlip flip) {

        float uu = u;
        float vv = v;

        switch (rotation) {
            case ROTATE_90_CW -> {
                float t = uu;
                uu = 1.0F - vv;
                vv = t;
            }
            case ROTATE_180 -> {
                uu = 1.0F - uu;
                vv = 1.0F - vv;
            }
            case ROTATE_90_CCW-> {
                float t = uu;
                uu = vv;
                vv = 1.0F - t;
            }
            default -> {

            }
        }

        switch (flip) {
            case HORIZONTAL -> {
                uu = 1.0F - uu;
            }
            case VERTICAL -> {
                vv = 1.0F - vv;
            }
            case BOTH -> {
                uu = 1.0F - uu;
                vv = 1.0F - vv;
            }
            default -> {

            }
        }

        vc.vertex(m, x, y, z)
                .uv(uu, vv)
                .color(r, g, b, a)
                .endVertex();
    }

    private static void drawQuad(PoseStack poseStack,
                                 float x1, float y1, float z1,
                                 float x2, float y2, float z2,
                                 float x3, float y3, float z3,
                                 float x4, float y4, float z4,
                                 int r, int g, int b, int a,
                                 SkyboxTranslation.SkyboxRotation rotation, SkyboxTranslation.SkyboxFlip flip) {

        VertexConsumer vc = startQuad();
        Matrix4f m = poseStack.last().pose();

        vertexUV(vc, m, x1, y1, z1, 0.0F, 1.0F, r, g, b, a, rotation, flip);
        vertexUV(vc, m, x2, y2, z2, 0.0F, 0.0F, r, g, b, a, rotation, flip);
        vertexUV(vc, m, x3, y3, z3, 1.0F, 0.0F, r, g, b, a, rotation, flip);
        vertexUV(vc, m, x4, y4, z4, 1.0F, 1.0F, r, g, b, a, rotation, flip);

        endQuad();
    }

    public static void renderBlockSkybox(
            PoseStack poseStack,
            ResourceLocation folder,
            ShaderInstance shaderInstance,
            int r, int g, int b, int a,
            SkyboxTranslation translation) {
        renderBlockSkybox(poseStack,
                translation,
                shaderInstance,
                r, g, b, a,
                folder.withSuffix("/front.png"),
                folder.withSuffix("/back.png"),
                folder.withSuffix("/left.png"),
                folder.withSuffix("/right.png"),
                folder.withSuffix("/top.png"),
                folder.withSuffix("/bottom.png")
        );
    }

    public static void renderBlockSkybox(PoseStack poseStack, ResourceLocation folder, ShaderInstance shaderInstance, SkyboxTranslation translation) {
        renderBlockSkybox(poseStack,
                translation,
                shaderInstance,
                255, 255, 255, 255,
                folder.withSuffix("/front.png"),
                folder.withSuffix("/back.png"),
                folder.withSuffix("/left.png"),
                folder.withSuffix("/right.png"),
                folder.withSuffix("/top.png"),
                folder.withSuffix("/bottom.png")
        );
    }
}