package com.benbenlaw.core.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CommonTags {

    //Item Tags
    public static TagKey<Item> getItemTag(ResourceType type, String resource) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", type + "_" + resource));
    }

    public static TagKey<Block> getBlockTag(String type, String resource) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", type + "_" + resource));
    }

    //Block Tags
    public static TagKey<Block> getBlockTag(ResourceType type, String resource) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", type + "_" + resource));
    }

    public static TagKey<Item> getItemTag(String type, String resource) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", type + "_" + resource));
    }




}
