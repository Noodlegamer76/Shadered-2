package com.noodlegamer76.shadered.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.noodlegamer76.shadered.client.renderer.skybox.SkyblockRenderer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin {

    @Inject(
            method = "putQuadData",
            at = @At("HEAD"),
            cancellable = true
    )
    public void shadered$putQuadData(
            BlockAndTintGetter pLevel,
            BlockState pState,
            BlockPos pPos,
            VertexConsumer pConsumer,
            PoseStack.Pose pPose,
            BakedQuad pQuad,
            float pBrightness0,
            float pBrightness1,
            float pBrightness2,
            float pBrightness3,
            int pLightmap0,
            int pLightmap1,
            int pLightmap2,
            int pLightmap3,
            int pPackedOverlay,
            CallbackInfo ci) {
        SkyblockRenderer.shadered$putQuadData(
                pLevel,
                pState,
                pPos,
                pConsumer,
                pPose,
                pQuad,
                pBrightness0,
                pBrightness1,
                pBrightness2,
                pBrightness3,
                pLightmap0,
                pLightmap1,
                pLightmap2,
                pLightmap3,
                pPackedOverlay,
                ci
        );
    }
}
