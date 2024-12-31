package com.benbenlaw.core.block.colored.util;

import com.benbenlaw.core.item.CoreDataComponents;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public interface IColored {

    default int getColor(int index) {
        return -1;
    }

    default int getColor(int index, ItemStack stack) {
        return this.getColor(index);
    }

    class BlockColors implements BlockColor {
        @Override
        public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
            if (state.getBlock() instanceof IColored) {
                DyeColor dyeColor = null;
                Function<BlockState, DyeColor> colorRetriever = BlockTypeColorFinder.BLOCK_TYPE_COLOR_FINDER.get(state.getBlock().getClass());

                if (colorRetriever != null) {
                    dyeColor = colorRetriever.apply(state);
                }

                // Check for null dye color and retrieve color value from ColorMap
                if (dyeColor != null) {
                    Integer colorValue = ColorMap.getColorValue(dyeColor); // Fetch the color value
                    if (colorValue != null) {
                    //    System.out.println("Block at " + pos + " has dye color: " + dyeColor + " -> " + Integer.toHexString(colorValue));
                        return colorValue; // Return the color value if found
                    } else {
                    //    System.out.println("Color value not found for dye color: " + dyeColor);
                    }
                } else {
                //    System.out.println("Block at " + pos + " has no dye color value set!");
                }
            }
            return tintIndex;
        }
    }





    class ItemColors implements ItemColor {
        @Override
        public int getColor(ItemStack stack, int index) {
            String colorString = stack.get(CoreDataComponents.COLOR);
            if (colorString != null) {
                DyeColor dyeColor = ColorMap.getDyeColor(colorString);
                if (dyeColor != null) {
                    int colorValue = ColorMap.getColorValue(dyeColor);
                    int alpha = 0xFF;
                    return (alpha << 24) | (colorValue & 0xFFFFFF);
                }
            }
            return index;
        }
    }

}
