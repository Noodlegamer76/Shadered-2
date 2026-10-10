package com.noodlegamer76.shadered.client.util;

public class LightPackingUtils {
    public static byte packLight(int blockLight, int skyLight) {
        int b = Math.max(0, Math.min(15, blockLight));
        int s = Math.max(0, Math.min(15, skyLight));
        
        return (byte) ((s << 4) | b);
    }

    public static int unpackBlockLight(byte packedLight) {
        return packedLight & 0x0F;
    }

    public static int unpackSkyLight(byte packedLight) {
        return (packedLight >> 4) & 0x0F;
    }
}