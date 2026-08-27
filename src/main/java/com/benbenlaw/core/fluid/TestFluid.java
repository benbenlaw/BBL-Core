package com.benbenlaw.core.fluid;

import com.benbenlaw.core.Core;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import javax.xml.transform.Source;

public class TestFluid {

    /**
    * This is an example of how to register a fluid using the FluidDeferredRegister and FluidRegistryObject system.
     * @apiNote This is not a full example and is only meant to show how to register a fluid, core has 2 built in textures, molten (lava like) and thin (water like),
     * tint can be applied, you can also use custom textures.
     */

    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(Core.MOD_ID);

    //Actual color is 0xFEEF9C add FF to make buckets render correctly 0xFFFEEF9C
    //Buckets require a colorHandler to render there contents correctly
    //using a tint without the FF will cause the bucket to render incorrectly unless you tweak the value inside the color handler
    //Path is for still and flowing texture, if using own textures tint is not needed
    //BlockState and Block model are needed to prevent errors
    //Register fluid in client event inside main class
    //Give fluids a name in lang files and bucket item a name
    //since

    public static final FluidRegistryObject<FluidDeferredRegister.CoreFluidTypes, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem> HEAVY_WATER = FLUIDS.register("heavy_water",
            renderProperties -> renderProperties.texture(Core.identifier("block/liquid"),
                   Core.identifier("block/liquid_flow")).tint(0xFF0D1455).moveLikeLava());

    /**
     * @since 26.1 + fluid now require Fluid Models, below is an example from Casting, that registers the fluids models
     *
     * ```js\
     * @EventBusSubscriber(modid = Casting.MOD_ID, value = Dist.CLIENT)
     * public class FluidModels {
     *
     *     @SubscribeEvent
     *     private static void registerFluidModels(RegisterFluidModelsEvent event) {
     *
     *         for (FluidData data : FluidData.FLUID_DEFINITIONS) {
     *
     *             var fluidObject = CastingFluids.FLUIDS_MAP.get(data.name());
     *
     *             var still = new Material(
     *                     Identifier.fromNamespaceAndPath(Casting.MOD_ID, data.stillTexture())
     *             );
     *
     *             var flowing = new Material(
     *                     Identifier.fromNamespaceAndPath(Casting.MOD_ID, data.flowTexture())
     *             );
     *
     *             FluidModel.Unbaked model = new FluidModel.Unbaked(
     *                     still,
     *                     flowing,
     *                     null,
     *                     state -> data.tint(),
     *                     null
     *             );
     *
     *             event.register(model,
     *                     fluidObject.getStillFluid(),
     *                     fluidObject.getFlowingFluid()
     *             );
     *         }
     *     }
     * }```
     */
}
