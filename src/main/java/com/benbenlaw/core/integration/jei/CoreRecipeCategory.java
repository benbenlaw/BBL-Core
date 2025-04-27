package com.benbenlaw.core.integration.jei;

import com.benbenlaw.core.recipe.CoreRecipes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

// THIS IS A WIP CLASS IMPLEMENTING COMMON FIELDS AND METHODS FOR ALL RECIPE CATEGORIES, not tested good luck //
public abstract class CoreRecipeCategory<T extends Recipe<?>> implements IRecipeCategory<T> {

    private final RecipeType<T> type;
    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;

    public CoreRecipeCategory(RecipeType<T> type, IDrawable background, IDrawable icon, Component title) {
        this.type = type;
        this.background = background;
        this.icon = icon;
        this.title = title;
    }

    @Override
    public @NotNull RecipeType<T> getRecipeType() {
        return type;
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull T recipe) {
        assert Minecraft.getInstance().level != null;
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        Optional<RecipeHolder<?>> recipeHolder = recipeManager.getRecipes().stream()
                .filter(holder -> holder.value().equals(recipe))
                .findFirst();

        return recipeHolder.map(RecipeHolder::id).orElse(null);

    }
}
