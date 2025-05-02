package com.benbenlaw.core.fluid;

import com.benbenlaw.core.Core;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;

public class TestFluid {

    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(Core.MOD_ID);

    //Actual color is 0xFEEF9C add FF to make buckets render correctly 0xFFFEEF9C
    //Buckets require a colorHandler to render there contents correctly
    //using a tint without the FF will cause the bucket to render incorrectly unless you tweak the value inside the color handler
    //Path is for still and flowing texture, if using own textures tint is not needed
    //BlockState and Block model are needed to prevent errors
    //Register fluid in client event inside main class
    //Give fluids a name in lang files and bucket item a name

    public static final FluidRegistryObject<FluidDeferredRegister.CoreFluidTypes, BaseFlowingFluid.Source,
            BaseFlowingFluid.Flowing, LiquidBlock, BucketItem> LIME_WATER;
    public static final FluidRegistryObject<FluidDeferredRegister.CoreFluidTypes, BaseFlowingFluid.Source,
            BaseFlowingFluid.Flowing, LiquidBlock, BucketItem> PINK_WATER;

    static {


        LIME_WATER = FLUIDS.register("eroding_water", (renderProperties) ->
                renderProperties.texture(ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "block/water_still"),
                        ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "block/water_flow")).tint(0xFFCCCCFF));

        PINK_WATER = FLUIDS.register("pink_water", (renderProperties) ->
                renderProperties.texture(ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "block/molten_still"),
                        ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "block/molten_flow")).tint(0xFFf72cff));




    }
}
