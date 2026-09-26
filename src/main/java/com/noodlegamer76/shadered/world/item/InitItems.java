package com.noodlegamer76.shadered.world.item;

import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.world.block.InitBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Shadered.MODID);

    public static final RegistryObject<Item> MAXWELL = ITEMS.register("maxwell",
            () -> new MaxwellItem(InitBlocks.MAXWELL.get(), new Item.Properties()));

    public static final RegistryObject<SkyblockItem> SPACE_SKYBLOCK = ITEMS.register("space_skyblock",
            () -> new SkyblockItem(0, SkyblockItemType.BLOCK, InitBlocks.SKYBLOCK.get(), new Item.Properties()));
    public static final RegistryObject<SkyblockItem> SPACE_SKYBLOCK_STAIRS = ITEMS.register("space_skyblock_stairs",
            () -> new SkyblockItem(0, SkyblockItemType.STAIRS, InitBlocks.SKYBLOCK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<SkyblockItem> SPACE_SKYBLOCK_SLAB = ITEMS.register("space_skyblock_slab",
            () -> new SkyblockItem(0, SkyblockItemType.SLAB, InitBlocks.SKYBLOCK_SLAB.get(), new Item.Properties()));

    public static final RegistryObject<SkyblockItem> STORMY_SKYBLOCK = ITEMS.register("stormy_skyblock",
            () -> new SkyblockItem(1, SkyblockItemType.BLOCK, InitBlocks.SKYBLOCK.get(), new Item.Properties()));
    public static final RegistryObject<SkyblockItem> STORMY_SKYBLOCK_STAIRS = ITEMS.register("stormy_skyblock_stairs",
            () -> new SkyblockItem(1, SkyblockItemType.STAIRS, InitBlocks.SKYBLOCK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<SkyblockItem> STORMY_SKYBLOCK_SLAB = ITEMS.register("stormy_skyblock_slab",
            () -> new SkyblockItem(1, SkyblockItemType.SLAB, InitBlocks.SKYBLOCK_SLAB.get(), new Item.Properties()));

    public static final RegistryObject<SkyblockItem> OCEAN_SKYBLOCK = ITEMS.register("ocean_skyblock",
            () -> new SkyblockItem(2, SkyblockItemType.BLOCK, InitBlocks.SKYBLOCK.get(),  new Item.Properties()));
    public static final RegistryObject<SkyblockItem> OCEAN_SKYBLOCK_STAIRS = ITEMS.register("ocean_skyblock_stairs",
            () -> new SkyblockItem(2, SkyblockItemType.STAIRS, InitBlocks.SKYBLOCK_STAIRS.get(),  new Item.Properties()));
    public static final RegistryObject<SkyblockItem> OCEAN_SKYBLOCK_SLAB = ITEMS.register("ocean_skyblock_slab",
            () -> new SkyblockItem(2, SkyblockItemType.SLAB, InitBlocks.SKYBLOCK_SLAB.get(),  new Item.Properties()));

    public static final RegistryObject<SkyblockItem> ECLIPSE_SKYBLOCK = ITEMS.register("eclipse_skyblock",
            () -> new SkyblockItem(3, SkyblockItemType.BLOCK, InitBlocks.SKYBLOCK.get(),  new Item.Properties()));
    public static final RegistryObject<SkyblockItem> ECLIPSE_SKYBLOCK_STAIRS = ITEMS.register("eclipse_skyblock_stairs",
            () -> new SkyblockItem(3, SkyblockItemType.STAIRS, InitBlocks.SKYBLOCK_STAIRS.get(),  new Item.Properties()));
    public static final RegistryObject<SkyblockItem> ECLIPSE_SKYBLOCK_SLAB = ITEMS.register("eclipse_skyblock_slab",
            () -> new SkyblockItem(3, SkyblockItemType.SLAB, InitBlocks.SKYBLOCK_SLAB.get(),  new Item.Properties()));

    public static final RegistryObject<SkyblockItem> IRIDIA_SKYBLOCK = ITEMS.register("iridia_skyblock",
            () -> new SkyblockItem(4, SkyblockItemType.BLOCK, InitBlocks.SKYBLOCK.get(),  new Item.Properties()));
    public static final RegistryObject<SkyblockItem> IRIDIA_SKYBLOCK_STAIRS = ITEMS.register("iridia_skyblock_stairs",
            () -> new SkyblockItem(4, SkyblockItemType.STAIRS, InitBlocks.SKYBLOCK_STAIRS.get(),  new Item.Properties()));
    public static final RegistryObject<SkyblockItem> IRIDIA_SKYBLOCK_SLAB = ITEMS.register("iridia_skyblock_slab",
            () -> new SkyblockItem(4, SkyblockItemType.SLAB, InitBlocks.SKYBLOCK_SLAB.get(),  new Item.Properties()));

    public static final RegistryObject<SkyblockItem> FOREST_SKYBLOCK = ITEMS.register("forest_skyblock",
            () -> new SkyblockItem(5, SkyblockItemType.BLOCK, InitBlocks.SKYBLOCK.get(),  new Item.Properties()));
    public static final RegistryObject<SkyblockItem> FOREST_SKYBLOCK_STAIRS = ITEMS.register("forest_skyblock_stairs",
            () -> new SkyblockItem(5, SkyblockItemType.STAIRS, InitBlocks.SKYBLOCK_STAIRS.get(),  new Item.Properties()));
    public static final RegistryObject<SkyblockItem> FOREST_SKYBLOCK_SLAB = ITEMS.register("forest_skyblock_slab",
            () -> new SkyblockItem(5, SkyblockItemType.SLAB, InitBlocks.SKYBLOCK_SLAB.get(),  new Item.Properties()));

    public static final RegistryObject<SkyblockItem> LIGHT_SKYBLOCK = ITEMS.register("light_skyblock",
            () -> new SkyblockItem(6, SkyblockItemType.BLOCK, InitBlocks.SKYBLOCK.get(),  new Item.Properties()));
    public static final RegistryObject<SkyblockItem> LIGHT_SKYBLOCK_STAIRS = ITEMS.register("light_skyblock_stairs",
            () -> new SkyblockItem(6, SkyblockItemType.STAIRS, InitBlocks.SKYBLOCK_STAIRS.get(),  new Item.Properties()));
    public static final RegistryObject<SkyblockItem> LIGHT_SKYBLOCK_SLAB = ITEMS.register("light_skyblock_slab",
            () -> new SkyblockItem(6, SkyblockItemType.SLAB, InitBlocks.SKYBLOCK_SLAB.get(),  new Item.Properties()));


    public static final RegistryObject<BlockItem> BLACK_BLOCK = ITEMS.register("black_block",
            () -> new BlockItem(InitBlocks.BLACK_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> GREEN_SCREEN = ITEMS.register("green_screen",
            () -> new BlockItem(InitBlocks.GREEN_SCREEN.get(), new Item.Properties()));
}
