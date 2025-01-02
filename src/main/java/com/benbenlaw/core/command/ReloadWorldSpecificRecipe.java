package com.benbenlaw.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Collection;
import java.util.Collections;

public class ReloadWorldSpecificRecipe {

    public ReloadWorldSpecificRecipe(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("reload_world_recipes").executes(this::execute));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        MinecraftServer server = context.getSource().getServer();

        //actually do the reload

        context.getSource().sendSuccess(() -> Component.literal("Recipes reloaded successfully!"), true);

        return 1;  // Success code
    }


}
