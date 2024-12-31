package com.benbenlaw.core.util;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public final class ColorHandler {

    @SubscribeEvent
    public void registerBlockColors(final RegisterColorHandlersEvent.Block event) {

        //event.register(new IColored.BlockColors(), TestBlock.COLORED_BLOCK.get());
        //event.register(new IColored.BlockColors(), TestBlock.PLANKS.get());
        //event.register(new IColored.BlockColors(), TestBlock.BRICKS.get());

    }


    @SubscribeEvent
    public void onItemColors(RegisterColorHandlersEvent.Item event) {

        //event.register(new IColored.ItemColors(), TestItem.TEST.get().asItem());
        //event.register(new IColored.ItemColors(), TestBlock.COLORED_BLOCK.get().asItem());
        //event.register(new IColored.ItemColors(), TestBlock.PLANKS.get().asItem());
        //event.register(new IColored.ItemColors(), TestBlock.BRICKS.get().asItem());

        /*
        var buckets = new Item[] {
                TestFluid.PINK_WATER.getBucket(),
                TestFluid.LIME_WATER.getBucket(),
        };

        event.register((stack, tint) -> {
            var fluid = ((BucketItem) stack.getItem()).content;
            return tint == 1 ? IClientFluidTypeExtensions.of(fluid).getTintColor() : -1;
        }, buckets);

         */



    }
}
