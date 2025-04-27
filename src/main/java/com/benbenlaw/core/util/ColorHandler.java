package com.benbenlaw.core.util;

import com.benbenlaw.core.block.TestBlock;
import com.benbenlaw.core.block.colored.util.IColored;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public final class ColorHandler {

    @SubscribeEvent
    public void registerBlockColors(final RegisterColorHandlersEvent.Block event) {
        //event.register(new IColored.BlockColors(), TestBlock.COLORED_BLOCK.get());
    }


    @SubscribeEvent
    public void onItemColors(RegisterColorHandlersEvent.Item event) {
        //event.register(new IColored.ItemColors(), TestBlock.COLORED_BLOCK.get().asItem());
    }
}
