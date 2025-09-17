package com.benbenlaw.core.tag;

public enum ResourceMaterial {
    // Vanilla
    COAL("coal"),
    DIAMOND("diamond"),
    EMERALD("emerald"),
    LAPIS("lapis"),
    QUARTZ("quartz"),
    REDSTONE("redstone"),
    NETHERITE("netherite"),
    COPPER("copper"),
    GOLD("gold"),
    IRON("iron"),

    // Common
    TIN("tin"),
    LEAD("lead"),
    SILVER("silver"),
    ZINC("zinc"),
    ALUMINUM("aluminum"),
    NICKEL("nickel"),
    PLATINUM("platinum"),
    OSMIUM("osmium"),
    URANIUM("uranium"),
    IRIDIUM("iridium"),
    RUBY("ruby"),
    SAPPHIRE("sapphire"),
    PERIDOT("peridot"),

    // Alloys
    BRONZE("bronze"),
    BRASS("brass"),
    STEEL("steel"),
    ELECTRUM("electrum"),
    INVAR("invar"),
    CONSTANTAN("constantan"),
    SIGNALUM("signalum"),
    LUMIUM("lumium"),
    ENDERIUM("enderium"),
    CONDUCTIVE_ALLOY("conductive_alloy"),
    ENERGETIC_ALLOY("energetic_alloy"),
    VIBRANT_ALLOY("vibrant_alloy"),
    PULSATING_ALLOY("pulsating_alloy"),
    SOULARIUM("soularium"),
    DARK_STEEL("dark_steel"),
    END_STEEL("end_steel"),
    REDSTONE_ALLOY("redstone_alloy"),
    COPPER_ALLOY("copper_alloy");

    private final String name;

    ResourceMaterial(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTag(MaterialType type) {
        return "c:" + type + "/" + name;
    }

    public String getNuggetTag() {
        return getTag(MaterialType.NUGGETS);
    }

    public String getIngotTag() {
        return getTag(MaterialType.INGOTS);
    }

    public String getStorageBlockTag() {
        return getTag(MaterialType.STORAGE_BLOCKS);
    }

    public String getOreTag() {
        return getTag(MaterialType.ORES);
    }

    public String getRawMaterialTag() {
        return getTag(MaterialType.RAW_MATERIALS);
    }

    public String getRawStorageBlockTag() {
        return getTag(MaterialType.RAW_STORAGE_BLOCKS);
    }

    public String getPlateTag() {
        return getTag(MaterialType.PLATES);
    }

    public String getDustTag() {
        return getTag(MaterialType.DUSTS);
    }

    public String getGearTag() {
        return getTag(MaterialType.GEARS);
    }

    public String getRodTag() {
        return getTag(MaterialType.RODS);
    }

    public String getGemTag() {
        return getTag(MaterialType.GEMS);
    }

    public String getWireTag() {
        return getTag(MaterialType.WIRES);
    }
}
