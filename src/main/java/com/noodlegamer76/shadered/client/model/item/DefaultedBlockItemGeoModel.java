package com.noodlegamer76.shadered.client.model.item;

import com.noodlegamer76.shadered.world.item.SkyblockItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.MolangQueries;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

/**
 * Implemented to avoid crashing on the Superflat screen
 */
public class DefaultedBlockItemGeoModel<T extends GeoAnimatable> extends DefaultedItemGeoModel<T> {

    /**
     * Create a new instance of this model class.<br>
     * The asset path should be the truncated relative path from the base folder.<br>
     * E.G.
     * <pre>{@code
     * 	new ResourceLocation("myMod", "armor/obsidian")
     * }</pre>
     *
     * @param assetSubpath
     */
    public DefaultedBlockItemGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public void applyMolangQueries(T animatable, double animTime) {
        MolangParser parser = MolangParser.INSTANCE;
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;

        parser.setMemoizedValue(MolangQueries.LIFE_TIME, () -> animTime / 20d);
        if (level == null) {
            parser.setMemoizedValue(MolangQueries.ACTOR_COUNT, () -> 0);
            parser.setMemoizedValue(MolangQueries.TIME_OF_DAY, () -> 0);
            parser.setMemoizedValue(MolangQueries.MOON_PHASE, () -> 0);
        }
        else {
            parser.setMemoizedValue(MolangQueries.ACTOR_COUNT, level::getEntityCount);
            parser.setMemoizedValue(MolangQueries.TIME_OF_DAY, () -> level.getDayTime() / 24000f);
            parser.setMemoizedValue(MolangQueries.MOON_PHASE, level::getMoonPhase);
        }
    }
}
