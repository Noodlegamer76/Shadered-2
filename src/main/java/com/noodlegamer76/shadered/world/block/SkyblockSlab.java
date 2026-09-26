package com.noodlegamer76.shadered.world.block;

import com.noodlegamer76.shadered.world.item.SkyblockItem;
import com.noodlegamer76.shadered.world.item.SkyblockItemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class SkyblockSlab extends SlabBlock {
    public static final IntegerProperty SKYBLOCK = ModBlockStateProperties.SKYBLOCK;
    private Item item;

    public SkyblockSlab(Properties pProperties) {
        super(pProperties);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = pContext.getLevel().getBlockState(blockpos);
        if (blockstate.is(this)) {
            blockstate = blockstate.setValue(TYPE, SlabType.DOUBLE)
                    .setValue(WATERLOGGED, false);
        }
        else {
            FluidState fluidstate = pContext.getLevel().getFluidState(blockpos);
            BlockState blockstate1 = this.defaultBlockState()
                    .setValue(TYPE, SlabType.BOTTOM)
                    .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            Direction direction = pContext.getClickedFace();
            blockstate = direction != Direction.DOWN && (direction == Direction.UP || !(pContext.getClickLocation().y - (double)blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.setValue(TYPE, SlabType.TOP);
        }

        ItemStack item = pContext.getItemInHand();
        if (!(item.getItem() instanceof SkyblockItem skyblock)) {
            return blockstate.setValue(SKYBLOCK, 0);
        }
        return blockstate.setValue(SKYBLOCK, skyblock.getSkyblockId());
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        SlabType slabtype = pState.getValue(TYPE);
        int skyblockId = pState.getValue(SKYBLOCK);
        if (slabtype != SlabType.DOUBLE && itemstack.is(SkyblockItem.getSkyblockItem(skyblockId, SkyblockItemType.SLAB))) {
            if (pUseContext.replacingClickedOnBlock()) {
                boolean flag = pUseContext.getClickLocation().y - (double)pUseContext.getClickedPos().getY() > 0.5D;
                Direction direction = pUseContext.getClickedFace();
                if (slabtype == SlabType.BOTTOM) {
                    return direction == Direction.UP || flag && direction.getAxis().isHorizontal();
                } else {
                    return direction == Direction.DOWN || !flag && direction.getAxis().isHorizontal();
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        int skyblockId = pState.getValue(SKYBLOCK);
        SkyblockItem item = SkyblockItem.getSkyblockItem(skyblockId, SkyblockItemType.SLAB);
        ItemStack stack = item.getDefaultInstance();
        List<ItemStack> drops = super.getDrops(pState, pParams);
        drops.add(stack);
        return drops;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        int skyblockId = state.getValue(SKYBLOCK);
        SkyblockItem item = SkyblockItem.getSkyblockItem(skyblockId, SkyblockItemType.SLAB);
        return item.getDefaultInstance();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(SKYBLOCK);
    }
}
