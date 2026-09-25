package com.noodlegamer76.shadered.mixin.compat.embeddium;

import com.noodlegamer76.shadered.compat.embeddium.TerrainRenderPassAddition;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DefaultTerrainRenderPasses.class, remap = false)
public class DefaultTerrainRenderPassesMixin {

    @Shadow
    @Final
    @Mutable
    public static TerrainRenderPass[] ALL;

    @Shadow
    @Final
    public static TerrainRenderPass SOLID;

    @Shadow
    @Final
    public static TerrainRenderPass CUTOUT;

    @Shadow
    @Final
    public static TerrainRenderPass TRANSLUCENT;

    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void shadered$addCustomPass(CallbackInfo ci) {
        ALL = new TerrainRenderPass[] {
                SOLID,
                CUTOUT,
                TRANSLUCENT,
                TerrainRenderPassAddition.SKYBLOCK
        };
    }
}