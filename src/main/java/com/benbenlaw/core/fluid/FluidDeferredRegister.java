package com.benbenlaw.core.fluid;

import com.benbenlaw.core.Core;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.util.Util;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class FluidDeferredRegister {

    private static final Identifier OVERLAY = Identifier.withDefaultNamespace("block/water_overlay");
    private static final Identifier RENDER_OVERLAY = Identifier.withDefaultNamespace("textures/misc/underwater.png");
    private static final Identifier LIQUID = Core.identifier("block/liquid");
    private static final Identifier LIQUID_FLOW = Core.identifier("block/liquid_flow");

    private static final DispenseItemBehavior BUCKET_DISPENSE_BEHAVIOR = new DefaultDispenseItemBehavior() {
        public @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
            Level world = source.level();
            DispensibleContainerItem bucket = (DispensibleContainerItem) stack.getItem();
            BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
            if (bucket.emptyContents(null, world, pos, null, stack)) {
                bucket.checkExtraContent(null, world, stack, pos);
                return new ItemStack(Items.BUCKET);
            } else {
                return super.execute(source, stack);
            }
        }
    };

    private final DeferredRegister<FluidType> fluidTypeRegister;
    private final DeferredRegister<Fluid> fluidRegister;
    private final DeferredRegister<Block> blockRegister;
    private final DeferredRegister<Item> itemRegister;
    private final String modid;

    public FluidDeferredRegister(String modid) {
        this.modid = modid;
        this.blockRegister = DeferredRegister.create(Registries.BLOCK, modid);
        this.fluidRegister = DeferredRegister.create(Registries.FLUID, modid);
        this.fluidTypeRegister = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, modid);
        this.itemRegister = DeferredRegister.create(Registries.ITEM, modid);
    }

    public static FluidType.Properties getBaseBuilder() {
        return FluidType.Properties.create()
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY);
    }

    // Main Registration Logic
    public <BUCKET extends BucketItem> FluidRegistryObject<CoreFluidTypes, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BUCKET> register(
            String name, FluidType.Properties properties, FluidTypeRenderProperties renderProperties,
            BucketCreator<BUCKET> bucketCreator, BiFunction<FluidType.Properties, FluidTypeRenderProperties, CoreFluidTypes> fluidTypeCreator) {

        // 1. Fluid Type
        DeferredHolder<FluidType, CoreFluidTypes> fluidType = this.fluidTypeRegister.register(name, (rl) -> {
            properties.descriptionId(Util.makeDescriptionId("block", rl));
            return fluidTypeCreator.apply(properties, renderProperties);
        });

        // 2. Pre-create Fluid Holders (These don't cause the "ID not set" error if in same register)
        Identifier baseKey = Identifier.fromNamespaceAndPath(modid, name);
        DeferredHolder<Fluid, BaseFlowingFluid.Source> stillHolder = DeferredHolder.create(Registries.FLUID, baseKey);
        DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowingHolder = DeferredHolder.create(Registries.FLUID, baseKey.withPrefix("flowing_"));

        // 3. Register Block and Bucket (Capturing the REAL Holders)
        DeferredHolder<Block, LiquidBlock> block = this.blockRegister.register(name, () ->
                new LiquidBlock((FlowingFluid) stillHolder.get(),
                        net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()
                                .noCollision().strength(100.0F).noLootTable().replaceable()
                                .pushReaction(PushReaction.DESTROY).liquid()
                                .mapColor(getClosestColor(renderProperties.color))
                                .setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(modid, name)))));

        DeferredHolder<Item, BUCKET> bucket = this.itemRegister.register(name + "_bucket", () ->
                bucketCreator.create(stillHolder.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)
                        .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(modid,  name + "_bucket")))));

        BaseFlowingFluid.Properties fluidProperties = new BaseFlowingFluid.Properties(fluidType, stillHolder, flowingHolder)
                .bucket(bucket)
                .block(block);

        DeferredHolder<Fluid, BaseFlowingFluid.Source> stillFluid = this.fluidRegister.register(name, () -> new BaseFlowingFluid.Source(fluidProperties));
        DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowingFluid = this.fluidRegister.register("flowing_" + name, () -> new BaseFlowingFluid.Flowing(fluidProperties));

        return new FluidRegistryObject<>(fluidType, stillFluid, flowingFluid, bucket, block);
    }

    public FluidRegistryObject<CoreFluidTypes, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem> register(String name, UnaryOperator<FluidTypeRenderProperties> renderProperties) {
        return this.register(name, UnaryOperator.identity(), renderProperties);
    }

    public FluidRegistryObject<CoreFluidTypes, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem> register(String name, UnaryOperator<FluidType.Properties> properties, UnaryOperator<FluidTypeRenderProperties> renderProperties) {
        return this.register(name, BucketItem::new, properties, renderProperties);
    }

    public <BUCKET extends BucketItem> FluidRegistryObject<CoreFluidTypes, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BUCKET> register(String name, BucketCreator<BUCKET> bucketCreator, UnaryOperator<FluidType.Properties> fluidProperties, UnaryOperator<FluidTypeRenderProperties> renderProperties) {
        return this.register(name, fluidProperties.apply(getBaseBuilder()), renderProperties.apply(FluidTypeRenderProperties.builder()), bucketCreator, CoreFluidTypes::new);
    }

    // Color Logic
    private static MapColor[] NONE = new MapColor[]{MapColor.NONE};

    private static MapColor getClosestColor(int tint) {
        if (tint == -1) return MapColor.NONE;
        int red = ARGB.red(tint);
        int green = ARGB.green(tint);
        int blue = ARGB.blue(tint);
        MapColor color = MapColor.NONE;
        double minDistance = Double.MAX_VALUE;

        for (MapColor toTest : NONE) {
            if (toTest != null && toTest != MapColor.NONE) {
                int testRed = ARGB.red(toTest.col);
                int testGreen = ARGB.green(toTest.col);
                int testBlue = ARGB.blue(toTest.col);
                double distanceSquare = perceptualColorDistanceSquared(red, green, blue, testRed, testGreen, testBlue);
                if (distanceSquare < minDistance) {
                    minDistance = distanceSquare;
                    color = toTest;
                }
            }
        }
        return color;
    }

    private static double perceptualColorDistanceSquared(int red1, int green1, int blue1, int red2, int green2, int blue2) {
        int redMean = red1 + red2 >> 1;
        int r = red1 - red2;
        int g = green1 - green2;
        int b = blue1 - blue2;
        return (double) (((512 + redMean) * r * r >> 8) + 4 * g * g + ((767 - redMean) * b * b >> 8));
    }

    public void register(IEventBus bus) {
        this.blockRegister.register(bus);
        this.fluidRegister.register(bus);
        this.fluidTypeRegister.register(bus);
        this.itemRegister.register(bus);
    }

    // Registry Accessors
    public Collection<DeferredHolder<FluidType, ? extends FluidType>> getFluidTypeEntries() { return this.fluidTypeRegister.getEntries(); }
    public Collection<DeferredHolder<Fluid, ? extends Fluid>> getFluidEntries() { return this.fluidRegister.getEntries(); }
    public Collection<DeferredHolder<Block, ? extends Block>> getBlockEntries() { return this.blockRegister.getEntries(); }
    public Collection<DeferredHolder<Item, ? extends Item>> getBucketEntries() { return this.itemRegister.getEntries(); }

    public void registerBucketDispenserBehavior() {
        for (Holder<Item> bucket : this.getBucketEntries()) {
            DispenserBlock.registerBehavior(bucket.value(), BUCKET_DISPENSE_BEHAVIOR);
        }
    }

    // Helper Classes
    public interface BucketCreator<BUCKET extends BucketItem> {
        BUCKET create(Fluid fluid, Item.Properties builder);
    }

    public static class FluidTypeRenderProperties {
        private Identifier stillTexture = LIQUID;
        private Identifier flowingTexture = LIQUID_FLOW;
        private Identifier overlayTexture = OVERLAY;
        private Identifier renderOverlayTexture = RENDER_OVERLAY;
        private int color;

        private FluidTypeRenderProperties() {}
        public static FluidTypeRenderProperties builder() { return new FluidTypeRenderProperties(); }
        public FluidTypeRenderProperties texture(Identifier still, Identifier flowing) { this.stillTexture = still; this.flowingTexture = flowing; return this; }
        public FluidTypeRenderProperties tint(int color) { this.color = color; return this; }
    }

    public static class CoreFluidTypes extends FluidType {
        public final Identifier stillTexture;
        public final Identifier flowingTexture;
        public final Identifier overlayTexture;
        public final int color;

        public CoreFluidTypes(Properties properties, FluidTypeRenderProperties renderProperties) {
            super(properties);
            this.stillTexture = renderProperties.stillTexture;
            this.flowingTexture = renderProperties.flowingTexture;
            this.overlayTexture = renderProperties.overlayTexture;
            this.color = renderProperties.color;
        }
    }
}