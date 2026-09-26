package com.noodlegamer76.shadered.world.block;

import com.noodlegamer76.shadered.Shadered;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class InitBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Shadered.MODID);

    public static final RegistryObject<Skyblock> SKYBLOCK = BLOCKS.register("skyblock",
            () -> new Skyblock(BlockBehaviour.Properties.of()
                    .isValidSpawn(((pState, pLevel, pPos, pValue) -> false))
                    .mapColor(DyeColor.WHITE)
                    .instrument(NoteBlockInstrument.XYLOPHONE)
                    .strength(0.5f, 1.5f)
                    .requiresCorrectToolForDrops()
            ));

    public static final RegistryObject<SkyblockStairs> SKYBLOCK_STAIRS = BLOCKS.register("skyblock_stairs",
            () -> new SkyblockStairs(() -> SKYBLOCK.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .isValidSpawn(((pState, pLevel, pPos, pValue) -> false))
                    .mapColor(DyeColor.WHITE)
                    .instrument(NoteBlockInstrument.XYLOPHONE)
                    .strength(0.5f, 1.5f)
                    .requiresCorrectToolForDrops()
            ));

    public static final RegistryObject<SkyblockSlab> SKYBLOCK_SLAB = BLOCKS.register("skyblock_slab",
            () -> new SkyblockSlab(BlockBehaviour.Properties.of()
                    .isValidSpawn(((pState, pLevel, pPos, pValue) -> false))
                    .mapColor(DyeColor.WHITE)
                    .instrument(NoteBlockInstrument.XYLOPHONE)
                    .strength(0.5f, 1.5f)
                    .requiresCorrectToolForDrops()
            ));

    public static final RegistryObject<Block> BLACK_BLOCK = BLOCKS.register("black_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.BLACK)
                    .strength(1.0f, 1.5f)
                    .requiresCorrectToolForDrops()
            ));

    public static final RegistryObject<Block> GREEN_SCREEN = BLOCKS.register("green_screen",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.LIME)
                    .strength(1.0f, 1.5f)
                    .requiresCorrectToolForDrops()
                    .emissiveRendering(((pState, pLevel, pPos) -> true))
            ));
}
