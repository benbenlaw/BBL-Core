package com.benbenlaw.core.data.recipe;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.recipe.ShapedTagOutputRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShapedTagOutputRecipeProvider implements RecipeBuilder {

    protected String group;
    Recipe.CommonInfo commonInfo;
    CraftingRecipe.CraftingBookInfo craftingBookInfo;
    ShapedRecipePattern shapedRecipePattern;
    TagKey<Item> outputTag;
    int count;

    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    public ShapedTagOutputRecipeProvider(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo craftingBookInfo, ShapedRecipePattern shapedRecipePattern, TagKey<Item> outputTag, int count) {
        this.commonInfo = commonInfo;
        this.craftingBookInfo = craftingBookInfo;
        this.shapedRecipePattern = shapedRecipePattern;
        this.outputTag = outputTag;
        this.count = count;
    }

    public static ShapedTagOutputRecipeProvider tagOutputRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo craftingBookInfo, ShapedRecipePattern shapedRecipePattern, TagKey<Item> outputTag, int count) {
        return new ShapedTagOutputRecipeProvider(commonInfo, craftingBookInfo, shapedRecipePattern, outputTag, count);
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
                Core.identifier(outputTag.location().getPath())
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
        ShapedTagOutputRecipe tagOutputRecipe = new ShapedTagOutputRecipe(this.commonInfo, this.craftingBookInfo, this.shapedRecipePattern, this.outputTag, this.count);
        recipeOutput.accept(resourceKey, tagOutputRecipe, builder.build(resourceKey.identifier().withPrefix("recipe/")));
    }



}