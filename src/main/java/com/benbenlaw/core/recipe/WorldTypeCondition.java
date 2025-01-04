package com.benbenlaw.core.recipe;

import com.benbenlaw.core.world.WorldInfoCache;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.util.WorldGenerationInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record WorldTypeCondition(String worldType) implements ICondition {

    public static final MapCodec<WorldTypeCondition> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder
                    .group(
                            Codec.STRING.fieldOf("world_type").forGetter(WorldTypeCondition::worldType))
                    .apply(builder, WorldTypeCondition::new));


    public static final Minecraft minecraft = Minecraft.getInstance();


    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }


    @Override
    public boolean test(@NotNull IContext context) {

        String gameWorldType = WorldInfoCache.getWorldType();

        if (gameWorldType == null) {
            System.out.println("Condition Failed: World type is null");
            return false;
        }

        else if (gameWorldType.contains(worldType)) {
            System.out.println("Condition Passed: World type matches the recipe");
            return true;
        }

        else {
            System.out.println("Condition Failed: World type does not match the recipe");
            return false;
        }
    }






    @Override
    public String toString() {
        return "Valid World Type";
    }
}
