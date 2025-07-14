package com.benbenlaw.core.network;

import com.benbenlaw.core.Core;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class CoreNetworking {

    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Core.MOD_ID);


        //To Server From Client
        registrar.playToServer(LightItemPacket.TYPE, LightItemPacket.STREAM_CODEC, LightItemPacket::handle);



    }
}
