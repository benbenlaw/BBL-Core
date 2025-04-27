package com.benbenlaw.core.item.colored;

import com.benbenlaw.core.block.colored.util.ColorMap;
import com.benbenlaw.core.block.colored.util.IColored;
import com.benbenlaw.core.block.colored.util.BlockTypeColorFinder;
import com.benbenlaw.core.item.CoreDataComponents;
import com.benbenlaw.core.item.TooltipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class ColoringItem extends Item {

    private final DyeColor color;
    public ColoringItem(Properties properties, DyeColor color) {
        super(properties.component(CoreDataComponents.MASS_SPRAYING, false));
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack itemStack) {

        if (itemStack.isDamageableItem()) {
            ItemStack stackInCraftingTable = itemStack.copy();
            stackInCraftingTable.setDamageValue(stackInCraftingTable.getDamageValue() + 1);

            if (stackInCraftingTable.getDamageValue() >= stackInCraftingTable.getMaxDamage()) {
                return ItemStack.EMPTY;
            }

            return stackInCraftingTable;
        }
        return super.getCraftingRemainingItem(itemStack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        if (Screen.hasShiftDown()) {
            boolean currentMassSpraying = Boolean.TRUE.equals(stack.get(CoreDataComponents.MASS_SPRAYING));
            stack.set(CoreDataComponents.MASS_SPRAYING, !currentMassSpraying);
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        String[] colors = {
                "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
                "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"
        };

        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);
        InteractionHand hand = context.getHand();

        if (!level.isClientSide()) {
            if (state.getBlock() instanceof IColored) {
                DyeColor dyeColor = null;
                Function<BlockState, DyeColor> colorRetriever = BlockTypeColorFinder.BLOCK_TYPE_COLOR_FINDER.get(state.getBlock().getClass());

                if (colorRetriever != null) {
                    dyeColor = colorRetriever.apply(state);
                }

                Block block = state.getBlock();
                Property<DyeColor> colorProperty = (Property<DyeColor>) block.getStateDefinition().getProperty("color");

                if (colorProperty != null && dyeColor != null) {
                    state = state.setValue(colorProperty, color);

                    if (Boolean.TRUE.equals(stack.get(CoreDataComponents.MASS_SPRAYING))) {
                        massConvertBlocks(level, pos, state, player, stack, state);
                    } else {
                        level.setBlock(pos, state, 3);
                    }
                }

                if (stack.isDamageableItem()) {
                    assert player != null;
                    stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
                } else {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }

            // Handle blocks with names containing the color
            for (String colorCheck : colors) {
                if (state.getBlock().getName().toString().contains(colorCheck) && !(state.getBlock() instanceof BaseEntityBlock)) {

                    String block = state.getBlock().toString();
                    String blockString = block.replace("Block{", "").replace("}", "");
                    String newColoredBlock = blockString.replace(colorCheck, color.toString().toLowerCase());

                    ResourceLocation newBlockResourceLocation = ResourceLocation.tryParse(newColoredBlock);

                    if (newBlockResourceLocation != null && BuiltInRegistries.BLOCK.containsKey(newBlockResourceLocation)) {
                        Block newBlock = BuiltInRegistries.BLOCK.get(newBlockResourceLocation);
                        BlockState newState = newBlock.defaultBlockState();
                        for (Property<?> property : state.getProperties()) {
                            if (newState.hasProperty(property)) {
                                newState = copyProperty(state, newState, property);
                            }
                        }

                        // Mass spraying check added here
                        if (Boolean.TRUE.equals(stack.get(CoreDataComponents.MASS_SPRAYING))) {
                            massConvertBlocks(level, pos, state, player, stack, newState);
                        } else {
                            level.setBlock(pos, newState, 3);
                        }

                        if (stack.isDamageableItem()) {
                            assert player != null;
                            stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
                        } else {
                            stack.shrink(1);
                        }
                        return InteractionResult.SUCCESS;

                    } else {
                        System.out.println("Failed to parse resource location: " + newColoredBlock);
                    }
                }
            }
            return InteractionResult.FAIL;
        }
        return InteractionResult.FAIL;
    }


    private void massConvertBlocks(Level level, BlockPos startPos, BlockState targetState, Player player, ItemStack itemStack, BlockState newBlockState) {
        Queue<BlockPos> toVisit = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();
        toVisit.add(startPos);

        // Counter for blocks converted
        int blocksConverted = 0;

        // We will play the sound once after conversion is done
        while (!toVisit.isEmpty() && itemStack.getDamageValue() < itemStack.getMaxDamage()) {
            BlockPos currentPos = toVisit.poll();
            if (visited.contains(currentPos)) {
                continue;
            }

            BlockState currentState = level.getBlockState(currentPos);
            if (currentState.getBlock() == targetState.getBlock()) {
                visited.add(currentPos);
                convertBlocks(level, currentPos, currentState, player, itemStack, newBlockState);
                blocksConverted++;
                for (Direction direction : Direction.values()) {
                    BlockPos adjacentPos = currentPos.relative(direction);
                    if (!visited.contains(adjacentPos)) {
                        toVisit.add(adjacentPos);
                    }
                }
            }
        }

        if (blocksConverted > 0) {
            level.playSound(null, startPos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }

    private void convertBlocks(Level level, BlockPos pos, BlockState blockState, Player player, ItemStack itemStack, BlockState newBlockState) {
        // Copy properties from the old state to the new one
        for (Property<?> property : blockState.getProperties()) {
            if (newBlockState.hasProperty(property)) {
                newBlockState = copyProperty(blockState, newBlockState, property);
            }
        }

        // Now set the block to the new state, which should include any copied properties
        level.setBlock(pos, newBlockState, 3);

        if (itemStack.isDamageableItem()) {
            itemStack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(itemStack));
        } else {
            itemStack.shrink(1);
        }
    }


    private <T extends Comparable<T>> BlockState copyProperty(BlockState oldState, BlockState newState, Property<T> property) {
        return newState.setValue(property, oldState.getValue(property));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> list, TooltipFlag flag) {

        boolean massSpraying = Boolean.TRUE.equals(stack.get(CoreDataComponents.MASS_SPRAYING));
        Component massSprayingComponent = Component.literal("");
        if (massSpraying) {
            massSprayingComponent = Component.translatable("tooltips.bblcore.coloring_item.mass_spraying");
        }
        String massSprayingComponentTranslated = massSprayingComponent.getString();

        TooltipUtil.addShiftTooltip(list, "tooltips.bblcore.coloring_item.shift_down");

        Component colorComponent = Component.translatable(ColorMap.getTranslationKey(color.toString()));
        String nameBuilder = colorComponent.getString();

        TextColor textColor = (color == DyeColor.BLACK)
                ? TextColor.fromRgb(0x3C3C3C)
                : TextColor.fromRgb(color.getTextColor());

        list.add(Component.literal("Color: " + nameBuilder + massSprayingComponentTranslated).withStyle(style -> style.withColor(textColor)));

    }

    @Override
    public @NotNull Component getName(ItemStack stack) {

        String nameBuilder = super.getName(stack).getString();

        TextColor textColor;

        if (color != DyeColor.BLACK) {
            textColor = TextColor.fromRgb(ColorMap.getColorValue(color));
        } else {
            textColor = TextColor.fromRgb(0x3C3C3C);
        }

        return Component.literal(nameBuilder)
                .withStyle(style -> style.withColor(textColor));
    }
}