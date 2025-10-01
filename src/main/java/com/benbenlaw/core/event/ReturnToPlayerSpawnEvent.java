package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.config.ModpackConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.Objects;
import java.util.Set;


@EventBusSubscriber(modid = Core.MOD_ID)
public class ReturnToPlayerSpawnEvent {

    @SubscribeEvent
    public static void onVoidDamage(LivingDamageEvent.Post event) {

        if (ModpackConfig.enabledVoidProtection.get()) {

            LivingEntity livingEntity = event.getEntity();
            DamageSource damageSource = event.getSource();

            if (livingEntity instanceof ServerPlayer player && damageSource.is(DamageTypes.FELL_OUT_OF_WORLD)) {

                ServerPlayer.RespawnConfig respawnConfig = player.getRespawnConfig();

                assert respawnConfig != null;
                BlockPos spawnPos = respawnConfig.pos();
                ResourceKey<Level> dimension = respawnConfig.dimension();
                ServerLevel serverLevel = Objects.requireNonNull(player.getServer()).getLevel(dimension);

                assert serverLevel != null;
                player.fallDistance = 0.0F;

                TeleportTransition transition = new TeleportTransition(
                        serverLevel,
                        new Vec3(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ()),
                        Vec3.ZERO,
                        0.0F, 0.0F,
                        Set.of(),
                        TeleportTransition.DO_NOTHING
                );

                player.teleport(transition);
                player.sendSystemMessage(Component.translatable("chat.bblcore.falling.home"));

            }
        }
    }
}
