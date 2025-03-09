package com.benbenlaw.core.block.brightable.flammable;

import com.benbenlaw.core.block.brightable.BrightFenceGate;
import com.benbenlaw.core.block.colored.ColoredFenceGate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class BrightFlammableFenceGate extends BrightFenceGate {

    public BrightFlammableFenceGate(Properties properties, SoundEvent openSound, SoundEvent closeSound) {
        super(properties, openSound, closeSound);
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
