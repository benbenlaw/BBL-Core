package com.benbenlaw.core.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.Objects;

public class ModdedTagBuilder {

    //NeoFabric Item Tag using the "c" namespace
    public static TagKey<Item> createNeoFabricItemTag(String path) {
        return ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", path)))));
    }

    //NeoFabric Block Tag using the "c" namespace
    public static TagKey<Block> createNeoFabricBlockTag(String path) {
        return BlockTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", path)))));
    }

    //NeoFabric Fluid Tag using the "c" namespace
    public static TagKey<Fluid> createNeoFabricFluidTag(String path) {
        return FluidTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", path)))));
    }

    //Item Tag using the mod id
    public static TagKey<Item> createItemTag(String modName, String path) {
        return ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath(modName, path)))));
    }

    //Item Tag using the mod id
    public static TagKey<Block> createBlockTag(String modName, String path) {
        return BlockTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath(modName, path)))));
    }

    //Fluid Tag using the mod id
    public static TagKey<Fluid> createFluidTag(String path) {
        return FluidTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", path)))));
    }

}
