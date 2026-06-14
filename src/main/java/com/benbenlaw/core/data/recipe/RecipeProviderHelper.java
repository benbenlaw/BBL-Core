package com.benbenlaw.core.data.recipe;

import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeProviderHelper {

    public static Recipe.CommonInfo simpleCommonInfo() {
        return new Recipe.CommonInfo(true);
    }

    public static CraftingRecipe.CraftingBookInfo simpleCraftingBookInfo() {
        return new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, "");
    }

    public static CraftingRecipe.CraftingBookInfo simpleCraftingBookInfo(String group) {
        return new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, group);
    }

    public static CraftingRecipe.CraftingBookInfo craftingBookInfo(CraftingBookCategory category, String group) {
        return new CraftingRecipe.CraftingBookInfo(category, group);
    }

    public static Recipe.CommonInfo simpleCommonInfo(boolean isHidden) {
        return new Recipe.CommonInfo(isHidden);
    }
}
