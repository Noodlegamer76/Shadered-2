package com.noodlegamer76.shadered.compat;

import net.minecraftforge.fml.loading.LoadingModList;

public class ModCompatUtils {
    public static boolean isCreateLoaded() {
        return LoadingModList.get().getModFileById("create") != null;
    }

    public static boolean isEmbeddiumLoaded() {
        return LoadingModList.get().getModFileById("embeddium") != null;
    }

    public static boolean isFramedBlocksLoaded() {
        return LoadingModList.get().getModFileById("framedblocks") != null;
    }
}
