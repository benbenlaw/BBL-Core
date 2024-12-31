package com.benbenlaw.core.item.colored;

import com.benbenlaw.core.block.colored.util.ColorMap;
import com.benbenlaw.core.item.CoreDataComponents;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ColoredItem extends Item{

    public ColoredItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {

        if (stack.get(CoreDataComponents.COLOR) != null) {

            String colorString = stack.get(CoreDataComponents.COLOR);
            assert colorString != null;
            DyeColor color = ColorMap.getDyeColor(colorString);
            Component colorComponent = Component.translatable(ColorMap.getTranslationKey(colorString));
            Component litComponent = Component.translatable("color.bblcore.lit");

            StringBuilder nameBuilder = new StringBuilder(super.getName(stack).getString())
                    .append(" - ")
                    .append(colorComponent.getString());

            if (Boolean.TRUE.equals(stack.get(CoreDataComponents.LIT))) {
                nameBuilder.append(" - ").append(litComponent.getString());
            }

            TextColor textColor;

            if (color != DyeColor.BLACK) {
                textColor = TextColor.fromRgb(ColorMap.getColorValue(color));
            } else {
                textColor = TextColor.fromRgb(0x3C3C3C);
            }

            return Component.literal(nameBuilder.toString())
                    .withStyle(style -> style.withColor(textColor));

        }

        return Component.literal(super.getName(stack).getString());
    }

}