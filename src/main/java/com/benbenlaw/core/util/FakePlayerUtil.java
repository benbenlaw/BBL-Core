package com.benbenlaw.core.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.UUID;

public class FakePlayerUtil {

    public static FakePlayer createFakePlayer(ServerLevel level, String name) {
        return new FakePlayer(level, new GameProfile(UUID.randomUUID(), name));
    }
}
