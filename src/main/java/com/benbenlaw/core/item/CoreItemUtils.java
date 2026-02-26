package com.benbenlaw.core.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CoreItemUtils {

    public static void popItemStack(Level level, BlockPos pos, ItemStack stack) {
        ItemEntity itemAsEntity = new ItemEntity(level,
                ((level.getRandom().nextFloat() * 0.1) + 0.5) + pos.getX(),
                ((level.getRandom().nextFloat() * 0.1) + 0.5) + pos.getY(),
                ((level.getRandom().nextFloat() * 0.1) + 0.5) + pos.getZ(),
                stack
        );
        itemAsEntity.setDefaultPickUpDelay();
        itemAsEntity.setDeltaMovement((level.getRandom().nextFloat() * 0.1 - 0.05), (level.getRandom().nextFloat() * 0.1 - 0.03), (level.getRandom().nextFloat() * 0.1 - 0.05));
        level.addFreshEntity(itemAsEntity);
    }

}
