package com.benbenlaw.core.tag;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ResourceNames {

    // Vanilla
    public static final String COAL = "coal";
    public static final String DIAMOND = "diamond";
    public static final String EMERALD = "emerald";
    public static final String LAPIS = "lapis";
    public static final String QUARTZ = "quartz";
    public static final String REDSTONE = "redstone";
    public static final String NETHERITE = "netherite";
    public static final String COPPER = "copper";
    public static final String GOLD = "gold";
    public static final String IRON = "iron";

    // Common
    public static final String TIN = "tin";
    public static final String LEAD = "lead";
    public static final String SILVER = "silver";
    public static final String ZINC = "zinc";
    public static final String ALUMINUM = "aluminum";
    public static final String NICKEL = "nickel";
    public static final String PLATINUM = "platinum";
    public static final String OSMIUM = "osmium";
    public static final String URANIUM = "uranium";
    public static final String IRIDIUM = "iridium";
    public static final String RUBY = "ruby";
    public static final String SAPPHIRE = "sapphire";
    public static final String PERIDOT = "peridot";

    // Alloys
    public static final String BRONZE = "bronze";
    public static final String BRASS = "brass";
    public static final String STEEL = "steel";
    public static final String ELECTRUM = "electrum";
    public static final String INVAR = "invar";
    public static final String CONSTANTAN = "constantan";
    public static final String SIGNALUM = "signalum";
    public static final String LUMIUM = "lumium";
    public static final String ENDERIUM = "enderium";
    public static final String CONDUCTIVE_ALLOY = "conductive_alloy";
    public static final String ENERGETIC_ALLOY = "energetic_alloy";
    public static final String VIBRANT_ALLOY = "vibrant_alloy";
    public static final String PULSATING_ALLOY = "pulsating_alloy";
    public static final String SOULARIUM = "soularium";
    public static final String DARK_STEEL = "dark_steel";
    public static final String END_STEEL = "end_steel";
    public static final String REDSTONE_ALLOY = "redstone_alloy";
    public static final String COPPER_ALLOY = "copper_alloy";

    public static List<String> getAllResourceNames() {
        return Arrays.stream(ResourceNames.class.getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()) && field.getType().equals(String.class))
                .map(field -> {
                    try {
                        return (String) field.get(null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}