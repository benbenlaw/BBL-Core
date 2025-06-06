package com.benbenlaw.core.block.brightable.util;

import com.benbenlaw.core.util.CoreTags;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

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

            for (String colorTag : CoreTags.Blocks.COLOR_TAGS.keySet()) {
                // Check if the block matches any of the color tags
                if (state.is(CoreTags.Blocks.COLOR_TAGS.get(colorTag))) {
                    // Convert the colorTag string to upper case and try to match it with DyeColor enum
                    try {
                        DyeColor dyeColor = DyeColor.valueOf(colorTag.toUpperCase());
                        // Return the color from ColorMap using the dye color
                        Integer colorValue = ColorMap.getColorValue(dyeColor);

                        if (colorValue != null) {
                            return colorValue; // Return the color value if found in ColorMap
                        } else {
                            // Handle the case where no color value is found in ColorMap
                        }
                    } catch (IllegalArgumentException e) {
                        // Log or handle the case where colorTag doesn't match any DyeColor enum value
                        // This will handle cases like "yellow" or other unrecognized tags gracefully
                    }
                }
            }
            return tintIndex;
        }
    }
}
