package com.benbenlaw.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ModpackConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<String> serverName;
    public static final ModConfigSpec.ConfigValue<String> serverIP;
    public static final ModConfigSpec.ConfigValue<String> discordURL;
    public static final ModConfigSpec.ConfigValue<String> modpackName;
    public static final ModConfigSpec.ConfigValue<String> modpackVersion;
    public static final ModConfigSpec.ConfigValue<Boolean> updateChecker;
    public static final ModConfigSpec.ConfigValue<Integer> projectID;
    public static final ModConfigSpec.ConfigValue<Double> horizonHeight;
    public static final ModConfigSpec.ConfigValue<Boolean> enabledVoidProtection;
    public static final ModConfigSpec.ConfigValue<Double> climbableBlockSpeed;


    static {

        //Modpack Config
        BUILDER.comment("Modpack Configuration Settings");

        //Server
        BUILDER.push("Custom server in multiplayer Screen");

        serverName = BUILDER.comment("Name of the server").define("Server Name", "");
        serverIP = BUILDER.comment("IP of the server").define("Server IP", "");

        BUILDER.pop();

        //Update Checker
        BUILDER.push("Check if the modpack has any updates available");

        updateChecker = BUILDER.comment("Check if the modpack has any updates available, default = false")
                .define("Update Checker", false);

        projectID = BUILDER.comment("Project ID for the modpack, found on curse forge")
                .define("Project ID", 0);

        BUILDER.pop();

        //Misc
        BUILDER.push("Misc Modpack Settings");

        modpackName = BUILDER.comment("Modpack name shown in /modpack").define("Modpack name", "");
        modpackVersion = BUILDER.comment("Version of the modpack shown in /modpack").define("Modpack Version", "");

        discordURL = BUILDER.comment("Add a URL to link your discord, BBL Discord = https://discord.gg/benbenlaw")
                .define("Discord URL", "");

        horizonHeight = BUILDER.comment("Set the horizon height, default = 0.0, if in skyblock modpacks set to -64.0")
                .define("Horizon Height", 0.0);

        enabledVoidProtection = BUILDER.comment("If enabled, players will be teleported to their spawn point if they fall into the void, default = false")
                .define("Enable Void Protection", false);

        climbableBlockSpeed = BUILDER.comment("Speed that blocks in the bblcore:climbable_blocks tag can be climbed, default = 0.15")
                .define("Climbable Block Speed", 0.15);

        BUILDER.pop();

        //LAST
        SPEC = BUILDER.build();

    }
}
