package com.benbenlaw.core.block.brightable.flammable;

import com.benbenlaw.core.block.brightable.BrightStairs;
import com.benbenlaw.core.block.colored.ColoredStairs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class BrightFlammableStairs extends BrightStairs {

    public BrightFlammableStairs(BlockState state, Properties properties) {
        super(state, properties);
    }
    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 20;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }
}
