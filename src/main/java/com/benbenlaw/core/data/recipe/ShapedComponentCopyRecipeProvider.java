package com.benbenlaw.core.data.recipe;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.recipe.ShapedComponentCopyRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShapedComponentCopyRecipeProvider implements RecipeBuilder {

    protected String group;
    Recipe.CommonInfo commonInfo;
    CraftingRecipe.CraftingBookInfo craftingBookInfo;
    ShapedRecipePattern shapedRecipePattern;
    ItemStackTemplate result;
    Ingredient source;
    List<DataComponentType<?>> componentsToCopy;

    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public ShapedComponentCopyRecipeProvider(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo craftingBookInfo, ShapedRecipePattern shapedRecipePattern,
            ItemStackTemplate result, Ingredient source, List<DataComponentType<?>> componentsToCopy) {
        this.commonInfo = commonInfo;
        this.craftingBookInfo = craftingBookInfo;
        this.shapedRecipePattern = shapedRecipePattern;
        this.result = result;
        this.source = source;
        this.componentsToCopy = componentsToCopy;
    }

    public static ShapedComponentCopyRecipeProvider componentCopyRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo craftingBookInfo,
            ShapedRecipePattern shapedRecipePattern, ItemStackTemplate result, Ingredient source, List<DataComponentType<?>> componentsToCopy) {
        return new ShapedComponentCopyRecipeProvider(commonInfo, craftingBookInfo, shapedRecipePattern, result, source, componentsToCopy);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return ResourceKey.create(
                Registries.RECIPE,
                Core.identifier(result.item().unwrapKey()
                        .map(k -> k.identifier().getPath())
                        .orElse("component_copy_recipe"))
        );
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull String id) {
        save(recipeOutput, ResourceKey.create(Registries.RECIPE, Core.identifier(id)));
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceKey<Recipe<?>> resourceKey) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);

        ShapedComponentCopyRecipe recipe = new ShapedComponentCopyRecipe(
                this.commonInfo,
                this.craftingBookInfo,
                this.shapedRecipePattern,
                this.result,
                this.source,
                this.componentsToCopy
        );

        recipeOutput.accept(resourceKey, recipe, builder.build(resourceKey.identifier().withPrefix("recipe/")));
    }
}