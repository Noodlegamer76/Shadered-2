package com.noodlegamer76.shadered.client.renderer.complexpass.passes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.shadered.client.renderer.assimp.AssimpRenderer;
import com.noodlegamer76.shadered.client.renderer.complexpass.PassType;
import com.noodlegamer76.shadered.client.renderer.complexpass.RenderStage;
import com.noodlegamer76.shadered.client.renderer.complexpass.RenderableComplexPass;

public class AssimpRendererComplexPass implements RenderableComplexPass {
    @Override
    public PassType getType() {
        return PassType.GEOMETRY;
    }

    @Override
    public void render(RenderStage stage, PoseStack poseStack, int renderTick, float partialTick) {
        AssimpRenderer renderer = AssimpRenderer.getInstance();
        renderer.render(partialTick, renderTick);
    }
}
