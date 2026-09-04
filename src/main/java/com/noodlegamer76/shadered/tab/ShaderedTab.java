package com.noodlegamer76.shadered.tab;

import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.item.InitItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Shadered.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ShaderedTab {

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == InitCreativeTabs.SHADERED_TAB.get()) {
            event.accept(InitItems.SKYBLOCK);
        }
    }
}
