package com.benbenlaw.core.item.colored;

import com.benbenlaw.core.item.CoreDataComponents;
import net.minecraft.world.item.Item;

public class ColoredItemProperties extends Item.Properties {
    public ColoredItemProperties() {
        this.component(CoreDataComponents.COLOR.get(), "white")
                .component(CoreDataComponents.LIT.get(), false);
    }
}
