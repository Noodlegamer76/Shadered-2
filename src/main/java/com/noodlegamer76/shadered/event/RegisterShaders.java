package com.noodlegamer76.shadered.event;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.noodlegamer76.shadered.Shadered;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Shadered.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterShaders {
    public static ShaderInstance skyblock;

    private static final Map<String, ShaderInstance> SHADERS = new HashMap<>();

    public static ShaderInstance get(String name) {
        return SHADERS.get(name);
    }

    @SubscribeEvent
    public static void registerShaders(net.minecraftforge.client.event.RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(Shadered.MODID, "skyblock"),
                        DefaultVertexFormat.BLOCK),
                (e) -> {
                    skyblock = e;
                    SHADERS.put("skyblock", e);
                });
    }

    public static ShaderInstance getSkyblock() {
        return skyblock;
    }
}
