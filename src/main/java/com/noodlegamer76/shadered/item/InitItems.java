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

    public static final RegistryObject<Item> SPACE_SKYBLOCK = ITEMS.register("space_skyblock",
            () -> new SkyblockItem(0, new Item.Properties()));

    public static final RegistryObject<Item> STORMY_SKYBLOCK = ITEMS.register("stormy_skyblock",
            () -> new SkyblockItem(1, new Item.Properties()));

    public static final RegistryObject<Item> OCEAN_SKYBLOCK = ITEMS.register("ocean_skyblock",
            () -> new SkyblockItem(2, new Item.Properties()));

    public static final RegistryObject<Item> ECLIPSE_SKYBLOCK = ITEMS.register("eclipse_skyblock",
            () -> new SkyblockItem(3, new Item.Properties()));

    public static final RegistryObject<Item> IRIDIA_SKYBLOCK = ITEMS.register("iridia_skyblock",
            () -> new SkyblockItem(4, new Item.Properties()));

    public static final RegistryObject<Item> FOREST_SKYBLOCK = ITEMS.register("forest_skyblock",
            () -> new SkyblockItem(5, new Item.Properties()));

    public static final RegistryObject<Item> LIGHT_SKYBLOCK = ITEMS.register("light_skyblock",
            () -> new SkyblockItem(6, new Item.Properties()));
}
