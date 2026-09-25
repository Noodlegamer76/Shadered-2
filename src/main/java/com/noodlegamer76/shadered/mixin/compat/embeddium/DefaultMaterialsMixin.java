package com.noodlegamer76.shadered.mixin.compat.embeddium;

import com.noodlegamer76.shadered.client.util.ModRenderTypes;
import com.noodlegamer76.shadered.compat.embeddium.TerrainRenderPassAddition;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DefaultMaterials.class, remap = false)
public class DefaultMaterialsMixin {

    @Inject(
            method = "forRenderLayer",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void shadered$forRenderLayerFix(RenderType layer, CallbackInfoReturnable<Material> cir) {
        if (layer == ModRenderTypes.SKYBLOCK) {
            cir.setReturnValue(TerrainRenderPassAddition.SKYBLOCK_MATERIAL);
        }
    }
}
