package com.noodlegamer76.shadered.mixin.compat.framedblocks;

import com.llamalad7.mixinextras.sugar.Local;
import com.noodlegamer76.shadered.world.block.ModBlockStateProperties;
import com.noodlegamer76.shadered.world.item.SkyblockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xfacthd.framedblocks.api.camo.EmptyCamoContainer;
import xfacthd.framedblocks.common.data.camo.BlockCamoContainer;

@Mixin(BlockCamoContainer.Factory.class)
public class BlockCamoContainerMixin {

    @Redirect(
            method = "fromItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    public BlockState shadered$skyblockFromItem(Block block, @Local(argsOnly = true) ItemStack stack) {
        BlockState state = block.defaultBlockState();

        if (stack.getItem() instanceof SkyblockItem skyblockItem && state.hasProperty(ModBlockStateProperties.SKYBLOCK)) {
            int skyblock = skyblockItem.getSkyblockId();
            state = state.setValue(ModBlockStateProperties.SKYBLOCK, skyblock);
        }

        return state;
    }

}
