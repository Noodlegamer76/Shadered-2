package com.noodlegamer76.shadered.block;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class Skyblock extends Block {
    public static final IntegerProperty SKYBLOCK = ModBlockStateProperties.SKYBLOCK;

    public Skyblock(Properties pProperties) {
        super(pProperties);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(SKYBLOCK, 0);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SKYBLOCK);
    }
}
