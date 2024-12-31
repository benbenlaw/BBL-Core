package com.benbenlaw.core.fluid;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public interface IFluidProvider {
    Fluid getFluid();

    default FluidStack getFluidStack(int size) {
        return new FluidStack(this.getFluid(), size);
    }

    default ResourceLocation getRegistryName() {
        return BuiltInRegistries.FLUID.getKey(this.getFluid());
    }

    default Component getTextComponent() {
        return this.getFluid().getFluidType().getDescription(this.getFluidStack(1));
    }

    default String getTranslationKey() {
        return this.getFluid().getFluidType().getDescriptionId();
    }
}