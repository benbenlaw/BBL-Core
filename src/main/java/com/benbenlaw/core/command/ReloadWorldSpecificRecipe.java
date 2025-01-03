package com.benbenlaw.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.common.Internal;
import mezz.jei.core.util.LoggedTimer;
import mezz.jei.gui.ingredients.IListElementInfo;
import mezz.jei.gui.ingredients.IngredientFilter;
import mezz.jei.gui.overlay.IngredientListOverlay;
import mezz.jei.gui.startup.JeiEventHandlers;
import mezz.jei.gui.startup.ResourceReloadHandler;
import mezz.jei.library.runtime.JeiRuntime;
import mezz.jei.library.startup.JeiStarter;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.CalculateDetachedCameraDistanceEvent;
import net.neoforged.neoforge.internal.BrandingControl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ReloadWorldSpecificRecipe {

    public ReloadWorldSpecificRecipe(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("reload_world_recipes").executes(this::execute));

    }

    private int execute(CommandContext<CommandSourceStack> context) {
        MinecraftServer server = context.getSource().getServer();

        // Access the recipe manager
        RecipeManager recipeManager = server.getRecipeManager();

        // Reload recipes manually
        reloadRecipes(recipeManager, server);

        // Notify the command sender about the reload
        context.getSource().sendSuccess(() -> Component.literal("Recipes reloaded successfully!"), true);

        return 1;  // Success code
    }

    private void reloadRecipes(RecipeManager recipeManager, MinecraftServer server) {
        // Access the resource manager
        ResourceManager resourceManager = server.getResourceManager();
        ProfilerFiller preparationProfiler = server.getProfiler();
        ProfilerFiller reloadProfiler = server.getProfiler();
        Executor backgroundExecutor = Executors.newCachedThreadPool();
        PreparableReloadListener.PreparationBarrier barrier = CompletableFuture::completedFuture;
        recipeManager.reload(barrier, resourceManager, preparationProfiler, reloadProfiler, backgroundExecutor, server);
        if (ModList.get().isLoaded("jei")) {
            reloadJEI();
        }

    }


    public void reloadJEI() {

        //WIP

        if (ModList.get().isLoaded("jei")) {
            Minecraft.getInstance().execute(() -> {
                IJeiRuntime jeiRuntime = Internal.getJeiRuntime();
                IRecipeManager recipeManager = jeiRuntime.getRecipeManager();

                // Clear existing recipes (optional based on your use case)
                recipeManager.getRecipeCategory(RecipeTypes.CRAFTING);

                // Add new recipes (empty list in this example)
                recipeManager.addRecipes(RecipeTypes.CRAFTING, List.of());

                // Trigger the refresh or update for JEI (important)

            });
        }
    }


}
