package com.benbenlaw.core.commands;

import com.benbenlaw.core.config.CoreModpackConfig;
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

            if (!CoreModpackConfig.modpackName.get().isEmpty() && !CoreModpackConfig.modpackVersion.get().isEmpty()) {
                player.sendSystemMessage(Component.translatable("chat.bblcore.modpack", CoreModpackConfig.modpackName.get(), CoreModpackConfig.modpackVersion.get())
                        .withStyle(ChatFormatting.BLUE));
            } else {
                player.sendSystemMessage(Component.translatable("chat.bblcore.modpack_not_set")
                        .withStyle(ChatFormatting.RED));
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}