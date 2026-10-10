package com.noodlegamer76.shadered.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.noodlegamer76.shadered.client.renderer.assimp.AssimpRenderer;
import com.noodlegamer76.shadered.client.renderer.assimp.MinecraftLightUvData;
import com.noodlegamer76.shadered.client.util.LightPackingUtils;
import net.minecraft.client.renderer.ChunkBufferBuilderPack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.chunk.ChunkRenderDispatcher$RenderChunk$RebuildTask")
public abstract class RebuildTaskMixin {

    @Inject(
            method = "compile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;betweenClosed(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Ljava/lang/Iterable;"
            )
    )
    private void onCompileLoop(
            float pX, float pY, float pZ,
            ChunkBufferBuilderPack pChunkBufferBuilderPack,
            CallbackInfoReturnable<?> cir,
            @Local RenderChunkRegion renderchunkregion,
            @Local(ordinal = 0) BlockPos blockPos
    ) {
        if (renderchunkregion == null) {
            return;
        }

        MinecraftLightUvData.SubchunkUvs uvs = new MinecraftLightUvData.SubchunkUvs();

        Iterable<BlockPos> positions = BlockPos.betweenClosed(blockPos, blockPos.offset(15, 15, 15));
        positions.forEach((current -> {
            int bl = renderchunkregion.getBrightness(LightLayer.BLOCK, current);
            int sl = renderchunkregion.getBrightness(LightLayer.SKY, current);
            byte packedLight = LightPackingUtils.packLight(bl, sl);
            uvs.setValue(current, packedLight);
        }));

        SectionPos pos = SectionPos.of(blockPos);
        AssimpRenderer.getInstance().getMinecraftLightUvData().addDirtySubchunk(pos, uvs);
    }
}