package com.benbenlaw.core.util;

import net.minecraft.core.Direction;

public class DirectionUtil {
    public static Direction adjustPosition(Direction facing, Direction direction) {
        return switch (facing) {
            case NORTH, UP, DOWN -> direction;
            case SOUTH -> direction.getOpposite();
            case EAST -> direction.getClockWise();
            case WEST -> direction.getCounterClockWise();
        };
    }
}