package com.benbenlaw.core.network;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.item.CoreDataComponents;
import com.benbenlaw.core.item.LightItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record LightItemPacket(int lightLevel) implements CustomPacketPayload  {

    public static final Type<LightItemPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "light_item"));


    public static final StreamCodec<FriendlyByteBuf, LightItemPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, LightItemPacket::lightLevel,
            LightItemPacket::new
    );

    public static void handle(final LightItemPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ItemStack item = player.getMainHandItem();
            if (item.getItem() instanceof LightItem) {
                item.set(CoreDataComponents.LIGHT_LEVEL, payload.lightLevel());
            }
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

