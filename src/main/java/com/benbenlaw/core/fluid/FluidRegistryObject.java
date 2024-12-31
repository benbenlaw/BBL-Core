package com.benbenlaw.core.fluid;


import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;

//FROM Mekanism//
//https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/registration/impl/FluidRegistryObject.java//
//Under MIT License//
public class FluidRegistryObject<TYPE extends FluidType, STILL extends Fluid, FLOWING extends Fluid, BLOCK extends LiquidBlock, BUCKET extends BucketItem> implements IFluidProvider {
    private final DeferredHolder<FluidType, TYPE> fluidType;
    private final DeferredHolder<Fluid, STILL> still;
    private final DeferredHolder<Fluid, FLOWING> flowing;
    private final DeferredHolder<Item, BUCKET> bucket;
    private final DeferredHolder<Block, BLOCK> block;

    FluidRegistryObject(DeferredHolder<FluidType, TYPE> fluidType, DeferredHolder<Fluid, STILL> still, DeferredHolder<Fluid, FLOWING> flowing, DeferredHolder<Item, BUCKET> bucket, DeferredHolder<Block, BLOCK> block) {
        this.fluidType = fluidType;
        this.still = still;
        this.flowing = flowing;
        this.bucket = bucket;
        this.block = block;
    }

    public TYPE getFluidType() {
        return this.fluidType.get();
    }

    public STILL getStillFluid() {
        return this.still.get();
    }

    public FLOWING getFlowingFluid() {
        return this.flowing.get();
    }

    public BLOCK getBlock() {
        return this.block.get();
    }

    public BUCKET getBucket() {
        return this.bucket.get();
    }

    public STILL getFluid() {
        return this.getStillFluid();
    }
}
