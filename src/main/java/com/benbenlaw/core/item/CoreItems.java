package com.benbenlaw.core.item;

import com.benbenlaw.core.Core;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CoreItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Core.MOD_ID);

    public static final DeferredHolder<Item, Item> UPGRADE_BASE = ITEMS.registerSimpleItem("upgrade_base");

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
