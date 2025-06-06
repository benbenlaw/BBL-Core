package com.benbenlaw.core.item;

import com.benbenlaw.core.Core;
import com.nimbusds.jose.util.Resource;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CoreItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Core.MOD_ID);

    public static final DeferredItem<Item> UPGRADE_BASE = ITEMS.register("upgrade_base",
            () -> new Item(new Item.Properties().setId(getKey("upgrade_base"))));

    private static ResourceKey<Item> getKey(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, name));


    }
}
