package com.benbenlaw.core.recipe;

import com.benbenlaw.core.Core;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CoreRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Core.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Core.MOD_ID);

    //Tag Output
    public static final Supplier<RecipeSerializer<ShapedTagOutputRecipe>> SHAPED_TAG_OUTPUT =
            SERIALIZER.register("shaped_tag_output", () -> ShapedTagOutputRecipe.SERIALIZER);
    public static final Supplier<RecipeType<ShapedTagOutputRecipe>> SHAPED_TAG_OUTPUT_TYPE =
            TYPES.register("shaped_tag_output", () -> ShapedTagOutputRecipe.TYPE);

    //Component Copy
    public static final Supplier<RecipeSerializer<ShapedComponentCopyRecipe>> SHAPED_COMPONENT_COPY =
            SERIALIZER.register("shaped_component_copy", () -> ShapedComponentCopyRecipe.SERIALIZER);
    public static final Supplier<RecipeType<ShapedComponentCopyRecipe>> SHAPED_COMPONENT_COPY_TYPE =
            TYPES.register("shaped_component_copy", () -> ShapedComponentCopyRecipe.TYPE);

}