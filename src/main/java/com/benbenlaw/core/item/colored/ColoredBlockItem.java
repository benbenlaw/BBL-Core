package com.benbenlaw.core.item.colored;

import com.benbenlaw.core.block.colored.util.ColorMap;
import com.benbenlaw.core.block.colored.*;
import com.benbenlaw.core.item.CoreDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class ColoredBlockItem extends BlockItem {

    public ColoredBlockItem(Properties properties) {
        super(Blocks.AIR, properties);
    }

    public ColoredBlockItem(ColoredBlock block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredStairs block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredSlab block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredWall block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredPressurePlate block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredButton block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredFence block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredFenceGate block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredDoor block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredTrapDoor block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredLog block, Properties properties) {
        super(block, properties);
    }

    public ColoredBlockItem(ColoredLeaves block, Properties properties) {
        super(block, properties);
    }

    public ColoredBlockItem(ColoredFlowerPot block, Properties properties) {
        super(block, properties);
    }

    public ColoredBlockItem(ColoredSapling block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredFlower block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredGrassBlock block, Properties properties) {super(block, properties);}
    public ColoredBlockItem(ColoredSnowyDirtBlock block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredSpreadingSnowyDirtBlock block, Properties properties) {
        super(block, properties);
    }
    public ColoredBlockItem(ColoredTallGrassBlock block, Properties properties) {super(block, properties);}
    public ColoredBlockItem(ColoredDoublePlantBlock block, Properties properties) {super(block, properties);}



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
                    .withStyle(style -> style.withColor(textColor.getValue()));

        }
        return Component.literal(super.getName(stack).getString());
    }
}