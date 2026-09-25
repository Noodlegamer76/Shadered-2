package com.noodlegamer76.shadered.compat.embeddium;

import com.noodlegamer76.shadered.client.util.ModRenderTypes;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.parameters.AlphaCutoffParameter;
import net.minecraft.client.renderer.RenderType;

public class TerrainRenderPassAddition {
    public static final TerrainRenderPass SKYBLOCK = new TerrainRenderPass(ModRenderTypes.SKYBLOCK, false, false);
    public static final Material SKYBLOCK_MATERIAL = new Material(SKYBLOCK, AlphaCutoffParameter.ZERO, false);

}
