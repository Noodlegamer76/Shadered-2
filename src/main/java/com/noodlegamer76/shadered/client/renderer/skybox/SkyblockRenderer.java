package com.noodlegamer76.shadered.client.renderer.skybox;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.noodlegamer76.shadered.world.block.InitBlocks;
import com.noodlegamer76.shadered.world.block.ModBlockStateProperties;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import org.joml.Vector4f;
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
            CallbackInfo ci) {

        if (pState.is(InitBlocks.SKYBLOCK.get())) {
            int skyblock = pState.getValue(ModBlockStateProperties.SKYBLOCK);
            int packedSkyblock = LightTexture.pack(skyblock, 0);

            int[] vertices = pQuad.getVertices();
            float[] afloat = new float[]{pBrightness0, pBrightness1, pBrightness2, pBrightness3};

            Matrix4f poseMatrix = pPose.pose();

            float[][] uv = {
                    {0.0F, 0.0F},
                    {1.0F, 0.0F},
                    {1.0F, 1.0F},
                    {0.0F, 1.0F}
            };

            for (int k = 0; k < 4; ++k) {
                int offset = k * 8;

                float x = Float.intBitsToFloat(vertices[offset]);
                float y = Float.intBitsToFloat(vertices[offset + 1]);
                float z = Float.intBitsToFloat(vertices[offset + 2]);

                Vector4f worldPos = poseMatrix.transform(new Vector4f(x, y, z, 1.0F));

                pConsumer.vertex(worldPos.x(), worldPos.y(), worldPos.z())
                        .color(afloat[k], afloat[k], afloat[k], 1.0F)
                        .uv(uv[k][0], uv[k][1])
                        .overlayCoords(0, 0)
                        .uv2(packedSkyblock)
                        .normal(0, 0, 0)
                        .endVertex();
            }

            ci.cancel();
        }
    }
}
