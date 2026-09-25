package com.noodlegamer76.shadered.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.client.renderer.complexpass.ComplexPassRenderer;
import com.noodlegamer76.shadered.client.renderer.complexpass.RenderStage;
import com.noodlegamer76.shadered.client.renderer.skybox.SkyboxRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Shadered.MODID, value = Dist.CLIENT)
public class RenderLevelState {

    @SubscribeEvent
    public static void levelRenderEvent(RenderLevelStageEvent event) {
        RenderLevelStageEvent.Stage stage = event.getStage();
        PoseStack poseStack = event.getPoseStack();
        float partialTick = event.getPartialTick();
        int renderTick = event.getRenderTick();

        ComplexPassRenderer renderer = ComplexPassRenderer.getInstance();

        if (stage == RenderLevelStageEvent.Stage.AFTER_SKY) {
            SkyboxRenderer.getInstance().preRender();
            renderer.render(RenderStage.AFTER_SKY, poseStack, renderTick, partialTick);
        }
        else if (stage == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            renderer.render(RenderStage.AFTER_BLOCK_ENTITIES, poseStack, renderTick, partialTick);
        }
        else if (stage == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            renderer.render(RenderStage.AFTER_LEVEL, poseStack, renderTick, partialTick);
        }
    }
}
