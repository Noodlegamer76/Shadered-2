package com.noodlegamer76.shadered.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.noodlegamer76.shadered.block.InitBlocks;
import com.noodlegamer76.shadered.block.ModBlockStateProperties;
import com.noodlegamer76.shadered.block.Skyblock;
import net.minecraft.client.renderer.LightTexture;
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
            at = @At(
                    value = "HEAD"
            ),
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
        if (pState.is(InitBlocks.SKYBLOCK.get())) {
            int packedSkyblock = LightTexture.pack(pState.getValue(ModBlockStateProperties.SKYBLOCK), 0);
            pConsumer.putBulkData(pPose, pQuad, new float[]{pBrightness0, pBrightness1, pBrightness2, pBrightness3}, 1, 1, 1, new int[]{packedSkyblock, packedSkyblock, packedSkyblock, packedSkyblock}, pPackedOverlay, true);
            ci.cancel();
        }
    }
}
