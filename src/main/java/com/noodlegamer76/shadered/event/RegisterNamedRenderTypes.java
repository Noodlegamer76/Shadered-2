package com.noodlegamer76.shadered.event;

import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.client.util.ModRenderTypes;
import net.minecraft.client.renderer.Sheets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterNamedRenderTypesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Shadered.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterNamedRenderTypes {

    @SubscribeEvent
    public static void onNamedRenderTypes(RegisterNamedRenderTypesEvent event) {
        event.register("skyblock", ModRenderTypes.SKYBLOCK, Sheets.chestSheet());
    }
}