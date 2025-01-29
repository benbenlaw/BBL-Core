package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.config.CoreStartupConfig;
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

        boolean isTouchingClimbable = false;
        BlockPos entityPos = entity.blockPosition();

        for (Direction direction : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            BlockPos adjacentPos = entityPos.relative(direction);
            BlockState blockState = level.getBlockState(adjacentPos);

            if (blockState.is(CoreTags.Blocks.CLIMBABLE_BLOCKS) &&
                    blockState.isFaceSturdy(level, adjacentPos, direction.getOpposite())) {
                isTouchingClimbable = true;
                break;
            }
        }

        Vec3 movement = entity.getDeltaMovement();

        //Up
        if (entity instanceof Player player) {
            if (entity.horizontalCollision && isTouchingClimbable) {
                if (!player.isShiftKeyDown()) { // Only climb if not sneaking
                    Vec3 climbVec = new Vec3(movement.x, CoreStartupConfig.climbableBlockSpeed.get(), movement.z);
                    entity.setDeltaMovement(climbVec.scale(0.96));
                } else {
                    entity.setDeltaMovement(new Vec3(movement.x, 0, movement.z));
                }
            }
            //Down
            else if (isTouchingClimbable && movement.y < 0) {
                if (!player.isShiftKeyDown()) { // Only slow fall if not sneaking
                    Vec3 slowFallVec = new Vec3(movement.x, -CoreStartupConfig.climbableBlockSpeed.get(), movement.z);
                    entity.setDeltaMovement(slowFallVec);
                    entity.resetFallDistance();
                }
                //Shift Held, Stop
                else {
                    entity.setDeltaMovement(new Vec3(movement.x, 0, movement.z));
                    entity.resetFallDistance();
                }
            }
        }
    }
}