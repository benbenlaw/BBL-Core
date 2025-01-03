package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.command.ReloadWorldSpecificRecipe;
import com.benbenlaw.core.config.WorldTypeConditionConfig;
import com.benbenlaw.core.recipe.WorldTypeCondition;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.server.command.ConfigCommand;

@EventBusSubscriber(modid = Core.MOD_ID, bus = EventBusSubscriber.Bus.GAME)

public class CoreEvents {
    public static boolean firstJoin = true;

    @SubscribeEvent
    public static void onFirstPlayerJoinWorld(PlayerEvent.PlayerLoggedInEvent event) throws CommandSyntaxException {

        if (WorldTypeConditionConfig.enableRecipeReload.get()) {
            Player player = event.getEntity();
            if (player instanceof ServerPlayer serverPlayer) {
                MinecraftServer server = serverPlayer.getServer();

                if (server != null && firstJoin) {
                    firstJoin = false;
                    String command = "reload_world_recipes";
                    CommandSourceStack sourceStack = server.createCommandSourceStack();
                    server.getCommands().getDispatcher()
                            .execute(server.getCommands().getDispatcher().parse(command, sourceStack));
                }
            }
        }
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
        firstJoin = true;
    }

}
