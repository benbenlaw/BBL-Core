package com.benbenlaw.core.tag;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.*;

public class CommonTags {
    // Provide common tags for resources


    // Add more resource types as needed
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
        WIRES("wires");
        private final String path;

        ResourceType(String path) {
            this.path = path;
        }
    }

    private static final Map<String, Map<ResourceType, TagKey<Item>>> TAGS = new HashMap<>();

    // Resource for tags
    public static void init() {
        List<String> resources = getResourceNames();

        // Register all resource tags
        for (String resource : resources) {
            registerResourceTags(resource);
            registerRawStorageBlockTags(resource);
        }
    }

    private static List<String> getResourceNames() {
        return Arrays.stream(ResourceNames.class.getFields())
                .filter(field -> field.getType() == String.class)
                .map(field -> {
                    try {
                        return (String) field.get(null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Registers tags for a given resource.
     *
     * @param resource The resource name.
     */
    private static void registerResourceTags(String resource) {
        Map<ResourceType, TagKey<Item>> tags = new EnumMap<>(ResourceType.class);
        for (ResourceType type : ResourceType.values()) {
            tags.put(type, ModdedTagBuilder.createNeoFabricItemTag(
                    String.format("%s/%s", type.path, resource)));
        }
        TAGS.put(resource, tags);
    }
    /**
     * Registers raw storage block tags for a given resource.
     *
     * @param resource The resource name.
     */

    private static void registerRawStorageBlockTags(String resource) {
        Map<ResourceType, TagKey<Item>> tags = TAGS.get(resource);
        if (tags != null) {
            tags.put(ResourceType.RAW_STORAGE_BLOCKS, ModdedTagBuilder.createNeoFabricItemTag(
                    String.format("storage_blocks/raw_%s", resource)));
        }
    }
    /**
     * Gets the tag for a given resource and type.
     *
     * @param resource The resource name.
     * @param type     The resource type.
     * @return The tag for the given resource and type.
     */
    public static TagKey<Item> getTag(String resource, ResourceType type) {
        return TAGS.get(resource).get(type);
    }

}
