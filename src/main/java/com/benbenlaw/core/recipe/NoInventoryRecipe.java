package com.benbenlaw.core.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class NoInventoryRecipe implements RecipeInput {
    public static final NoInventoryRecipe INSTANCE = new NoInventoryRecipe();

    @Override
    public ItemStack getItem(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 0;
    }
}

