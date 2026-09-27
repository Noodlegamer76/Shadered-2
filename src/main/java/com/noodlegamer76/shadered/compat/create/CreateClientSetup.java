package com.noodlegamer76.shadered.compat.create;

import com.noodlegamer76.shadered.client.util.ModRenderTypes;
import com.simibubi.create.AllBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CreateClientSetup {
    public static void setupClient(FMLClientSetupEvent event) {
        appendRenderType(AllBlocks.COPYCAT_BASE.get(), ModRenderTypes.SKYBLOCK);
        appendRenderType(AllBlocks.COPYCAT_PANEL.get(), ModRenderTypes.SKYBLOCK);
        appendRenderType(AllBlocks.COPYCAT_STEP.get(), ModRenderTypes.SKYBLOCK);
        appendRenderType(AllBlocks.COPYCAT_BARS.get(), ModRenderTypes.SKYBLOCK);
    }

    private static void appendRenderType(Block block, RenderType renderType) {
        ItemBlockRenderTypes.setRenderLayer(
                block,
                ChunkRenderTypeSet.union(
                        ItemBlockRenderTypes.getRenderLayers(block.defaultBlockState()),
                        ChunkRenderTypeSet.of(renderType)
                )
        );
    }
}
