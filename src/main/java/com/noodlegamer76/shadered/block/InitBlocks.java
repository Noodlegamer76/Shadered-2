package com.noodlegamer76.shadered.block;

import com.noodlegamer76.shadered.Shadered;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class InitBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Shadered.MODID);

    public static final RegistryObject<Skyblock> SKYBLOCK = BLOCKS.register("skyblock",
            () -> new Skyblock(BlockBehaviour.Properties.of()));
}
