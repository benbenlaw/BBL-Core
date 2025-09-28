package com.benbenlaw.core.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CoreItemUtils {

    public static void popItemStack(Level level, BlockPos pos, ItemStack stack) {
        ItemEntity itemAsEntity = new ItemEntity(level,
                ((level.random.nextFloat() * 0.1) + 0.5) + pos.getX(),
                ((level.random.nextFloat() * 0.1) + 0.5) + pos.getY(),
                ((level.random.nextFloat() * 0.1) + 0.5) + pos.getZ(),
                stack
        );
        itemAsEntity.setDefaultPickUpDelay();
        itemAsEntity.setDeltaMovement((level.random.nextFloat() * 0.1 - 0.05), (level.random.nextFloat() * 0.1 - 0.03), (level.random.nextFloat() * 0.1 - 0.05));
        level.addFreshEntity(itemAsEntity);
    }

}
