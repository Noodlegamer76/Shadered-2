package com.noodlegamer76.shadered.tab;

import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.world.item.InitItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class InitCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Shadered.MODID);

    public static RegistryObject<CreativeModeTab> SHADERED_TAB = CREATIVE_TABS.register("shadered_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("shadered.creative_tab"))
            .icon(() -> new ItemStack(InitItems.SPACE_SKYBLOCK.get()))
            .build());
}
