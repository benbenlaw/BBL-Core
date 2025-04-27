package com.benbenlaw.core.commands;

import com.benbenlaw.core.config.CoreModpackConfig;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;

public class DiscordCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("discord").executes(DiscordCommand::execute));
    }
    private static int execute(CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof Player player){

            if (!CoreModpackConfig.discordURL.get().isEmpty()) {
                player.sendSystemMessage(Component.literal(CoreModpackConfig.discordURL.get())
                        .setStyle(Style.EMPTY.withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, CoreModpackConfig.discordURL.get()))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.bblcore.discord"))))
                        .withStyle(ChatFormatting.BLUE));
            } else {
                player.sendSystemMessage(Component.translatable("chat.bblcore.discord_not_set")
                        .withStyle(ChatFormatting.RED));
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
