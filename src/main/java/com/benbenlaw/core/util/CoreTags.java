package com.benbenlaw.core.util;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.tag.ModdedTagBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.Map;

public class CoreTags {

    //Extending this class CoreTag allows for the creation of new tags tag is your mod id and name is the name of the tag
    ///commonTags is used for common tags that are used by multiple mods using the "c" namespace
    public static class Blocks {

        //Nether Portal Valid Tags
        public static final TagKey<Block> NETHER_PORTAL_FRAME = tag(Core.MOD_ID, "nether_portal_frame");
        public static final TagKey<Block> CLIMBABLE_BLOCKS = tag(Core.MOD_ID,"climbable_blocks");
        public static final TagKey<Block> BANNED_FROM_COLORING = tag(Core.MOD_ID,"banned_from_coloring");

        //Color Tags
        public static final Map<String, TagKey<Block>> COLOR_TAGS = new HashMap<>();
        static {
            for (String color : ColorList.COLORS) {
                COLOR_TAGS.put(color, ModdedTagBuilder.createBlockTag(Core.MOD_ID, "tintable/" + color));
            }
        }


        public static TagKey<Block> tag(String modName, String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(modName, name));
        }

        public static TagKey<Block> commonTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }


    }
    public static class Items {
        public static final TagKey<Item> NETHER_PORTAL_FRAME = tag(Core.MOD_ID,"nether_portal_frame");
        public static final TagKey<Item> BANNED_FROM_COLORING = tag(Core.MOD_ID,"banned_from_coloring");


        //Color Tags
        public static final Map<String, TagKey<Item>> COLOR_TAGS = new HashMap<>();
        static {
            for (String color : ColorList.COLORS) {
                COLOR_TAGS.put(color, ModdedTagBuilder.createItemTag(Core.MOD_ID, "tintable/" + color));
            }
        }

        public static TagKey<Item> tag(String modName, String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(modName, name));
        }

        public static TagKey<Item> commonTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }

    public static class Fluids {

        public static TagKey<Fluid> tag(String modName, String name) {
            return FluidTags.create(ResourceLocation.fromNamespaceAndPath(modName, name));
        }

        public static TagKey<Fluid> commonTag(String name) {
            return FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }
}
