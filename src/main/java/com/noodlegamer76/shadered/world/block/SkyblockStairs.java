package com.noodlegamer76.shadered.world.block;

import com.noodlegamer76.shadered.world.item.SkyblockItem;
import com.noodlegamer76.shadered.world.item.SkyblockItemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.function.Supplier;

public class SkyblockStairs extends StairBlock {
    public static final IntegerProperty SKYBLOCK = ModBlockStateProperties.SKYBLOCK;

    public SkyblockStairs(Supplier<BlockState> baseState, Properties pProperties) {
        super(baseState, pProperties);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = super.getStateForPlacement(pContext);
        ItemStack item = pContext.getItemInHand();
        if (!(item.getItem() instanceof SkyblockItem skyblock)) {
            return state.setValue(SKYBLOCK, 0);
        }
        return state.setValue(SKYBLOCK, skyblock.getSkyblockId());
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        int skyblockId = pState.getValue(SKYBLOCK);
        SkyblockItem item = SkyblockItem.getSkyblockItem(skyblockId, SkyblockItemType.STAIRS);
        ItemStack stack = item.getDefaultInstance();
        List<ItemStack> drops = super.getDrops(pState, pParams);
        drops.add(stack);
        return drops;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        int skyblockId = state.getValue(SKYBLOCK);
        SkyblockItem item = SkyblockItem.getSkyblockItem(skyblockId, SkyblockItemType.STAIRS);
        return item.getDefaultInstance();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(SKYBLOCK);
    }
}
