package com.benbenlaw.core.data.recipe;

import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeProviderHelper {

    public Recipe.CommonInfo simpleCommonInfo() {
        return new Recipe.CommonInfo(true);
    }

    public CraftingRecipe.CraftingBookInfo simpleCraftingBookInfo() {
        return new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, "");
    }

    public CraftingRecipe.CraftingBookInfo simpleCraftingBookInfo(String group) {
        return new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, group);
    }

    public CraftingRecipe.CraftingBookInfo craftingBookInfo(CraftingBookCategory category, String group) {
        return new CraftingRecipe.CraftingBookInfo(category, group);
    }

    public Recipe.CommonInfo simpleCommonInfo(boolean isHidden) {
        return new Recipe.CommonInfo(isHidden);
    }
}
