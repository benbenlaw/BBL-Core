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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.EnumSet;
import java.util.Objects;

@EventBusSubscriber(modid = Core.MOD_ID)
public class ReturnToPlayerSpawnEvent {

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickItem event) {

     //   if (event.getItemStack().is(CommonTags.getTag(RUBY, GEMS))) {
//
       //     event.getEntity().sendSystemMessage(Component.literal("its working !!!!!!!!"));
//
       // }
    }

    @SubscribeEvent
    public static void onVoidDamage(LivingDamageEvent.Post event) {

        if (CoreStartupConfig.enabledVoidProtection.get()) {

            LivingEntity livingEntity = event.getEntity();
            DamageSource damageSource = event.getSource();

            if (livingEntity instanceof ServerPlayer player && damageSource.is(DamageTypes.FELL_OUT_OF_WORLD)) {

                assert player.getRespawnConfig() != null;
                BlockPos spawnPos = player.getRespawnConfig().pos();
                ResourceKey<Level> dimension = player.getRespawnConfig().dimension();
                ServerLevel serverLevel = Objects.requireNonNull(player.getServer()).getLevel(dimension);

                assert serverLevel != null;

                if (spawnPos == null) {
                    spawnPos = serverLevel.getSharedSpawnPos();
                    player.sendSystemMessage(Component.translatable("chat.bblcore.falling.default"));
                }

                player.fallDistance = 0.0F;
                player.teleportTo(serverLevel, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), EnumSet.noneOf(Relative.class), 0.0F, 0.0F, true);
                player.sendSystemMessage(Component.translatable("chat.bblcore.falling.home"));

            }
        }
    }
}
