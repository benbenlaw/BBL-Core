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

public record LightItemPacket() {

    public static final LightItemPacket INSTANCE = new LightItemPacket();
    public static LightItemPacket get() {
        return INSTANCE;
    }

    public void handle(final LightItemPayload payload, IPayloadContext context) {

        context.enqueueWork(() -> {

            ServerPlayer player = (ServerPlayer) context.player();
            ItemStack item = player.getMainHandItem();
            if (item.getItem() instanceof LightItem) {
                item.set(CoreDataComponents.LIGHT_LEVEL, payload.lightLevel());
            }
        });
    }
}
