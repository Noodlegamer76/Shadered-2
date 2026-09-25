package com.noodlegamer76.shadered.mixin.compat.iris;

import com.noodlegamer76.shadered.client.util.ModRenderTypes;
import net.irisshaders.iris.pipeline.WorldRenderingPhase;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WorldRenderingPhase.class, remap = false)
public class WorldRenderingPhaseMixin {

    @Shadow
    @Final
    public static WorldRenderingPhase TERRAIN_SOLID;

    @Inject(
            method = "fromTerrainRenderType",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void shadered$skyblockDummy(RenderType renderType, CallbackInfoReturnable<WorldRenderingPhase> cir) {
        if (renderType == ModRenderTypes.SKYBLOCK) {
            cir.setReturnValue(WorldRenderingPhase.TERRAIN_SOLID);
        }
    }
}
