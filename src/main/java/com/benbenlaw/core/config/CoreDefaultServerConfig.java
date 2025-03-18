package com.benbenlaw.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CoreDefaultServerConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<Boolean> enableDefaultServer;
    public static final ModConfigSpec.ConfigValue<String> serverName;
    public static final ModConfigSpec.ConfigValue<String> serverIP;

    static {

        // Caveopolis Configs
        BUILDER.comment("BBL Core Default Server Config")
                .push("Default Server");

        enableDefaultServer = BUILDER.comment("If you want to add a server into the multiplayer screen, default = false")
                .define("Enable Default Server", false);

        serverName = BUILDER.comment("Name of the server")
                .define("Server Name", "");

        serverIP = BUILDER.comment("IP of the server")
                .define("Server IP", "");


        BUILDER.pop();

        //LAST
        SPEC = BUILDER.build();

    }

}
