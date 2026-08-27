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
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.Util;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForgeMod;
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

    //FROM Mekanism//
    //https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/registration/impl/FluidDeferredRegister.java//
    //Under MIT License//

    private static final Identifier OVERLAY = Identifier.withDefaultNamespace("block/water_overlay");
    private static final Identifier RENDER_OVERLAY = Identifier.withDefaultNamespace("textures/misc/underwater.png");
    private static final Identifier LIQUID = Core.identifier( "block/liquid");
    private static final Identifier LIQUID_FLOW = Core.identifier( "block/liquid_flow");
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
        return this.register(name, (FluidType.Properties) fluidProperties.apply(getBaseBuilder()), renderProperties.apply(FluidTypeRenderProperties.builder()), bucketCreator, CoreFluidTypes::new);
    }

    public <BUCKET extends BucketItem> FluidRegistryObject<CoreFluidTypes, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BUCKET> register(String name, FluidType.Properties properties, FluidTypeRenderProperties renderProperties, BucketCreator<BUCKET> bucketCreator, BiFunction<FluidType.Properties, FluidTypeRenderProperties, CoreFluidTypes> fluidTypeCreator) {
        DeferredHolder<FluidType, CoreFluidTypes> fluidType = this.fluidTypeRegister.register(name, (rl) -> {
            properties.descriptionId(Util.makeDescriptionId("block", rl));
            properties.temperature(renderProperties.temperature);
            return fluidTypeCreator.apply(properties, renderProperties);
        });
        Identifier baseKey = Identifier.fromNamespaceAndPath(this.fluidRegister.getNamespace(), name);
        BaseFlowingFluid.Properties fluidProperties = (new BaseFlowingFluid.Properties(fluidType, DeferredHolder.create(Registries.FLUID, baseKey), DeferredHolder.create(Registries.FLUID, baseKey.withPrefix("flowing_")))).bucket(DeferredHolder.create(Registries.ITEM, baseKey.withSuffix("_bucket"))).block(DeferredHolder.create(Registries.BLOCK, baseKey));
        DeferredHolder<Fluid, BaseFlowingFluid.Source> stillFluid = this.fluidRegister.register(name, () -> new BaseFlowingFluid.Source(fluidProperties));
        DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowingFluid = this.fluidRegister.register("flowing_" + name, () -> new BaseFlowingFluid.Flowing(fluidProperties));
        DeferredHolder<Item, BUCKET> bucket = this.itemRegister.register(name + "_bucket", () -> bucketCreator.create(stillFluid.get(), (new Item.Properties()).stacksTo(1).craftRemainder(Items.BUCKET).setId(ResourceKey.create(Registries.ITEM, baseKey.withSuffix("_bucket")))));
        MapColor color = getClosestColor(renderProperties.color);
        DeferredHolder<Block, LiquidBlock> block = this.blockRegister.register(name, () -> new LiquidBlock(stillFluid.get(), net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().noCollision().strength(100.0F).noLootTable().replaceable().pushReaction(PushReaction.DESTROY).liquid().mapColor(color).setId(ResourceKey.create(Registries.BLOCK, baseKey))));
        return new FluidRegistryObject<>(fluidType, stillFluid, flowingFluid, bucket, block);
    }

    static MapColor[] NONE = List.of(MapColor.NONE).toArray(new MapColor[0]);

    private static MapColor getClosestColor(int tint) {
        if (tint == -1) {
            return MapColor.NONE;
        } else {
            int red = ARGB.red(tint);
            int green = ARGB.green(tint);
            int blue = ARGB.blue(tint);
            MapColor color = MapColor.NONE;
            double minDistance = Double.MAX_VALUE;
            MapColor[] var7 = NONE;
            int var8 = var7.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                MapColor toTest = var7[var9];
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
        private Identifier stillTexture = LIQUID;
        private Identifier flowingTexture = LIQUID_FLOW;
        private Identifier overlayTexture = OVERLAY;
        private Identifier renderOverlayTexture = RENDER_OVERLAY;
        private int color;
        private int temperature = 300; // vanilla water default
        private MovementBehavior movementBehavior = MovementBehavior.NONE;

        private FluidTypeRenderProperties() {
        }

        public static FluidTypeRenderProperties builder() {
            return new FluidTypeRenderProperties();
        }

        public FluidTypeRenderProperties texture(Identifier still, Identifier flowing) {
            this.stillTexture = still;
            this.flowingTexture = flowing;
            return this;
        }

        public FluidTypeRenderProperties texture(Identifier still, Identifier flowing, Identifier overlay) {
            this.stillTexture = still;
            this.flowingTexture = flowing;
            this.overlayTexture = overlay;
            return this;
        }

        public FluidTypeRenderProperties renderOverlay(Identifier renderOverlay) {
            this.renderOverlayTexture = renderOverlay;
            return this;
        }

        public FluidTypeRenderProperties tint(int color) {
            this.color = color;
            return this;
        }

        public FluidTypeRenderProperties temperature(int temperature) {
            this.temperature = temperature;
            return this;
        }

        public FluidTypeRenderProperties moveLikeWater() {
            this.movementBehavior = MovementBehavior.WATER;
            return this;
        }

        public FluidTypeRenderProperties moveLikeLava() {
            this.movementBehavior = MovementBehavior.LAVA;
            return this;
        }
    }

    public enum MovementBehavior {
        NONE,
        WATER,
        LAVA
    }

    public static class CoreFluidTypes extends FluidType {
        public final Identifier stillTexture;
        public final Identifier flowingTexture;
        public final Identifier overlayTexture;
        public final Identifier renderOverlayTexture;
        public final int color;
        private final MovementBehavior movementBehavior;

        public CoreFluidTypes(Properties properties, FluidTypeRenderProperties renderProperties) {
            super(properties);
            this.stillTexture = renderProperties.stillTexture;
            this.flowingTexture = renderProperties.flowingTexture;
            this.overlayTexture = renderProperties.overlayTexture;
            this.renderOverlayTexture = renderProperties.renderOverlayTexture;
            this.color = renderProperties.color;
            this.movementBehavior = renderProperties.movementBehavior;
        }

        @Override
        public boolean move(LivingEntity entity, Vec3 movementVector, double gravity) {
            boolean isFalling = entity.getDeltaMovement().y <= 0;
            double oldY = entity.getY();

            return switch (movementBehavior) {
                case WATER -> {
                    travelInWater(entity, movementVector, gravity, isFalling, oldY);
                    yield true;
                }
                case LAVA -> {
                    travelInLava(entity, movementVector, gravity, isFalling, oldY);
                    yield true;
                }
                case NONE -> false;
            };
        }

        public boolean isVaporizedOnPlacement(Level level, BlockPos pos, FluidStack stack) {
            return false;
        }

        public void travelInLava(LivingEntity entity, Vec3 input, double baseGravity, boolean isFalling, double oldY) {
            entity.moveRelative(0.02F, input);
            entity.move(MoverType.SELF, entity.getDeltaMovement());
            if (entity.getFluidHeight(FluidTags.LAVA) <= entity.getFluidJumpThreshold()) {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 0.8F, 0.5));
                Vec3 movement = entity.getFluidFallingAdjustedMovement(baseGravity, isFalling, entity.getDeltaMovement());
                entity.setDeltaMovement(movement);
            } else {
                entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5));
            }

            if (baseGravity != 0.0) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, -baseGravity / 4.0, 0.0));
            }

            jumpOutOfFluid(entity, oldY);
        }

        public void travelInWater(LivingEntity entity, Vec3 input, double baseGravity, boolean isFalling, double oldY) {
            float slowDown = entity.isSprinting() ? 0.9F : getWaterSlowDown();
            float speed = 0.02F;
            float waterWalker = (float)entity.getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY);
            if (!entity.onGround()) {
                waterWalker *= 0.5F;
            }

            if (waterWalker > 0.0F) {
                slowDown += (0.54600006F - slowDown) * waterWalker;
                speed += (entity.getSpeed() - speed) * waterWalker;
            }

            if (entity.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                slowDown = 0.96F;
            }

            speed *= (float)entity.getAttributeValue(NeoForgeMod.SWIM_SPEED);
            entity.moveRelative(speed, input);
            entity.move(MoverType.SELF, entity.getDeltaMovement());
            Vec3 ladderMovement = entity.getDeltaMovement();
            if (entity.horizontalCollision && entity.onClimbable()) {
                ladderMovement = new Vec3(ladderMovement.x, 0.2, ladderMovement.z);
            }

            ladderMovement = ladderMovement.multiply((double)slowDown, (double)0.8F, (double)slowDown);
            entity.setDeltaMovement(entity.getFluidFallingAdjustedMovement(baseGravity, isFalling, ladderMovement));
            jumpOutOfFluid(entity, oldY);
        }

        protected float getWaterSlowDown() {
            return 0.8F;
        }


        private void jumpOutOfFluid(LivingEntity entity, double oldY) {
            Vec3 movement = entity.getDeltaMovement();
            if (entity.horizontalCollision && entity.isFree(movement.x, movement.y + (double)0.6F - entity.getY() + oldY, movement.z)) {
                entity.setDeltaMovement(movement.x, (double)0.3F, movement.z);
            }

        }

    }
}