package com.benbenlaw.core.item.colored;

import com.benbenlaw.core.block.brightable.IBrightable;
import com.benbenlaw.core.block.brightable.util.IColored;
import com.benbenlaw.core.item.TooltipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class LightingItem extends Item {

    public LightingItem(Properties properties) {
        super(properties);
    }


    @Override
    public ItemStack getCraftingRemainder(ItemStack itemStack) {
        if (itemStack.isDamageableItem()) {
            ItemStack stackInCraftingTable = itemStack.copy();
            stackInCraftingTable.setDamageValue(stackInCraftingTable.getDamageValue() + 1);

            if (stackInCraftingTable.getDamageValue() >= stackInCraftingTable.getMaxDamage()) {
                return ItemStack.EMPTY;
            }

            return stackInCraftingTable;
        }
        return super.getCraftingRemainder(itemStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof IBrightable) {
            Property<Boolean> litProperty = (Property<Boolean>) state.getBlock().getStateDefinition().getProperty("lit");

            if (litProperty != null) {
                boolean isLit = state.getValue(litProperty);
                if (isLit) {
                    level.setBlock(pos, state.setValue(litProperty, false), 3);
                } else {
                    level.setBlock(pos, state.setValue(litProperty, true), 3);
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


        if (state.getBlock() instanceof IColored) {

            Block block = state.getBlock();
            Property<DyeColor> colorProperty = (Property<DyeColor>) block.getStateDefinition().getProperty("color");


            if (stack.isDamageableItem()) {
                assert player != null;
                stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
            } else {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        TooltipUtil.addShiftTooltip(tooltipDisplay, tooltipAdder, Component.translatable("tooltips.lighting_item.shift.held"));
        tooltipAdder.accept(Component.literal("Ability: Lights Blocks").withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {

        StringBuilder nameBuilder = new StringBuilder(super.getName(stack).getString());
        return Component.literal(nameBuilder.toString()).withStyle(ChatFormatting.YELLOW);
    }
}