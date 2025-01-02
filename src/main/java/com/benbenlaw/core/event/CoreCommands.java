package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.command.ReloadWorldSpecificRecipe;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.server.command.ConfigCommand;

@EventBusSubscriber(modid = Core.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CoreCommands {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        // Register commands here
        new ReloadWorldSpecificRecipe(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }
}
