package com.benbenlaw.core.commands;

import com.benbenlaw.core.Core;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = Core.MOD_ID)
public class CoreCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        DiscordCommand.register(event.getDispatcher());
        ModpackCommand.register(event.getDispatcher());
    }

}
