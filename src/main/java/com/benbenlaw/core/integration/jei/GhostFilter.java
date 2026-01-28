package com.benbenlaw.core.integration.jei;

import com.benbenlaw.core.network.packets.UpdateFilterFluidSlotsPacket;
import com.benbenlaw.core.network.packets.UpdateFilterSlotsPacket;
import com.benbenlaw.core.screen.util.slot.FilterFluidSlot;
import com.benbenlaw.core.screen.util.slot.FilterSlot;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GhostFilter<T extends AbstractContainerScreen<?>> implements IGhostIngredientHandler<T> {

    /// Allows JEI to drag and drop items/fluids into FilterSlots and FilterFluidSlots in the GUI

    @Override
    public <I> List<Target<I>> getTargetsTyped(T gui, ITypedIngredient<I> ingredient, boolean doStart) {
        List<Target<I>> targets = new ArrayList<>();

        for (Slot slot : gui.getMenu().slots) {
            Rect2i bounds = new Rect2i(gui.getGuiLeft() + slot.x, gui.getGuiTop() + slot.y, 16, 16);

            if (ingredient.getIngredient() instanceof ItemStack && (slot instanceof FilterSlot)) {
                targets.add(new Target<I>() {
                    @Override
                    public Rect2i getArea() {
                        return bounds;
                    }

                    @Override
                    public void accept(I ingredient) {
                        slot.set((ItemStack) ingredient);
                        ClientPacketDistributor.sendToServer(new UpdateFilterSlotsPacket(slot.index, (ItemStack) ingredient));
                    }
                });
            }

            if (ingredient.getIngredient() instanceof FluidStack && (slot instanceof FilterFluidSlot filterFluidSlot)) {
                targets.add(new Target<I>() {
                    @Override
                    public Rect2i getArea() {
                        return bounds;
                    }

                    @Override
                    public void accept(I ingredient) {
                        filterFluidSlot.set((FluidStack) ingredient);
                        ClientPacketDistributor.sendToServer(new UpdateFilterFluidSlotsPacket(slot.index, (FluidStack) ingredient));
                    }
                });
            }
        }
        return targets;
    }

    @Override
    public void onComplete() {

    }
}
