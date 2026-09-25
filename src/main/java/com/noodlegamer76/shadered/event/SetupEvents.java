package com.noodlegamer76.shadered.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.client.assimp.load.AssimpLoader;
import com.noodlegamer76.shadered.client.renderer.skybox.SkyblockRenderer;
import com.noodlegamer76.shadered.client.renderer.skybox.SkyboxRenderer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Shadered.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents {

    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        //TODO: make a proper loading system that works with resource reloads
        SkyboxRenderer.getInstance().setup();
        event.enqueueWork(() -> RenderSystem.recordRenderCall(AssimpLoader::load));
    }
}
