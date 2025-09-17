package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.config.StartupConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = Core.MOD_ID)
public class BrokenBlockRemoverEvent {

    public static boolean worldSaved = false;
    public static BlockPos positionToFix = new BlockPos(StartupConfig.positionToFixX.get(), StartupConfig.positionToFixY.get(), StartupConfig.positionToFixZ.get());

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        LevelAccessor accessor = event.getLevel();
        if (StartupConfig.enableSaveTheWorld.get()) {
            if (accessor instanceof ServerLevel level) {
                level.setBlock(positionToFix, Blocks.AIR.defaultBlockState(), 3);
                worldSaved = true;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {

        Player player = event.getEntity();

        if (player instanceof ServerPlayer) {
            if (StartupConfig.enableSaveTheWorld.get()) {
                if (worldSaved && player.level().getBlockState(positionToFix).is(Blocks.AIR)) {
                    ((ServerPlayer) player).sendSystemMessage(Component.literal("A problematic block was removed at " + positionToFix.getX() + ", " + positionToFix.getY() + ", " + positionToFix.getZ() +
                            ". Please disable the config and restart the game to prevent any accidental removal of blocks."));
                }
            }
        }
    }


}
