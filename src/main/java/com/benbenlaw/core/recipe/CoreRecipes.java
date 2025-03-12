package com.benbenlaw.core.recipe;

import com.benbenlaw.core.Core;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CoreRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Core.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Core.MOD_ID);

    //Coloring
    public static final Supplier<RecipeSerializer<ColoringRecipe>> COLORING_SERIALIZER =
            SERIALIZER.register("coloring", () -> new SimpleCraftingRecipeSerializer<>(ColoringRecipe::new));

    public static final Supplier<RecipeType<ColoringRecipe>> COLORING_TYPE =
            TYPES.register("coloring", () -> new RecipeType<ColoringRecipe>() {});

    public static void register(IEventBus eventBus) {
        SERIALIZER.register(eventBus);
        TYPES.register(eventBus);
    }
}
