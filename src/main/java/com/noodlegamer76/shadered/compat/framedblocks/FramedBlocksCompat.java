package com.noodlegamer76.shadered.compat.framedblocks;

import com.noodlegamer76.shadered.world.block.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.common.block.FramedBlock;

public class FramedBlocksCompat {
    public static boolean isFramedBlock(BlockState state) {
        return state.getBlock() instanceof FramedBlock;
    }

    public static int getFramedBlockSkybox(BlockAndTintGetter level, BlockState framedBlock, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof FramedBlockEntity framedBlockEntity)) {
            return -1;
        }

        BlockState skyblock = framedBlockEntity.getCamo().getState();
        if (skyblock.hasProperty(ModBlockStateProperties.SKYBLOCK)) {
            return skyblock.getValue(ModBlockStateProperties.SKYBLOCK);
        }

        return -1;
    }
}
