package com.noodlegamer76.shadered.world.tile;

import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.world.block.InitBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitBlockEntities { public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Shadered.MODID);

    public static final RegistryObject<BlockEntityType<MaxwellEntity>> MAXWELL = BLOCK_ENTITIES.register("maxwell",
            () -> BlockEntityType.Builder.of(MaxwellEntity::new, InitBlocks.MAXWELL.get()).build(null));
}
