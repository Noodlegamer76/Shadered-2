package com.noodlegamer76.shadered.mixin.compat.embeddium;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.GlStateManager;
import com.noodlegamer76.shadered.client.renderer.complexpass.passes.SkyboxRenderPass;
import com.noodlegamer76.shadered.client.renderer.skybox.SkyboxRenderer;
import com.noodlegamer76.shadered.compat.embeddium.TerrainRenderPassAddition;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniform;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformInt;
import me.jellysquid.mods.sodium.client.render.chunk.shader.*;
import org.lwjgl.opengl.GL32C;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

@Mixin(value = ChunkShaderInterface.class, remap = false)
public class ChunkShaderInterfaceMixin {
    @Unique
    private boolean shadered$isSkyblockShader;

    @Unique
    private GlUniformInt[] shadered$skyboxUniforms;

    @Mutable
    @Shadow
    @Final
    private ChunkShaderFogComponent fogShader;

    @Inject(
            method = "<init>",
            at = @At(value = "TAIL")
    )
    public void shadered$setupShader(ShaderBindingContext context, ChunkShaderOptions options, CallbackInfo ci) {
        shadered$isSkyblockShader =
                options.pass() == TerrainRenderPassAddition.SKYBLOCK;

        if (!shadered$isSkyblockShader) {
            return;
        }

        shadered$skyboxUniforms = new GlUniformInt[11];

        for (int i = 0; i < 11; i++) {
            shadered$skyboxUniforms[i] = context.bindUniform("skybox" + i, GlUniformInt::new);
        }
    }

    @Inject(
            method = "setupState",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void shadered$skyblockStateSetup(CallbackInfo ci) {
        if (shadered$isSkyblockShader) {
            SkyboxRenderer skyboxRenderer = SkyboxRenderer.getInstance();
            List<SkyboxRenderPass> skyboxes = skyboxRenderer.getSkyboxes();
            for (int i = 0; i < skyboxes.size(); i++) {
                int textureId = skyboxes.get(i).getSkyboxTarget().getColorTextureId();

                shadered$bindTexture(i, textureId);
            }

            fogShader.setup();
            ci.cancel();
        }
    }

    @Unique
    private void shadered$bindTexture(int textureSlot, int textureId) {
        GlStateManager._activeTexture(GL32C.GL_TEXTURE0 + textureSlot);
        GlStateManager._bindTexture(textureId);

        shadered$skyboxUniforms[textureSlot].setInt(textureSlot);
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/render/chunk/shader/ShaderBindingContext;bindUniform(Ljava/lang/String;Ljava/util/function/IntFunction;)Lme/jellysquid/mods/sodium/client/gl/shader/uniform/GlUniform;",
                    ordinal = 3
            )
    )
    private GlUniform shadered$redirectFourthBindUniform(
            ShaderBindingContext context,
            String name,
            IntFunction<?> factory,
            @Local(argsOnly = true) ChunkShaderOptions options
    ) {
        if (options.pass() == TerrainRenderPassAddition.SKYBLOCK) {
            return null;
        }

        return context.bindUniform(name, (IntFunction) factory);
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/render/chunk/shader/ShaderBindingContext;bindUniform(Ljava/lang/String;Ljava/util/function/IntFunction;)Lme/jellysquid/mods/sodium/client/gl/shader/uniform/GlUniform;",
                    ordinal = 4
            )
    )
    private GlUniform shadered$redirectFifthBindUniform(
            ShaderBindingContext context,
            String name,
            IntFunction<?> factory,
            @Local(argsOnly = true) ChunkShaderOptions options
    ) {
        if (options.pass() == TerrainRenderPassAddition.SKYBLOCK) {
            return null;
        }

        return context.bindUniform(name, (IntFunction) factory);
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 0
            )
    )
    private Object shadered$changeFirstMapPut(
            Map<ChunkShaderTextureSlot, GlUniformInt> map,
            Object key,
            Object value,
            @Local(argsOnly = true) ChunkShaderOptions options
    ) {
        if (options.pass() == TerrainRenderPassAddition.SKYBLOCK) {
            return null;
        }

        return map.put((ChunkShaderTextureSlot) key, (GlUniformInt) value);
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 1
            )
    )
    private Object shadered$changeSecondMapPut(
            Map<ChunkShaderTextureSlot, GlUniformInt> map,
            Object key,
            Object value,
            @Local(argsOnly = true) ChunkShaderOptions options
    ) {
        if (options.pass() == TerrainRenderPassAddition.SKYBLOCK) {
            return null;
        }

        return map.put((ChunkShaderTextureSlot) key, (GlUniformInt) value);
    }
}
