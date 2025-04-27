package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.config.CoreStartupConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.apache.logging.log4j.core.jmx.Server;

import javax.management.Attribute;
import java.util.Objects;

import static net.minecraft.world.level.portal.DimensionTransition.DO_NOTHING;

@EventBusSubscriber(modid = Core.MOD_ID)
public class ReturnToPlayerSpawnEvent {


    @SubscribeEvent
    public static void onVoidDamage(LivingDamageEvent.Post event) {

        if (CoreStartupConfig.enabledVoidProtection.get()) {

            LivingEntity livingEntity = event.getEntity();
            DamageSource damageSource = event.getSource();

            if (livingEntity instanceof ServerPlayer player && damageSource.is(DamageTypes.FELL_OUT_OF_WORLD)) {

                BlockPos spawnPos = player.getRespawnPosition();
                ResourceKey<Level> dimension = player.getRespawnDimension();
                ServerLevel serverLevel = Objects.requireNonNull(player.getServer()).getLevel(dimension);

                assert serverLevel != null;

                if (spawnPos == null) {
                    spawnPos = serverLevel.getSharedSpawnPos();
                    player.sendSystemMessage(Component.translatable("chat.bblcore.falling.default"));
                }

                player.fallDistance = 0.0F;
                player.teleportTo(serverLevel, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0.0F, 0.0F);
                player.sendSystemMessage(Component.translatable("chat.bblcore.falling.home"));

            }
        }
    }
}
