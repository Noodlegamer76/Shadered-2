package com.noodlegamer76.shadered.client.renderer.skybox;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.noodlegamer76.shadered.compat.ModCompatUtils;
import com.noodlegamer76.shadered.compat.framedblocks.FramedBlocksCompat;
import com.noodlegamer76.shadered.world.block.ModBlockStateProperties;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class SkyblockRenderer {

    //Moving this here so I can hotswap since I cant in mixin classes.
    public static void shadered$putQuadData(
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
            BlockColors blockColors,
            CallbackInfo ci) {
        int skyblock = -1;

        if (pState.hasProperty(ModBlockStateProperties.SKYBLOCK)) {
            skyblock = pState.getValue(ModBlockStateProperties.SKYBLOCK);
        }
        else if (ModCompatUtils.isFramedBlocksLoaded() && FramedBlocksCompat.isFramedBlock(pState)) {
            skyblock = FramedBlocksCompat.getFramedBlockSkybox(pLevel, pState, pPos);
        }

        if (skyblock != -1) {
            int packedSkyblock = LightTexture.pack(skyblock, 0);

            float f;
            float f1;
            float f2;
            if (pQuad.isTinted()) {
                int i = blockColors.getColor(pState, pLevel, pPos, pQuad.getTintIndex());
                f = (float) (i >> 16 & 255) / 255.0F;
                f1 = (float) (i >> 8 & 255) / 255.0F;
                f2 = (float) (i & 255) / 255.0F;
            } else {
                f = 1.0F;
                f1 = 1.0F;
                f2 = 1.0F;
            }

            pConsumer.putBulkData(pPose, pQuad, new float[]{pBrightness0, pBrightness1, pBrightness2, pBrightness3}, f, f1, f2, new int[]{packedSkyblock, packedSkyblock, packedSkyblock, packedSkyblock}, pPackedOverlay, true);
            ci.cancel();
        }
    }
}
