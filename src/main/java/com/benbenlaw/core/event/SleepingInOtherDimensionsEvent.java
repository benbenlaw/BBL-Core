package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.config.DimensionConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EventBusSubscriber(modid = Core.MOD_ID)
public class SleepingInOtherDimensionsEvent {

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ServerLevel level = (ServerLevel) player.level();
        if (level.isClientSide()) return;

        // Get list of dimension strings from config
        List<String> configuredDims = new ArrayList<>(DimensionConfig.dimensionNames.get());

        // Convert strings to ResourceLocations and check if current dimension matches any
        boolean isConfiguredDim = configuredDims.stream()
                .map(ResourceLocation::tryParse)
                .filter(Objects::nonNull)
                .anyMatch(dim -> dim.equals(level.dimension().location()));

        if (!isConfiguredDim) return;

        MinecraftServer server = player.getServer();
        long currentTime = level.getDayTime();
        long newTime = ((currentTime / 24000) + 1) * 24000;
        String command = "time set " + newTime;

        assert server != null;
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withLevel(level).withSuppressedOutput(), command);
    }


}