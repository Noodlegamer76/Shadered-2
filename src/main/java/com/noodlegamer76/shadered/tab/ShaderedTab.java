package com.noodlegamer76.shadered.tab;

import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.world.item.InitItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Shadered.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ShaderedTab {

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == InitCreativeTabs.SHADERED_TAB.get()) {
            event.accept(InitItems.SPACE_SKYBLOCK);
            event.accept(InitItems.STORMY_SKYBLOCK);
            event.accept(InitItems.OCEAN_SKYBLOCK);
            event.accept(InitItems.ECLIPSE_SKYBLOCK);
            event.accept(InitItems.IRIDIA_SKYBLOCK);
            event.accept(InitItems.FOREST_SKYBLOCK);
            event.accept(InitItems.LIGHT_SKYBLOCK);
            event.accept(InitItems.BLACK_BLOCK);
            event.accept(InitItems.GREEN_SCREEN);
        }
    }
}
