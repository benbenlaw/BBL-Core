package com.benbenlaw.core.recipe;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.util.WorldGenerationInfo;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class WorldTypeCondition implements ICondition {

    public static final WorldTypeCondition INSTANCE = new WorldTypeCondition();
    public static final MapCodec<WorldTypeCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final Minecraft minecraft = Minecraft.getInstance();


    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(@NotNull IContext context) {



        MinecraftServer server = minecraft.getSingleplayerServer();
        Player player = minecraft.player;

        if (player == null) {
            System.out.println("Condition failed: No player available");
            return false;
        }

        if (server == null) {
            System.out.println("Condition failed: No server available");
            return false;
        }
        WorldData worldData = server.getWorldData();

        if (worldData.isFlatWorld()) {
            System.out.println("Condition failed: Is Super Flat");
            return true;
        } else {
            System.out.println("Condition passed: Not Super Flat");
            return false;
        }
    }


    @Override
    public String toString() {
        return "Valid World Type";
    }
}
