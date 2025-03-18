package com.benbenlaw.core.mixin.client;

import com.benbenlaw.core.config.CoreDefaultServerConfig;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerList.class)
public class ServerListMixin {

    @Inject(method = "load", at = @At("RETURN"))
    private void injectCustomServer(CallbackInfo ci) {

        if (CoreDefaultServerConfig.enableDefaultServer.get()) {

            ServerList serverList = (ServerList) (Object) this;
            List<ServerData> servers = ((ServerListAccessor) serverList).getServerList();

            String serverName = CoreDefaultServerConfig.serverName.get();
            String serverIP = CoreDefaultServerConfig.serverIP.get();

            servers.removeIf(server -> server.ip.equals(serverIP));
            ServerData customServer = new ServerData(serverName, serverIP, ServerData.Type.OTHER);
            servers.add(0, customServer);
            serverList.save();
        }
    }
}

