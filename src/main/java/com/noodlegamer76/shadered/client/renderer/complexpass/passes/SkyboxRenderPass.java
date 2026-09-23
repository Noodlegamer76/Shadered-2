package com.noodlegamer76.shadered.client.renderer.complexpass.passes;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.noodlegamer76.shadered.client.renderer.complexpass.ComplexPassRenderer;
import com.noodlegamer76.shadered.client.renderer.complexpass.PassType;
import com.noodlegamer76.shadered.client.renderer.complexpass.RenderStage;
import com.noodlegamer76.shadered.client.renderer.complexpass.RenderableComplexPass;
import com.noodlegamer76.shadered.client.renderer.skybox.SkyboxRenderer;
import com.noodlegamer76.shadered.client.renderer.skybox.SkyboxTranslation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class SkyboxRenderPass implements RenderableComplexPass {
    private final ResourceLocation texturePath;
    private final SkyboxTranslation translation;
    private final Vector3f skyboxRotationSpeed;
    private final Supplier<ShaderInstance> skyboxShader;
    private TextureTarget skyboxTarget;

    public SkyboxRenderPass(ResourceLocation texturePath, SkyboxTranslation translation, Vector3f skyboxRotationSpeed, Supplier<ShaderInstance> skyboxShader) {
        this.texturePath = texturePath;
        this.translation = translation;
        this.skyboxRotationSpeed = skyboxRotationSpeed;
        this.skyboxShader = skyboxShader;
    }

    public SkyboxRenderPass(ResourceLocation texturePath, SkyboxTranslation translation) {
        this(texturePath, translation, new Vector3f(), GameRenderer::getPositionTexColorShader);
    }

    public ResourceLocation getTexturePath() {
        return texturePath;
    }

    public SkyboxTranslation getTranslation() {
        return translation;
    }

    @Override
    public PassType getType() {
        return PassType.GEOMETRY;
    }

    @Override
    public void render(RenderStage stage, PoseStack poseStack, int renderTick, float partialTick) {
        ComplexPassRenderer renderer = ComplexPassRenderer.getInstance();

        if (skyboxTarget == null) {
            skyboxTarget = new TextureTarget(renderer.getPreviousWidth(), renderer.getPreviousHeight(), false, Minecraft.ON_OSX);
        }
        else if (skyboxTarget.width != renderer.getPreviousWidth() || skyboxTarget.height != renderer.getPreviousHeight()) {
            skyboxTarget.resize(renderer.getPreviousWidth(), renderer.getPreviousHeight(), Minecraft.ON_OSX);
        }

        skyboxTarget.bindWrite(true);
        poseStack.pushPose();
        float ticks = (renderTick + partialTick);

        if (skyboxRotationSpeed.lengthSquared() > 0) {
            Quaternionf rotation = new Quaternionf();
            rotation.mul(Axis.XP.rotationDegrees(ticks * skyboxRotationSpeed.x));
            rotation.mul(Axis.YN.rotationDegrees(ticks * skyboxRotationSpeed.y));
            rotation.mul(Axis.ZP.rotationDegrees(ticks * skyboxRotationSpeed.z));

            poseStack.mulPose(rotation);
        }

        RenderSystem.setShader(skyboxShader);
        SkyboxRenderer.renderBlockSkybox(poseStack, texturePath,
                skyboxShader.get(),
                translation
        );

        poseStack.popPose();

        renderer.getRenderBuffer().bindWrite(true);
    }

    public TextureTarget getSkyboxTarget() {
        return skyboxTarget;
    }
}
