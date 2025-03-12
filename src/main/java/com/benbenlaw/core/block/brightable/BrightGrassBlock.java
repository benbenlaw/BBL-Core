package com.benbenlaw.core.block.brightable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.lighting.LightEngine;

public class BrightGrassBlock extends GrassBlock implements BonemealableBlock, IBrightable {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private final Block DIRT_BLOCK;
    private final Block SHORT_GRASS_BLOCK;
    private final ResourceKey<ConfiguredFeature<?, ?>> VEGETATION_PLACEMENT;


    public BrightGrassBlock(Properties properties, ResourceKey<ConfiguredFeature<?, ?>> vegetationPlacements, Block dirt, Block shortGrassBlock) {
        super(properties);
        VEGETATION_PLACEMENT = vegetationPlacements;
        DIRT_BLOCK = dirt;
        SHORT_GRASS_BLOCK = shortGrassBlock;
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE).setValue(SNOWY, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, SNOWY);
    }

    private static boolean canBeGrass(BlockState p_56824_, LevelReader p_56825_, BlockPos p_56826_) {
        BlockPos blockpos = p_56826_.above();
        BlockState blockstate = p_56825_.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(
                    p_56825_, p_56824_, p_56826_, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(p_56825_, blockpos)
            );
            return i < p_56825_.getMaxLightLevel();
        }
    }

    private static boolean canPropagate(BlockState p_56828_, LevelReader p_56829_, BlockPos p_56830_) {
        BlockPos blockpos = p_56830_.above();
        return canBeGrass(p_56828_, p_56829_, p_56830_) && !p_56829_.getFluidState(blockpos).is(FluidTags.WATER);
    }

    @Override
    protected void randomTick(BlockState p_222508_, ServerLevel p_222509_, BlockPos p_222510_, RandomSource p_222511_) {
        if (!canBeGrass(p_222508_, p_222509_, p_222510_)) {
            if (!p_222509_.isAreaLoaded(p_222510_, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            p_222509_.setBlockAndUpdate(p_222510_, DIRT_BLOCK.defaultBlockState());
        } else {
            if (!p_222509_.isAreaLoaded(p_222510_, 3)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            if (p_222509_.getMaxLocalRawBrightness(p_222510_.above()) >= 9) {
                BlockState blockstate = this.defaultBlockState();

                for (int i = 0; i < 4; i++) {
                    BlockPos blockpos = p_222510_.offset(p_222511_.nextInt(3) - 1, p_222511_.nextInt(5) - 3, p_222511_.nextInt(3) - 1);
                    if (p_222509_.getBlockState(blockpos).is(DIRT_BLOCK) && canPropagate(blockstate, p_222509_, blockpos)) {
                        p_222509_.setBlockAndUpdate(
                                blockpos, blockstate.setValue(SNOWY, Boolean.valueOf(p_222509_.getBlockState(blockpos.above()).is(Blocks.SNOW)))
                        );
                    }
                }
            }
        }
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos blockPos, BlockState state) {
        level.registryAccess().registry(Registries.CONFIGURED_FEATURE).flatMap((registry) -> registry.getHolder(VEGETATION_PLACEMENT)).ifPresent((feature) -> {

            // Place vegetation directly above the clicked block
            if (level.getBlockState(blockPos).is(state.getBlock()) && level.getBlockState(blockPos.above()).isAir()) {
                feature.value().place(level, level.getChunkSource().getGenerator(), random, blockPos.above());
            }

            // Controlled spreading logic (only spread on BrightGrassBlock)
            for (int i = 0; i < 128; ++i) {  // Reduced spread attempts for better balance
                BlockPos spreadPos = blockPos.offset(random.nextInt(5) - 2, random.nextInt(3) - 1, random.nextInt(5) - 2);
                BlockState spreadState = level.getBlockState(spreadPos);

                // Spread only on BrightGrassBlock, NOT on dirt
                if (spreadState.is(state.getBlock())) {
                    if (random.nextFloat() < 0.3F) { // Lower chance of spreading to control density
                        BlockPos abovePos = spreadPos.above();
                        if (level.getBlockState(abovePos).isAir()) {
                            feature.value().place(level, level.getChunkSource().getGenerator(), random, abovePos);
                        }
                    }
                }
            }
        });
    }


}