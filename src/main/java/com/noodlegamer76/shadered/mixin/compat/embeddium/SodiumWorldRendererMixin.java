package com.noodlegamer76.shadered.mixin.compat.embeddium;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.shadered.compat.embeddium.TerrainRenderPassAddition;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SodiumWorldRenderer.class, remap = false)
public class SodiumWorldRendererMixin {

    @Shadow
    private RenderSectionManager renderSectionManager;

    @Inject(
            method = "drawChunkLayer",
            at = @At(value = "HEAD")
    )
    public void shadered$drawSkyblockChunkLayer(RenderType renderLayer, PoseStack matrixStack, double x, double y, double z, CallbackInfo ci) {
        ChunkRenderMatrices matrices = ChunkRenderMatrices.from(matrixStack);

        if (renderLayer == RenderType.solid()) {
            renderSectionManager.renderLayer(matrices, TerrainRenderPassAddition.SKYBLOCK, x, y, z);
        }
    }
}
