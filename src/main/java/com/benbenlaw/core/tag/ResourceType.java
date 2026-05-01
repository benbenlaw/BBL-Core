package com.benbenlaw.core.tag;

public enum ResourceType {
    NUGGETS("nuggets"),
    INGOTS("ingots"),
    STORAGE_BLOCKS("storage_blocks"),
    ORES("ores"),
    RAW_MATERIALS("raw_materials"),
    RAW_STORAGE_BLOCKS("storage_blocks/raw_"),
    PLATES("plates"),
    DUSTS("dusts"),
    GEARS("gears"),
    RODS("rods"),
    GEMS("gems"),
    WIRES("wires"),
    SHARDS("shards");

    private final String path;

    ResourceType(String path) {
        this.path = path;
    }
}