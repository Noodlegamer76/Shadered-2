package com.noodlegamer76.shadered.event;

import com.mojang.blaze3d.shaders.Shader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.client.util.ModVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Shadered.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterShaders {
    public static ShaderInstance skyblock;
    public static ShaderInstance pbr;

    private static final Map<String, ShaderInstance> SHADERS = new HashMap<>();

    public static ShaderInstance get(String name) {
        return SHADERS.get(name);
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(Shadered.MODID, "skyblock"),
                        DefaultVertexFormat.BLOCK),
                (e) -> {
                    skyblock = e;
                    SHADERS.put("skyblock", e);
                });

        event.registerShader(new ShaderInstance(event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(Shadered.MODID, "pbr"),
                        ModVertexFormat.PBR),
                (e) -> {
                    pbr = e;
                    SHADERS.put("pbr", e);
                });
    }

    public static ShaderInstance getSkyblock() {
        return skyblock;
    }

    public static ShaderInstance getPbr() {
        return pbr;
    }
}
