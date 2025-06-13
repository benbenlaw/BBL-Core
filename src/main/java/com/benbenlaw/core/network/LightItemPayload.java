package com.benbenlaw.core.network;

import com.benbenlaw.core.Core;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record LightItemPayload(int lightLevel) implements CustomPacketPayload {

    public static final Type<LightItemPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "light_item"));

    @Override
    public Type<LightItemPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, LightItemPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, LightItemPayload::lightLevel,
            LightItemPayload::new
    );


}
