package com.noodlegamer76.shadered.mixin.compat.framedblocks;

import com.llamalad7.mixinextras.sugar.Local;
import com.noodlegamer76.shadered.world.block.InitBlocks;
import com.noodlegamer76.shadered.world.block.ModBlockStateProperties;
import com.noodlegamer76.shadered.world.item.SkyblockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xfacthd.framedblocks.api.block.FramedBlockEntity;

@Mixin(value = FramedBlockEntity.class, remap = false)
public class FramedBlockEntityMixin {

    @Redirect(
            method = "setBlockCamo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    public BlockState shadered$getSkyblockState(Block instance, @Local(argsOnly = true) ItemStack stack) {
        BlockState skyblock = ((BlockItem)stack.getItem()).getBlock().defaultBlockState();
        if (!(stack.getItem() instanceof SkyblockItem skyblockItem)) {
            return skyblock;
        }

        skyblock = skyblock.setValue(ModBlockStateProperties.SKYBLOCK, skyblockItem.getSkyblockId());
        return skyblock;
    }
}
