package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.item.CoreItems;
import com.benbenlaw.core.util.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = Core.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void removeAdditionalShiftTooltip(ItemTooltipEvent event) {
        List<Component> tooltip = event.getToolTip();
        boolean seenShiftTooltip = false;

        Iterator<Component> iterator = tooltip.iterator();
        while (iterator.hasNext()) {
            Component component = iterator.next();
            if (component.getContents() instanceof TranslatableContents translatableContents) {
                String key = translatableContents.getKey();
                if (key.equals("tooltip.bblcore.shift")) {
                    if (seenShiftTooltip) {
                        iterator.remove();
                    } else {
                        seenShiftTooltip = true;
                    }
                }
            }
        }
    }

}
