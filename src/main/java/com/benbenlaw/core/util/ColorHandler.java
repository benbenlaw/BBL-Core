package com.benbenlaw.core.util;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public final class ColorHandler {

    @SubscribeEvent
    public void registerBlockColors(final RegisterColorHandlersEvent.Block event) {
        //event.register(new IColored.BlockColors(), TestBlock.COLORED_BLOCK.get());
    }
}
