package com.noodlegamer76.shadered.world.item;

import com.noodlegamer76.shadered.world.block.InitBlocks;
import com.noodlegamer76.shadered.client.renderer.item.SkyblockItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SkyblockItem extends BlockItem implements GeoItem {
    private static final Map<Integer, Map<SkyblockItemType, SkyblockItem>> idToSkyblockItem = new HashMap<>();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int skyblockId;
    private final SkyblockItemType type;

    public SkyblockItem(int skyblockId, SkyblockItemType type, Block block, Properties pProperties) {
        super(block, pProperties);
        this.skyblockId = skyblockId;
        this.type = type;

        idToSkyblockItem.putIfAbsent(skyblockId, new HashMap<>());
        idToSkyblockItem.get(skyblockId).put(type, this);
    }

    @Nullable
    public static SkyblockItem getSkyblockItem(int skyblockId, SkyblockItemType type) {
        return idToSkyblockItem.getOrDefault(skyblockId, new HashMap<>()).get(type);
    }

    public int getSkyblockId() {
        return skyblockId;
    }

    @Override
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public SkyblockItemType getType() {
        return type;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SkyblockItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new SkyblockItemRenderer(type);

                return this.renderer;
            }
        });
    }
}
