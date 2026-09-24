package com.noodlegamer76.shadered.item;

import com.noodlegamer76.shadered.block.InitBlocks;
import com.noodlegamer76.shadered.client.renderer.item.SkyblockItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SkyblockItem extends BlockItem implements GeoItem {
    private static final Map<Integer, SkyblockItem> idToSkyblockItem = new HashMap<>();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int skyblockId;

    public SkyblockItem(int skyblockId, Properties pProperties) {
        super(InitBlocks.SKYBLOCK.get(), pProperties);
        this.skyblockId = skyblockId;
        idToSkyblockItem.put(skyblockId, this);
    }

    @Nullable
    public static SkyblockItem getSkyblockItem(int skyblockId) {
        return idToSkyblockItem.get(skyblockId);
    }

    public int getSkyblockId() {
        return skyblockId;
    }

    @Override
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
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
                    this.renderer = new SkyblockItemRenderer();

                return this.renderer;
            }
        });
    }
}
