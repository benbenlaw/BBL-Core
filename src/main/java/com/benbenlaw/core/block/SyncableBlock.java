package com.benbenlaw.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A base block class for blocks that has a redstone control and a facing direction.
 * Direction is all 6 sides
 * When powered, the block's "running" state is set to false.
 * When unpowered, the block's "running" state is set to true.
 */
public abstract class SyncableBlock extends BaseEntityBlock {

    public static final BooleanProperty RUNNING = BooleanProperty.create("running");
    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;

    protected SyncableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(RUNNING, true)
                        .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected abstract @NotNull MapCodec<? extends BaseEntityBlock> codec();

    @Override
    protected void neighborChanged(@NotNull BlockState state, Level level, @NotNull BlockPos pos,
                                   @NotNull Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide()) {
            boolean powered = level.hasNeighborSignal(pos);
            if (powered && state.getValue(RUNNING)) {
                level.setBlock(pos, state.setValue(RUNNING, false), 3);
            } else if (!powered && !state.getValue(RUNNING)) {
                level.setBlock(pos, state.setValue(RUNNING, true), 3);
            }
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getNearestLookingDirection().getOpposite();
        return this.defaultBlockState()
                .setValue(FACING, direction)
                .setValue(RUNNING, true);
    }

    @Override
    public @NotNull BlockState rotate(BlockState blockState, @NotNull LevelAccessor level,
                                      @NotNull BlockPos blockPos, Rotation direction) {
        return blockState.setValue(FACING, direction.rotate(blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RUNNING, FACING);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }
}