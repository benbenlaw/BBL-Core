package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.util.CoreTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = Core.MOD_ID)
public class ClimbableBlockEvent {



    @SubscribeEvent
    public static void onPlayerTick(EntityTickEvent.Post event) {

        Entity entity = event.getEntity();
        Level level = entity.level();

        if (entity.horizontalCollision) {
            BlockPos entityPos = entity.blockPosition();
            for (Direction direction : Direction.values()) {
                BlockPos adjacentPos = entityPos.relative(direction);
                BlockState blockState = level.getBlockState(adjacentPos);

                if (blockState.isFaceSturdy(level, adjacentPos, direction.getOpposite())) {
                    if (blockState.is(CoreTags.Blocks.CLIMBABLE_BLOCKS)) {
                        Vec3 initialVec = entity.getDeltaMovement();
                        Vec3 climbVec = new Vec3(initialVec.x, 0.2D, initialVec.z);
                        entity.setDeltaMovement(climbVec.scale(0.96D));
                        break;
                    }
                }
            }
        }
    }
}