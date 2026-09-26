package com.noodlegamer76.shadered.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.noodlegamer76.shadered.Shadered;
import com.noodlegamer76.shadered.client.util.ModRenderTypes;
import com.noodlegamer76.shadered.world.item.SkyblockItem;
import com.noodlegamer76.shadered.world.item.SkyblockItemType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SkyblockItemRenderer extends GeoItemRenderer<SkyblockItem> {
    public SkyblockItemRenderer(SkyblockItemType type) {
        super(getItemModel(type));
    }

    public static DefaultedItemGeoModel<SkyblockItem> getItemModel(SkyblockItemType type) {
        String path = switch (type) {
            case BLOCK -> "skyblock";
            case STAIRS -> "skyblock_stairs";
            case SLAB -> "skyblock_slab";
            default -> throw new IllegalArgumentException("Unsupported SkyblockItemType: " + type + ". This shouldn't happen.");
        };

        return new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(Shadered.MODID, path));
    }

    @Override
    public void defaultRender(PoseStack poseStack, SkyblockItem animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        int skyblock = -1;
        if (getCurrentItemStack().getItem() instanceof SkyblockItem skyblockItem) {
            skyblock = skyblockItem.getSkyblockId();
        }
        int packedSkyblock = LightTexture.pack(skyblock, 0);
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedSkyblock);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, SkyblockItem animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(SkyblockItem animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return ModRenderTypes.SKYBLOCK;
    }
}
