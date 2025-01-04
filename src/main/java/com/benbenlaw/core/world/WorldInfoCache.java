package com.benbenlaw.core.world;

public class WorldInfoCache {
    private static String worldType;

    public static void setWorldType(String type) {
        worldType = type;
    }

    public static String getWorldType() {
        return worldType;
    }
}