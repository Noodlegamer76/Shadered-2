package com.noodlegamer76.shadered.client.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.noodlegamer76.shadered.client.renderer.skybox.SkyboxRenderer;
import com.noodlegamer76.shadered.event.RegisterShaders;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class ModRenderTypes {
    protected static final RenderStateShard.LightmapStateShard LIGHTMAP = new RenderStateShard.LightmapStateShard(true);

    public static final RenderType SKYBLOCK = RenderType.create(
            "skyblock",
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            256,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.EmptyTextureStateShard(
                            () -> {
                                SkyboxRenderer.getInstance().bindSkyblockTextures();
                            },
                            () -> {

                            }
                    ))
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> {
                        return RegisterShaders.skyblock;
                    }))
                    .createCompositeState(true)
    );
}
