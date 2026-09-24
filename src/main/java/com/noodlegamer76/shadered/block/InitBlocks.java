package com.noodlegamer76.shadered.block;

import com.noodlegamer76.shadered.Shadered;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

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
}
