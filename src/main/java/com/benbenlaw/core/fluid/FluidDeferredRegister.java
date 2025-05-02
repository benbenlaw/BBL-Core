package com.benbenlaw.core.fluid;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.util.RenderUtil;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class FluidDeferredRegister {

    //FROM Mekanism//
    //https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/registration/impl/FluidDeferredRegister.java//
    //Under MIT License//

    private static final ResourceLocation OVERLAY = ResourceLocation.withDefaultNamespace("block/water_overlay");
    private static final ResourceLocation RENDER_OVERLAY = ResourceLocation.withDefaultNamespace("textures/misc/underwater.png");
    private static final ResourceLocation LIQUID = ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "block/liquid");
    private static final ResourceLocation LIQUID_FLOW = ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "block/liquid_flow");
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

    public static FluidType.Properties getBaseBuilder() {
        return FluidType.Properties.create().sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY);
    }

    public FluidDeferredRegister(String modid) {
        this.blockRegister = DeferredRegister.create(Registries.BLOCK, modid);
        this.fluidRegister = DeferredRegister.create(Registries.FLUID, modid);
        this.fluidTypeRegister = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, modid);
        this.itemRegister = DeferredRegister.create(Registries.ITEM, modid);
    }

    public FluidRegistryObject<CoreFluidTypes, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem> register(String name, UnaryOperator<FluidTypeRenderProperties> renderProperties) {
        return this.register(name, UnaryOperator.identity(), renderProperties);
    }

    public FluidRegistryObject<CoreFluidTypes, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem> register(String name, UnaryOperator<FluidType.Properties> properties, UnaryOperator<FluidTypeRenderProperties> renderProperties) {
        return this.register(name, BucketItem::new, properties, renderProperties);
    }

    public <BUCKET extends BucketItem> FluidRegistryObject<CoreFluidTypes, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BUCKET> register(String name, BucketCreator<BUCKET> bucketCreator, UnaryOperator<FluidType.Properties> fluidProperties, UnaryOperator<FluidTypeRenderProperties> renderProperties) {
        return this.register(name, (FluidType.Properties) fluidProperties.apply(getBaseBuilder()), (FluidTypeRenderProperties) renderProperties.apply(FluidTypeRenderProperties.builder()), bucketCreator, CoreFluidTypes::new);
    }

    public <BUCKET extends BucketItem> FluidRegistryObject<CoreFluidTypes, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BUCKET> register(String name, FluidType.Properties properties, FluidTypeRenderProperties renderProperties, BucketCreator<BUCKET> bucketCreator, BiFunction<FluidType.Properties, FluidTypeRenderProperties, CoreFluidTypes> fluidTypeCreator) {
        DeferredHolder<FluidType, CoreFluidTypes> fluidType = this.fluidTypeRegister.register(name, (rl) -> {
            properties.descriptionId(Util.makeDescriptionId("block", rl));
            return fluidTypeCreator.apply(properties, renderProperties);
        });
        ResourceLocation baseKey = ResourceLocation.fromNamespaceAndPath(this.fluidRegister.getNamespace(), name);
        BaseFlowingFluid.Properties fluidProperties = (new BaseFlowingFluid.Properties(fluidType, DeferredHolder.create(Registries.FLUID, baseKey), DeferredHolder.create(Registries.FLUID, baseKey.withPrefix("flowing_")))).bucket(DeferredHolder.create(Registries.ITEM, baseKey.withSuffix("_bucket"))).block(DeferredHolder.create(Registries.BLOCK, baseKey));
        DeferredHolder<Fluid, BaseFlowingFluid.Source> stillFluid = this.fluidRegister.register(name, () -> {
            return new BaseFlowingFluid.Source(fluidProperties);
        });
        DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowingFluid = this.fluidRegister.register("flowing_" + name, () -> {
            return new BaseFlowingFluid.Flowing(fluidProperties);
        });
        DeferredHolder<Item, BUCKET> bucket = this.itemRegister.register(name + "_bucket", () -> {
            return bucketCreator.create((Fluid) stillFluid.get(), (new Item.Properties()).stacksTo(1).craftRemainder(Items.BUCKET));
        });
        MapColor color = getClosestColor(renderProperties.color);
        DeferredHolder<Block, LiquidBlock> block = this.blockRegister.register(name, () -> {
            return new LiquidBlock((FlowingFluid) stillFluid.get(), net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().noCollission().strength(100.0F).noLootTable().replaceable().pushReaction(PushReaction.DESTROY).liquid().mapColor(color));
        });
        return new FluidRegistryObject(fluidType, stillFluid, flowingFluid, bucket, block);
    }

    static MapColor[] NONE = List.of(MapColor.NONE).toArray(new MapColor[0]);

    private static MapColor getClosestColor(int tint) {
        if (tint == -1) {
            return MapColor.NONE;
        } else {
            int red = FastColor.ARGB32.red(tint);
            int green = FastColor.ARGB32.green(tint);
            int blue = FastColor.ARGB32.blue(tint);
            MapColor color = MapColor.NONE;
            double minDistance = Double.MAX_VALUE;
            MapColor[] var7 = NONE;
            int var8 = var7.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                MapColor toTest = var7[var9];
                if (toTest != null && toTest != MapColor.NONE) {
                    int testRed = FastColor.ARGB32.red(toTest.col);
                    int testGreen = FastColor.ARGB32.green(toTest.col);
                    int testBlue = FastColor.ARGB32.blue(toTest.col);
                    double distanceSquare = perceptualColorDistanceSquared(red, green, blue, testRed, testGreen, testBlue);
                    if (distanceSquare < minDistance) {
                        minDistance = distanceSquare;
                        color = toTest;
                    }
                }
            }

            return color;
        }
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

    public Collection<DeferredHolder<FluidType, ? extends FluidType>> getFluidTypeEntries() {
        return this.fluidTypeRegister.getEntries();
    }

    public Collection<DeferredHolder<Fluid, ? extends Fluid>> getFluidEntries() {
        return this.fluidRegister.getEntries();
    }

    public Collection<DeferredHolder<Block, ? extends Block>> getBlockEntries() {
        return this.blockRegister.getEntries();
    }

    public Collection<DeferredHolder<Item, ? extends Item>> getBucketEntries() {
        return this.itemRegister.getEntries();
    }

    public void registerBucketDispenserBehavior() {
        Iterator var1 = this.getBucketEntries().iterator();

        while (var1.hasNext()) {
            Holder<Item> bucket = (Holder) var1.next();
            DispenserBlock.registerBehavior(bucket.value(), BUCKET_DISPENSE_BEHAVIOR);
        }

    }

    @FunctionalInterface
    public interface BucketCreator<BUCKET extends BucketItem> {
        BUCKET create(Fluid fluid, Item.Properties builder);
    }

    public static class FluidTypeRenderProperties {
        private ResourceLocation stillTexture = LIQUID;
        private ResourceLocation flowingTexture = LIQUID_FLOW;
        //For now all our fluids use the same "overlay" for being against glass as vanilla water.
        private ResourceLocation overlayTexture = OVERLAY;
        private ResourceLocation renderOverlayTexture = RENDER_OVERLAY;
        private int color;

        private FluidTypeRenderProperties() {
        }

        public static FluidTypeRenderProperties builder() {
            return new FluidTypeRenderProperties();
        }

        public FluidTypeRenderProperties texture(ResourceLocation still, ResourceLocation flowing) {
            this.stillTexture = still;
            this.flowingTexture = flowing;
            return this;
        }

        public FluidTypeRenderProperties texture(ResourceLocation still, ResourceLocation flowing, ResourceLocation overlay) {
            this.stillTexture = still;
            this.flowingTexture = flowing;
            this.overlayTexture = overlay;
            return this;
        }

        public FluidTypeRenderProperties renderOverlay(ResourceLocation renderOverlay) {
            this.renderOverlayTexture = renderOverlay;
            return this;
        }

        public FluidTypeRenderProperties tint(int color) {
            this.color = color;
            return this;
        }
    }

    public static class CoreFluidTypes extends FluidType {
        public final ResourceLocation stillTexture;
        public final ResourceLocation flowingTexture;
        public final ResourceLocation overlayTexture;
        public final ResourceLocation renderOverlayTexture;
        public final int color;

        public CoreFluidTypes(Properties properties, FluidTypeRenderProperties renderProperties) {
            super(properties);
            this.stillTexture = renderProperties.stillTexture;
            this.flowingTexture = renderProperties.flowingTexture;
            this.overlayTexture = renderProperties.overlayTexture;
            this.renderOverlayTexture = renderProperties.renderOverlayTexture;
            this.color = renderProperties.color;
        }

        public boolean isVaporizedOnPlacement(Level level, BlockPos pos, FluidStack stack) {
            return false;
        }

        @SuppressWarnings({"initializeClient"})
        public IClientFluidTypeExtensions getClientExtensions() {
            return new IClientFluidTypeExtensions() {


                public @NotNull ResourceLocation getStillTexture() {
                    return CoreFluidTypes.this.stillTexture;
                }

                public @NotNull ResourceLocation getFlowingTexture() {
                    return CoreFluidTypes.this.flowingTexture;
                }

                public ResourceLocation getOverlayTexture() {
                    return CoreFluidTypes.this.overlayTexture;
                }

                public @Nullable ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                    return CoreFluidTypes.this.renderOverlayTexture;
                }

                public @NotNull Vector3f modifyFogColor(@NotNull Camera camera, float partialTick,
                                                        @NotNull ClientLevel level, int renderDistance, float darkenWorldAmount,
                                                        @NotNull Vector3f fluidFogColor) {
                    return new Vector3f(RenderUtil.getRed(CoreFluidTypes.this.color), RenderUtil.getGreen(CoreFluidTypes.this.color), RenderUtil.getBlue(CoreFluidTypes.this.color));
                }

                public void modifyFogRender(@NotNull Camera camera, @NotNull FogRenderer.@NotNull FogMode mode,
                                            float renderDistance, float partialTick, float nearDistance, float farDistance,
                                            @NotNull FogShape shape) {
                    farDistance = 24.0F;
                    if (farDistance > renderDistance) {
                        farDistance = renderDistance;
                        shape = FogShape.CYLINDER;
                    }

                    RenderSystem.setShaderFogStart(-8.0F);
                    RenderSystem.setShaderFogEnd(farDistance);
                    RenderSystem.setShaderFogShape(shape);
                }

                public int getTintColor() {
                    return color;
                }
            };
        }
    }
}
