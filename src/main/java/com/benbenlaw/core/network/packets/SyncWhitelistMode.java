package com.benbenlaw.core.network.packets;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.WhitelistBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record SyncWhitelistMode(BlockPos pos, boolean whitelist) implements CustomPacketPayload {

    public static final Type<SyncWhitelistMode> TYPE = new Type<>(Core.identifier("sync_whitelist_mode"));

    public static final IPayloadHandler<SyncWhitelistMode> HANDLER = (packet, context) -> {
        BlockEntity entity = context.player().level().getBlockEntity(packet.pos);

        if (entity instanceof WhitelistBlockEntity whitelistBlockEntity) {
            whitelistBlockEntity.setWhitelist(packet.whitelist);

            context.player().playSound(SoundEvents.LEVER_CLICK, SoundSource.PLAYERS.ordinal(), 1.0f);

            if (whitelistBlockEntity instanceof SyncableBlockEntity syncableBlockEntity) {
                syncableBlockEntity.setChanged();
                syncableBlockEntity.sync();
            }
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncWhitelistMode> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SyncWhitelistMode::pos,
            ByteBufCodecs.BOOL, SyncWhitelistMode::whitelist,
            SyncWhitelistMode::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
