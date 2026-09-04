package com.noodlegamer76.shadered.mixin;

import com.google.common.collect.ImmutableList;
import com.noodlegamer76.shadered.client.util.ModRenderTypes;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(RenderType.class)
public class RenderTypeMixin {

    @Inject(
            method = "chunkBufferLayers",
            at = @At(value = "RETURN"),
            cancellable = true)
    private static void onChunkBufferLayers(CallbackInfoReturnable<List<RenderType>> cir) {
        List<RenderType> immutable = cir.getReturnValue();
        List<RenderType> list = new ArrayList<>(immutable);
        list.add(
                ModRenderTypes.SKYBLOCK
        );
        cir.setReturnValue(ImmutableList.copyOf(list));
        cir.cancel();
    }
}
