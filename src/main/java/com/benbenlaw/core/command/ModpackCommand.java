package com.benbenlaw.core.command;

import com.benbenlaw.core.config.ModpackConfig;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class ModpackCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("modpack").executes(ModpackCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> command) {
        if (command.getSource().getEntity() instanceof Player player) {

            if (!ModpackConfig.modpackName.get().isEmpty() && !ModpackConfig.modpackVersion.get().isEmpty()) {
                player.sendSystemMessage(Component.translatable("chat.bblcore.modpack", ModpackConfig.modpackName.get(), ModpackConfig.modpackVersion.get())
                        .withStyle(ChatFormatting.BLUE));
            } else {
                player.sendSystemMessage(Component.translatable("chat.bblcore.modpack_not_set")
                        .withStyle(ChatFormatting.RED));
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}