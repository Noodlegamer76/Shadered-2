package com.noodlegamer76.shadered.datagen;

import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.world.item.InitItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture,
                               CompletableFuture<TagLookup<Block>> tagLookupCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, providerCompletableFuture, tagLookupCompletableFuture, Shadered.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.STAIRS)
                .add(InitItems.SPACE_SKYBLOCK_STAIRS.get())
                .add(InitItems.STORMY_SKYBLOCK_STAIRS.get())
                .add(InitItems.OCEAN_SKYBLOCK_STAIRS.get())
                .add(InitItems.ECLIPSE_SKYBLOCK_STAIRS.get())
                .add(InitItems.IRIDIA_SKYBLOCK_STAIRS.get())
                .add(InitItems.FOREST_SKYBLOCK_STAIRS.get())
                .add(InitItems.LIGHT_SKYBLOCK_STAIRS.get());

        tag(ItemTags.SLABS)
                .add(InitItems.SPACE_SKYBLOCK_SLAB.get())
                .add(InitItems.STORMY_SKYBLOCK_SLAB.get())
                .add(InitItems.OCEAN_SKYBLOCK_SLAB.get())
                .add(InitItems.ECLIPSE_SKYBLOCK_SLAB.get())
                .add(InitItems.IRIDIA_SKYBLOCK_SLAB.get())
                .add(InitItems.FOREST_SKYBLOCK_SLAB.get())
                .add(InitItems.LIGHT_SKYBLOCK_SLAB.get());
    }
}
