package com.benbenlaw.core.command;

import com.benbenlaw.core.config.ModpackConfig;
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

import java.net.URI;
import java.net.URISyntaxException;


public class DiscordCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("discord").executes(DiscordCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> command) {
        if (command.getSource().getEntity() instanceof Player player) {
            String urlString = ModpackConfig.discordURL.get();

            if (urlString == null || urlString.isBlank()) {
                player.sendSystemMessage(Component.translatable("chat.bblcore.discord_not_set")
                        .withStyle(ChatFormatting.RED));
                return Command.SINGLE_SUCCESS;
            }

            if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
                player.sendSystemMessage(Component.translatable("chat.bblcore.discord_invalid_url")
                        .withStyle(ChatFormatting.RED));
                return Command.SINGLE_SUCCESS;
            }

            try {
                URI uri = new URI(urlString);

                player.sendSystemMessage(Component.literal(urlString)
                        .setStyle(Style.EMPTY
                                .withUnderlined(true)
                                .withColor(ChatFormatting.BLUE)
                                .withClickEvent(new ClickEvent.OpenUrl(uri))
                                .withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.bblcore.discord")))
                        )
                );

            } catch (URISyntaxException e) {
                player.sendSystemMessage(Component.translatable("chat.bblcore.discord_invalid_url")
                        .withStyle(ChatFormatting.RED));
            }
        }
        return Command.SINGLE_SUCCESS;
    }


}