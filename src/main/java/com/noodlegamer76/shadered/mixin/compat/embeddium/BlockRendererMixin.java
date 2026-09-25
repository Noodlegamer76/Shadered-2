package com.noodlegamer76.shadered.mixin.compat.embeddium;

import com.noodlegamer76.shadered.world.block.Skyblock;
import com.noodlegamer76.shadered.compat.embeddium.TerrainRenderPassAddition;
import me.jellysquid.mods.sodium.client.model.light.data.QuadLightData;
import me.jellysquid.mods.sodium.client.model.quad.BakedQuadView;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BlockRenderer.class, remap = false)
public abstract class BlockRendererMixin {

    @Shadow
    protected abstract void writeGeometry(BlockRenderContext ctx, ChunkModelBuilder builder, Vec3 offset, Material material, BakedQuadView quad, int[] colors, QuadLightData light);

    @Redirect(
            method = "renderQuadList",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;writeGeometry(Lme/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderContext;Lme/jellysquid/mods/sodium/client/render/chunk/compile/buffers/ChunkModelBuilder;Lnet/minecraft/world/phys/Vec3;Lme/jellysquid/mods/sodium/client/render/chunk/terrain/material/Material;Lme/jellysquid/mods/sodium/client/model/quad/BakedQuadView;[ILme/jellysquid/mods/sodium/client/model/light/data/QuadLightData;)V")
    )
    public void shadered$writeSkyblockData(
            BlockRenderer instance,
            BlockRenderContext ctx,
            ChunkModelBuilder builder,
            Vec3 offset,
            Material material,
            BakedQuadView quad,
            int[] colors,
            QuadLightData light
    ) {
        if (material == TerrainRenderPassAddition.SKYBLOCK_MATERIAL && ctx.state().hasProperty(Skyblock.SKYBLOCK)) {
            int skyblock = ctx.state().getValue(Skyblock.SKYBLOCK);
            int packedSkyblock = LightTexture.pack(skyblock, 0);
            for (int i = 0; i < 4; i++) {
                light.lm[i] = packedSkyblock;
            }
        }

        writeGeometry(ctx, builder, offset, material, quad, colors, light);
    }
}
