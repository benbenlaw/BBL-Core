package com.benbenlaw.core.item;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.block.TestBlock;
import com.benbenlaw.core.block.colored.ColoredBlock;
import com.benbenlaw.core.item.colored.ColoredBlockItem;
import com.benbenlaw.core.item.colored.ColoredItem;
import com.benbenlaw.core.item.colored.ColoringItem;
import com.benbenlaw.core.item.colored.LightingItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CoreItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Core.MOD_ID);

    public static final DeferredItem<Item> UPGRADE_BASE = ITEMS.register("upgrade_base",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
