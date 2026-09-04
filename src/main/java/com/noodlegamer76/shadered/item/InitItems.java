package com.noodlegamer76.shadered.item;

import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.block.InitBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Shadered.MODID);

    public static final RegistryObject<Item> SKYBLOCK = ITEMS.register("skyblock",
            () -> new BlockItem(InitBlocks.SKYBLOCK.get(), new Item.Properties()));
}
