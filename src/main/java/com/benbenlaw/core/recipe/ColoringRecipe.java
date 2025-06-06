package com.benbenlaw.core.recipe;

import com.benbenlaw.core.item.CoreDataComponents;
import com.benbenlaw.core.item.colored.ColoredBlockItem;
import com.benbenlaw.core.item.colored.ColoringItem;
import com.benbenlaw.core.util.ColorList;
import com.benbenlaw.core.util.CoreTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class ColoringRecipe extends CustomRecipe {
    public ColoringRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {

        ItemStack coloredBlockItem = ItemStack.EMPTY;
        ItemStack sprayCanItem = ItemStack.EMPTY;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ColoredBlockItem && !stack.is(CoreTags.Items.BANNED_FROM_COLORING)) {
                    if (!coloredBlockItem.isEmpty()) {
                        return false;
                    }
                    coloredBlockItem = stack;
                } else if (stack.getItem() instanceof ColoringItem) {
                    if (!sprayCanItem.isEmpty()) {
                        return false;
                    }
                    sprayCanItem = stack;
                } else if (containsColor(stack) && !(stack.getItem() instanceof ColoredBlockItem)) {
                    // Handle other colorable blocks
                    if (!coloredBlockItem.isEmpty()) {
                        return false;
                    }
                    coloredBlockItem = stack;
                } else {
                    return false;
                }
            }
        }

        return !coloredBlockItem.isEmpty() && !sprayCanItem.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        ItemStack coloredBlockItem = ItemStack.EMPTY;
        ColoringItem sprayCanItem = null;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ColoredBlockItem) {
                    if (!coloredBlockItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    coloredBlockItem = stack.copy();
                } else if (stack.getItem() instanceof ColoringItem) {
                    if (sprayCanItem != null) {
                        return ItemStack.EMPTY;
                    }
                    sprayCanItem = (ColoringItem) stack.getItem();
                } else if (containsColor(stack)) {
                    if (!coloredBlockItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    coloredBlockItem = stack.copy();
                }
            }
        }

        if (!coloredBlockItem.isEmpty() && sprayCanItem != null) {
            DyeColor sprayColor = sprayCanItem.getColor();
            ItemStack result = applyColorToBlock(coloredBlockItem, sprayColor);

            if (!result.isEmpty()) {
                result.setCount(1);
                return result;
            }
        }

        return ItemStack.EMPTY;
    }

    private ItemStack applyColorToBlock(ItemStack stack, DyeColor color) {
        if (stack.getItem() instanceof ColoredBlockItem) {
            String colorString = color.getName();
            stack.set(CoreDataComponents.COLOR, colorString);
            return stack;
        }

        for (String colorCheck : ColorList.COLORS) {
            if (stack.getItem().toString().contains(colorCheck) && !(stack.getItem() instanceof ColoredBlockItem)) {
                String resourceLocationString = stack.getItem().toString().replace(colorCheck, color.toString().toLowerCase());
                ResourceLocation resourceLocation;

                try {
                    resourceLocation = ResourceLocation.tryParse(resourceLocationString);
                    if (resourceLocation != null && BuiltInRegistries.ITEM.containsKey(resourceLocation)) {
                        return BuiltInRegistries.ITEM.getValue(resourceLocation).getDefaultInstance();
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Failed to parse resource location: " + resourceLocationString);
                    return ItemStack.EMPTY;
                }
            }
        }
        return ItemStack.EMPTY;
    }
    private boolean containsColor(ItemStack stack) {
        // Check if the stack contains a colorable block (but not a ColoredBlockItem)
        for (String colorCheck : ColorList.COLORS) {
            if (stack.getItem().toString().contains(colorCheck) && !(stack.getItem() instanceof ColoringItem)) {
                return true;
            }
        }
        return false;
    }

    //TODO implement the serializer when recipe works
    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return null;
    //    return CoreRecipes.COLORING_SERIALIZER.get();
    }
}
