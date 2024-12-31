package com.benbenlaw.core.util;

import com.benbenlaw.core.Core;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CoreTags {

    //Extending this class CoreTag allows for the creation of new tags tag is your mod id and name is the name of the tag
    ///commonTags is used for common tags that are used by multiple mods using the "c" namespace
    public static class Blocks {

        //Nether Portal Valid Tags
        public static final TagKey<Block> NETHER_PORTAL_FRAME = tag(Core.MOD_ID, "nether_portal_frame");

        public static TagKey<Block> tag(String modName, String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(modName, name));
        }

        public static TagKey<Block> commonTags(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }

    }
    public static class Items {
        public static final TagKey<Item> NETHER_PORTAL_FRAME = tag(Core.MOD_ID,"nether_portal_frame");

        public static TagKey<Item> tag(String modName, String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(modName, name));
        }

        public static TagKey<Item> commonTags(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }

    }

}
