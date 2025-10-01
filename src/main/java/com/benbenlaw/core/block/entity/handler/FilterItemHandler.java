package com.benbenlaw.core.block.entity.handler;

import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class FilterItemHandler extends ItemStackHandler {

    private boolean whitelist = true;

    public FilterItemHandler(int size) {
        super(size);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return super.isItemValid(slot, stack);
    }

    public void setWhitelist(boolean whitelist) {
        this.whitelist = whitelist;
    }

    public boolean isWhitelist() {
        return whitelist;
    }


    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    public boolean allows(BlockState blockState) {
        boolean hasAnyFilter = false;

        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack stack = this.getStackInSlot(i);
            if (!stack.isEmpty()) {
                hasAnyFilter = true;

                if (stack.getItem() instanceof BlockItem blockItem) {
                    if (blockItem.getBlock() == blockState.getBlock()) {
                        return whitelist; // whitelist=true => allow, false => deny
                    }
                }
            }
        }

        // If filter empty, allow everything
        return !hasAnyFilter || !whitelist;
    }

    public boolean allows(FluidState fluidState) {
        boolean hasAnyFilter = false;

        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack stack = this.getStackInSlot(i);
            if (!stack.isEmpty()) {
                hasAnyFilter = true;

                if (stack.getItem() instanceof IFluidHandlerItem fluidHandlerItem) {
                    if (!fluidHandlerItem.getFluidInTank(0).isEmpty()) {
                        FluidState filterFluid = fluidHandlerItem.getFluidInTank(0).getFluid().defaultFluidState();
                        if (filterFluid.getType() == fluidState.getType()) {
                            return whitelist;
                        }
                    }
                }
            }
        }

        // If filter empty, allow everything
        return !hasAnyFilter || !whitelist;
    }

    public boolean allows(ItemStack stack) {
        boolean hasAnyFilter = false;

        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack filterStack = this.getStackInSlot(i);
            if (!filterStack.isEmpty()) {
                hasAnyFilter = true;

                if (ItemStack.isSameItemSameComponents(stack, filterStack)) {
                    return whitelist;
                }
            }
        }

        return !hasAnyFilter || !whitelist;
    }

    public void serialize(ValueOutput output) {
        output.putBoolean("Whitelist", whitelist);
        ValueOutput.TypedOutputList<ItemStackWithSlot> itemList = output.list("FilteredItems", ItemStackWithSlot.CODEC);

        for(int i = 0; i < this.stacks.size(); ++i) {
            ItemStack stack = this.stacks.get(i);
            if (!stack.isEmpty()) {
                itemList.add(new ItemStackWithSlot(i, stack));
            }
        }
        output.putInt("Size", this.stacks.size());
    }

    public void deserialize(ValueInput input) {
        this.whitelist = input.getBooleanOr("Whitelist", true);
        this.setSize(input.getIntOr("Size", this.stacks.size()));
        input.listOrEmpty("FilteredItems", ItemStackWithSlot.CODEC).forEach((slot) -> {
            if (slot.isValidInContainer(this.stacks.size())) {
                this.stacks.set(slot.slot(), slot.stack());
            }
        });
    }
}
