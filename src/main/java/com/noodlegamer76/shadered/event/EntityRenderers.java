package com.noodlegamer76.shadered.event;

import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.client.renderer.block.MaxwellRenderer;
import com.noodlegamer76.shadered.world.tile.InitBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Shadered.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityRenderers {

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(InitBlockEntities.MAXWELL.get(), MaxwellRenderer::new);
    }
}
