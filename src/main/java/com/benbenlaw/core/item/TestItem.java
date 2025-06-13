package com.benbenlaw.core.item;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.item.colored.ColoringItem;
import com.benbenlaw.core.item.colored.LightingItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TestItem {


    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Core.MOD_ID);

    public static final DeferredItem<Item> LIGHT_ITEM = ITEMS.registerItem(
            "light_item",
            LightItem::new,
            new Item.Properties().durability(10)
    );

    /*
        public static final DeferredItem<Item> COLORED_BLOCK = ITEMS.register("colored_block",
                () -> new ColoredBlockItem((ColoredBlock) TestBlock.COLORED_BLOCK.get(), new Item.Properties()));
        public static final DeferredItem<Item> COLORED_BLOCK_STAIRS = ITEMS.register("colored_block_stairs",
                () -> new ColoredBlockItem((ColoredStairs) TestBlock.COLORED_BLOCK_STAIRS.get(), new Item.Properties()));
        public static final DeferredItem<Item> BRICKS = ITEMS.register("bricks",
                () -> new ColoredBlockItem((ColoredBlock) TestBlock.BRICKS.get(), new Item.Properties()));


     */

    //public static final DeferredItem<Item> PLANKS = ITEMS.register("planks",
    //        () -> new ColoredBlockItem((ColoredBlock) TestBlock.PLANKS.get(), new Item.Properties()));
    //       // () -> new ColoredItem(new Item.Properties()));
//
    //public static final DeferredItem<Item> TEST = ITEMS.register("test",
    //          () -> new ColoredItem(new Item.Properties()));




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
