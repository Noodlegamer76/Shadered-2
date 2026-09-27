package com.noodlegamer76.shadered.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.client.assimp.load.AssimpLoader;
import com.noodlegamer76.shadered.client.renderer.skybox.SkyboxRenderer;
import com.noodlegamer76.shadered.client.util.ModRenderTypes;
import com.noodlegamer76.shadered.compat.ModCompatUtils;
import com.noodlegamer76.shadered.compat.create.CreateClientSetup;
import com.noodlegamer76.shadered.world.block.InitBlocks;
import com.simibubi.create.AllBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.LoadingModList;
import software.bernie.example.registry.BlockRegistry;

@Mod.EventBusSubscriber(modid = Shadered.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        //TODO: make a proper loading system that works with resource reloads
        SkyboxRenderer.getInstance().setup();
        event.enqueueWork(() -> RenderSystem.recordRenderCall(AssimpLoader::load));

        ItemBlockRenderTypes.setRenderLayer(InitBlocks.SKYBLOCK.get(), ModRenderTypes.SKYBLOCK);
        ItemBlockRenderTypes.setRenderLayer(InitBlocks.SKYBLOCK_STAIRS.get(), ModRenderTypes.SKYBLOCK);
        ItemBlockRenderTypes.setRenderLayer(InitBlocks.SKYBLOCK_SLAB.get(), ModRenderTypes.SKYBLOCK);

        boolean isCreateLoaded = ModCompatUtils.isCreateLoaded();
        if (isCreateLoaded) {
            CreateClientSetup.setupClient(event);
        }
    }
}
