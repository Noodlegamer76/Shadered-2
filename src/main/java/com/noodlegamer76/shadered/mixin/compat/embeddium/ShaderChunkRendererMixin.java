package com.noodlegamer76.shadered.mixin.compat.embeddium;

import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.compat.embeddium.TerrainRenderPassAddition;
import me.jellysquid.mods.sodium.client.gl.shader.*;
import me.jellysquid.mods.sodium.client.render.chunk.ShaderChunkRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderBindingPoints;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = ShaderChunkRenderer.class, remap = false)
public abstract class ShaderChunkRendererMixin {

    @Shadow
    @Final
    private Map<ChunkShaderOptions, GlProgram<ChunkShaderInterface>> programs;

    @Shadow
    protected abstract GlProgram<ChunkShaderInterface> createShader(String path, ChunkShaderOptions options);

    @Inject(
            method = "compileProgram",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    protected void shadered$compileSkyblockProgram(ChunkShaderOptions options, CallbackInfoReturnable<GlProgram<ChunkShaderInterface>> cir) {
        GlProgram<ChunkShaderInterface> program = programs.get(options);

        if (program == null && options.pass() == TerrainRenderPassAddition.SKYBLOCK) {
            programs.put(options, program = shadered$createSkyblockShader("core/skyblock_sodium", options));

            cir.setReturnValue(program);
        }
    }

    @Unique
    private GlProgram<ChunkShaderInterface> shadered$createSkyblockShader(String path, ChunkShaderOptions options) {
        ShaderConstants constants = options.constants();

        GlShader vertShader = ShaderLoader.loadShader(ShaderType.VERTEX,
                ResourceLocation.fromNamespaceAndPath(Shadered.MODID, path + ".vsh"), constants);

        GlShader fragShader = ShaderLoader.loadShader(ShaderType.FRAGMENT,
                ResourceLocation.fromNamespaceAndPath(Shadered.MODID, path + ".fsh"), constants);

        try {
            return GlProgram.builder(ResourceLocation.fromNamespaceAndPath(Shadered.MODID, "skyblock_chunk_shader"))
                    .attachShader(vertShader)
                    .attachShader(fragShader)
                    .bindAttribute("a_PosId", ChunkShaderBindingPoints.ATTRIBUTE_POSITION_ID)
                    .bindAttribute("a_Color", ChunkShaderBindingPoints.ATTRIBUTE_COLOR)
                    .bindAttribute("a_TexCoord", ChunkShaderBindingPoints.ATTRIBUTE_BLOCK_TEXTURE)
                    .bindAttribute("a_LightCoord", ChunkShaderBindingPoints.ATTRIBUTE_LIGHT_TEXTURE)
                    .bindFragmentData("fragColor", ChunkShaderBindingPoints.FRAG_COLOR)
                    .link((shader) -> new ChunkShaderInterface(shader, options));
        } finally {
            vertShader.delete();
            fragShader.delete();
        }
    }
}
