package com.benbenlaw.core.util;

import com.benbenlaw.core.Core;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class CoreTags {

    ///Extending this class CoreTag allows for the creation of new tags tag is your mod id and name is the name of the tag
    ///CommonTags is used for common tags that are used by multiple mods using the "c" namespace
    public static class Blocks {

        public static TagKey<Block> tag(String modName, String name) {
            return BlockTags.create(Identifier.fromNamespaceAndPath(modName, name));
        }

        public static TagKey<Block> commonTag(String name) {
            return BlockTags.create(Identifier.fromNamespaceAndPath("c", name));
        }


    }
    public static class Items {

        public static TagKey<Item> tag(String modName, String name) {
            return ItemTags.create(Identifier.fromNamespaceAndPath(modName, name));
        }

        public static TagKey<Item> commonTag(String name) {
            return ItemTags.create(Identifier.fromNamespaceAndPath("c", name));
        }
    }

    public static class Fluids {

        public static TagKey<Fluid> tag(String modName, String name) {
            return FluidTags.create(Identifier.fromNamespaceAndPath(modName, name));
        }

        public static TagKey<Fluid> commonTag(String name) {
            return FluidTags.create(Identifier.fromNamespaceAndPath("c", name));
        }
    }
}
