package com.benbenlaw.core.commands;

import com.benbenlaw.core.config.CoreModpackConfig;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.net.URI;
import java.net.URISyntaxException;

public class DiscordCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("discord").executes(DiscordCommand::execute));
    }
    private static int execute(CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof Player player){

            ServerPlayer serverPlayer = (ServerPlayer) player;

            try {
                URI uri = new URI(CoreModpackConfig.discordURL.get());
                serverPlayer.sendSystemMessage(Component.literal(CoreModpackConfig.discordURL.get())
                        .setStyle(Style.EMPTY
                                .withUnderlined(true)
                                .withColor(ChatFormatting.BLUE)
                                .withClickEvent(new ClickEvent.OpenUrl(uri))
                                .withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.bblcore.discord")))
                        )
                );
            } catch (URISyntaxException e) {
                serverPlayer.sendSystemMessage(Component.translatable("chat.bblcore.discord_not_set")
                        .withStyle(ChatFormatting.RED));
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
