package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.config.CoreStartupConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@EventBusSubscriber(modid = Core.MOD_ID)
public class UpdateCheckerEvent {

    private static final String PREFIX = "https://api.curseforge.com";
    private static final String apiKey = "$2a$10$Y64bw4w0RYpXpu9d9bEu7ulQSgP3MzXPm6rfmhEbqhnHf3oa8WOEq";

    @SubscribeEvent
    public static void onPlayerLoggingInEvent(ClientPlayerNetworkEvent.LoggingIn event) {

        Player player = event.getPlayer();

        if (player.getPersistentData().get("bblcore.modpack_version_id") == null) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(PREFIX + "/v1/mods/1193409").openConnection();
                connection.addRequestProperty("x-api-key", apiKey);
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() != 200) {
                    System.out.println("Unable to establish connection to API! Code " + connection.getResponseCode());
                    if (connection.getResponseCode() == 403) {
                        System.out.println("(Are you sure the key is valid?)");
                    }
                    return;
                }

                event.getPlayer().sendSystemMessage(Component.literal("Successfully connected to API!"));

                StringBuilder response = new StringBuilder();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();
                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                JsonObject dataObject = jsonObject.getAsJsonObject("data");
                JsonArray latestFilesIndexesArray = dataObject.getAsJsonArray("latestFilesIndexes");
                if (!latestFilesIndexesArray.isEmpty()) {
                    JsonObject firstFileIndex = latestFilesIndexesArray.get(0).getAsJsonObject();

                    int fileId = firstFileIndex.get("fileId").getAsInt();
                    player.getPersistentData().putString("bblcore.modpack_version_id", String.valueOf(fileId));

                } else {
                    System.out.println("No files found in the 'latestFilesIndexes' array.");
                }

                System.out.println("Response: " + response);
            } catch (IOException e) {
                System.out.println("Error while testing connection! " + e.getMessage());
            }

        } else {


            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(PREFIX + "/v1/mods/1193409").openConnection();
                connection.addRequestProperty("x-api-key", apiKey);
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() != 200) {
                    System.out.println("Unable to establish connection to API! Code " + connection.getResponseCode());
                    if (connection.getResponseCode() == 403) {
                        System.out.println("(Are you sure the key is valid?)");
                    }
                    return;
                }

                event.getPlayer().sendSystemMessage(Component.literal("Successfully connected to API!"));

                StringBuilder response = new StringBuilder();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();
                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                JsonObject dataObject = jsonObject.getAsJsonObject("data");
                JsonArray latestFilesIndexesArray = dataObject.getAsJsonArray("latestFilesIndexes");
                if (!latestFilesIndexesArray.isEmpty()) {
                    JsonObject firstFileIndex = latestFilesIndexesArray.get(0).getAsJsonObject();

                    int fileId = firstFileIndex.get("fileId").getAsInt();

                    System.out.println("File ID: " + fileId);
                    event.getPlayer().sendSystemMessage(Component.literal("Update Available"));
                    player.getPersistentData().putString("bblcore.modpack_version_id", String.valueOf(fileId));

                } else {
                    System.out.println("No files found in the 'latestFilesIndexes' array.");
                }

                System.out.println("Response: " + response);
            } catch (IOException e) {
                System.out.println("Error while testing connection! " + e.getMessage());
            }
        }
    }
}



