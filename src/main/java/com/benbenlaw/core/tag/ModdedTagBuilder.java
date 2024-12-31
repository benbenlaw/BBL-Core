package com.benbenlaw.core.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.Objects;

public class ModdedTagBuilder {

    public static TagKey<Item> createTag(String path) {
        return ItemTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", path)))));
    }

    public static TagKey<Fluid> createFluidTag(String path) {
        return FluidTags.create(Objects.requireNonNull(ResourceLocation.tryParse(
                String.valueOf(ResourceLocation.fromNamespaceAndPath("c", path)))));
    }

}
