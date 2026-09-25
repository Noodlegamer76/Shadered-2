package com.noodlegamer76.shadered;

import com.mojang.logging.LogUtils;
import com.noodlegamer76.shadered.block.InitBlocks;
import com.noodlegamer76.shadered.item.InitItems;
import com.noodlegamer76.shadered.tab.InitCreativeTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Shadered.MODID)
public class Shadered {
    public static final String MODID = "shadered";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Shadered() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        NativeLibraryLoader.loadNatives();

        InitItems.ITEMS.register(modEventBus);
        InitBlocks.BLOCKS.register(modEventBus);
        InitCreativeTabs.CREATIVE_TABS.register(modEventBus);
    }
}
