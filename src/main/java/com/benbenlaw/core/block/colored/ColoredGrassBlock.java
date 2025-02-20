package com.benbenlaw.core.block.colored;

import com.benbenlaw.core.block.colored.util.BlockTypeColorFinder;
import com.benbenlaw.core.block.colored.util.ColorMap;
import com.benbenlaw.core.block.colored.util.IColored;
import com.benbenlaw.core.item.CoreDataComponents;
import com.benbenlaw.core.item.colored.ColoredBlockItem;
import com.benbenlaw.core.item.colored.ColoredItem;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.function.Function;
import java.util.function.Supplier;

public class ColoredGrassBlock extends GrassBlock implements IColored, BonemealableBlock {

    public static Map<DyeColor, ResourceKey<ConfiguredFeature<?, ?>>> VEGETATION_PLACEMENT_MAP = new HashMap<>();
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static ColoredBlock DIRT;
    public static ColoredTallGrassBlock SHORT_GRASS_BLOCK;
    public static ResourceKey<ConfiguredFeature<?, ?>>  VEGETATION_PLACEMENT;

    public ColoredGrassBlock(Properties properties, ColoredBlock dirt, ColoredTallGrassBlock shortGrassBlock, ResourceKey<ConfiguredFeature<?, ?>> vegetationPlacement) {
        super(properties);
        DIRT = dirt;
        SHORT_GRASS_BLOCK = shortGrassBlock;
        this.registerDefaultState(this.stateDefinition.any().setValue(COLOR, DyeColor.WHITE).setValue(LIT, Boolean.FALSE).setValue(SNOWY, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR, LIT, SNOWY);
    }

    @Override
    public int getColor(int index) {
        DyeColor color = DyeColor.values()[index % DyeColor.values().length];
        return ColorMap.getColorValue(color);
    }


    @Override
    public int getColor(int index, ItemStack stack) {
        return getColor(index);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        Function<BlockState, DyeColor> colorFunction = BlockTypeColorFinder.BLOCK_TYPE_COLOR_FINDER.get(state.getBlock().getClass());
        DyeColor color = (colorFunction != null) ? colorFunction.apply(state) : DyeColor.WHITE;

        if (!canBeGrass(state, level, pos)) {
            if (!level.isAreaLoaded(pos, 1)) {
                return;
            }

            level.setBlockAndUpdate(pos, DIRT.defaultBlockState().setValue(ColoredBlock.COLOR, color));
        } else {
            if (!level.isAreaLoaded(pos, 3)) {
                return;
            }

            if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                BlockState blockstate = this.defaultBlockState().setValue(COLOR, color);

                for(int i = 0; i < 4; ++i) {
                    BlockPos blockpos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (level.getBlockState(blockpos).is(DIRT) && canPropagate(blockstate, level, blockpos)) {
                        level.setBlockAndUpdate(blockpos, blockstate.setValue(SNOWY, level.getBlockState(blockpos.above()).is(Blocks.SNOW)));
                    }
                }
            }
        }

    }

    private static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = levelReader.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && (Integer)blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(levelReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(levelReader, blockpos));
            return i < levelReader.getMaxLightLevel();
        }
    }

    private static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return canBeGrass(state, level, pos) && !level.getFluidState(blockpos).is(FluidTags.WATER);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader p_256229_, BlockPos p_256432_, BlockState p_255677_) {
        return p_256229_.getBlockState(p_256432_.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level p_221275_, RandomSource p_221276_, BlockPos p_221277_, BlockState p_221278_) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos blockPos, BlockState state) {
        DyeColor color = state.getValue(COLOR);

        level.registryAccess()
                .registry(Registries.CONFIGURED_FEATURE)
                .flatMap(registry -> registry.getHolder(VEGETATION_PLACEMENT_MAP.get(color)))
                .ifPresent(feature -> {
                    BlockPos targetPos = blockPos;
                    if (level.getBlockState(targetPos).is(state.getBlock()) && level.getBlockState(targetPos.above()).isAir()) {
                        feature.value().place(level, level.getChunkSource().getGenerator(), random, targetPos.above());
                    }

                    for (int i = 0; i < 128; i++) {
                        BlockPos spreadPos = blockPos.offset(random.nextInt(5) - 2, random.nextInt(3) - 1, random.nextInt(5) - 2);

                        // Ensure we are spreading only on the same block type and properties (e.g., color)
                        BlockState spreadState = level.getBlockState(spreadPos);

                        // Match both block type and any additional properties (e.g., color)
                        if (spreadState.is(state.getBlock()) && spreadState.getValue(COLOR).equals(state.getValue(COLOR))) {
                            if (random.nextFloat() < 0.40f) {
                                if (random.nextBoolean()) {
                                    // Spread above the block
                                    BlockPos abovePos = spreadPos.above();
                                    if (level.getBlockState(abovePos).isAir()) {
                                        feature.value().place(level, level.getChunkSource().getGenerator(), random, abovePos);
                                    }
                                } else {
                                    // Spread below the block
                                    BlockPos belowPos = spreadPos.below();
                                    if (level.getBlockState(belowPos).isAir()) {
                                        feature.value().place(level, level.getChunkSource().getGenerator(), random, belowPos);
                                    }
                                }
                            }
                        } else {
                            // If it doesn't match, try spreading vegetation on air blocks in the vicinity
                            if (level.getBlockState(spreadPos).isAir()) {
                                feature.value().place(level, level.getChunkSource().getGenerator(), random, spreadPos);
                            }
                        }
                    }
                });
    }

    public static void updateVegetationPlacementMap(DyeColor color, ResourceKey<ConfiguredFeature<?, ?>> vegetationPlacement) {
        VEGETATION_PLACEMENT_MAP.put(color, vegetationPlacement);
    }


    public BonemealableBlock.Type getType() {
        return Type.NEIGHBOR_SPREADER;
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        DyeColor color = state.getValue(COLOR);

        for (ItemStack drop : drops) {
            if (drop.getItem() instanceof BlockItem && ((BlockItem) drop.getItem()).getBlock() == this) {
                drop.set(CoreDataComponents.COLOR, color.toString());
                drop.set(CoreDataComponents.LIT, state.getValue(LIT));
            }

            if (drop.getItem() instanceof ColoredBlockItem || drop.getItem() instanceof ColoredItem) {
                drop.set(CoreDataComponents.COLOR, color.toString());
                drop.set(CoreDataComponents.LIT, state.getValue(LIT));
            }
        }
        return drops;
    }


    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {

        if (state.getBlock() instanceof ColoredGrassBlock) {
            DyeColor color = state.getValue(COLOR);
            ItemStack stack = new ItemStack(this);
            stack.set(CoreDataComponents.COLOR, color.toString());
            stack.set(CoreDataComponents.LIT, state.getValue(LIT));
            return stack;
        }

        return super.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (stack.get(CoreDataComponents.COLOR) != null && stack.get(CoreDataComponents.LIT) != null) {
            String colorString = stack.get(CoreDataComponents.COLOR);
            assert colorString != null;
            DyeColor dyeColor = ColorMap.getDyeColor(colorString);

            Boolean lit = stack.get(CoreDataComponents.LIT);
            assert lit != null;

            BlockState newState = state.setValue(COLOR, dyeColor).setValue(LIT, lit);

            level.setBlockAndUpdate(pos, newState);
        } else {
            level.setBlockAndUpdate(pos, state);
        }
    }







}