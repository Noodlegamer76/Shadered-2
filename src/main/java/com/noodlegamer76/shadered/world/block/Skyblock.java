package com.noodlegamer76.shadered.world.block;

import com.noodlegamer76.shadered.world.item.SkyblockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class Skyblock extends Block {
    public static final IntegerProperty SKYBLOCK = ModBlockStateProperties.SKYBLOCK;

    public Skyblock(Properties pProperties) {
        super(pProperties);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        ItemStack item = pContext.getItemInHand();
        if (!(item.getItem() instanceof SkyblockItem skyblock)) {
            return this.defaultBlockState().setValue(SKYBLOCK, 0);
        }
        return this.defaultBlockState().setValue(SKYBLOCK, skyblock.getSkyblockId());
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        int skyblockId = pState.getValue(SKYBLOCK);
        SkyblockItem item = SkyblockItem.getSkyblockItem(skyblockId);
        ItemStack stack = item.getDefaultInstance();
        List<ItemStack> drops = super.getDrops(pState, pParams);
        drops.add(stack);
        return drops;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        int skyblockId = state.getValue(SKYBLOCK);
        SkyblockItem item = SkyblockItem.getSkyblockItem(skyblockId);
        return item.getDefaultInstance();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SKYBLOCK);
    }
}
