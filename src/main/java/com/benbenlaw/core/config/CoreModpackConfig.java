package com.benbenlaw.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CoreModpackConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<String> serverName;
    public static final ModConfigSpec.ConfigValue<String> serverIP;
    public static final ModConfigSpec.ConfigValue<String> discordURL;
    public static final ModConfigSpec.ConfigValue<String> modpackName;
    public static final ModConfigSpec.ConfigValue<String> modpackVersion;
    public static final ModConfigSpec.ConfigValue<Boolean> updateChecker;

    static {

        // Modpack Config
        BUILDER.comment("Modpack Configuration Settings");

        //Custom Server
        BUILDER.push("Custom server in multiplayer Screen");

        serverName = BUILDER.comment("Name of the server").define("Server Name", "");
        serverIP = BUILDER.comment("IP of the server").define("Server IP", "");

        BUILDER.pop();

        //Discord URL
        BUILDER.push("Discord URL use /discord command to get the link");

        discordURL = BUILDER.comment("Add a URL to link your discord, default = https://discord.gg/benbenlaw")
                .define("Discord URL", "https://discord.gg/benbenlaw");

        BUILDER.pop();

        //Modpack Information
        BUILDER.push("Modpack information handy for modpack makers use /modpack in game to get the information");

        modpackName = BUILDER.comment("Modpack name").define("Modpack name", "");
        modpackVersion = BUILDER.comment("Modpack Version").define("Modpack Version", "");

        BUILDER.pop();

        //Update Checker
        BUILDER.push("Check if the modpack has any updates available");

        updateChecker = BUILDER.comment("Check if the modpack has any updates available, default = false")
                .define("Update Checker", false);

        BUILDER.pop();

        //LAST
        SPEC = BUILDER.build();

    }

}
