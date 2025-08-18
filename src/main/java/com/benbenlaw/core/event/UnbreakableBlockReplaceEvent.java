package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.util.BlockInformation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@EventBusSubscriber(modid = Core.MOD_ID)
public class UnbreakableBlockReplaceEvent {

    public static final Map<BlockPos, BlockInformation> blockInformationMap = new HashMap<>();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (blockInformationMap.isEmpty()) return;

        for (Map.Entry<BlockPos, BlockInformation> entry : blockInformationMap.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockInformation blockInfo = entry.getValue();
            Level level = blockInfo.level();

            if (level.isClientSide()) continue;

            if (Objects.requireNonNull(level.getServer()).getTickCount() >= blockInfo.tickPlace()) {
                level.setBlockAndUpdate(pos, blockInfo.state());
                blockInformationMap.remove(pos);
            }
        }
    }

}
