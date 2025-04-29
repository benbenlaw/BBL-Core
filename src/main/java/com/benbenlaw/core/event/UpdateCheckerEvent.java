package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.config.CoreModpackConfig;
import com.benbenlaw.core.util.GitExcludedClass;
import com.google.gson.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@EventBusSubscriber(modid = Core.MOD_ID)
public class UpdateCheckerEvent {

    private static final String PREFIX = "https://api.curseforge.com";
    private static final String apiKey = GitExcludedClass.a;
    private static final Path curseforgeMinecraftInstanceFileLocation = Path.of("minecraftinstance.json");
    private static final int projectID = CoreModpackConfig.projectID.get();

    @SubscribeEvent
    public static void onPlayerLoggingInEvent(ClientPlayerNetworkEvent.LoggingIn event) {
        Player player = event.getPlayer();

        if (CoreModpackConfig.updateChecker.get()) {

            try {
                // Fetch the latest file ID from the API
                HttpsURLConnection connection = (HttpsURLConnection) new URL(PREFIX + "/v1/mods/" + projectID).openConnection();
                connection.addRequestProperty("x-api-key", apiKey);
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() != 200) {
                    System.out.println("Unable to establish connection to API! Code " + connection.getResponseCode());
                    if (connection.getResponseCode() == 403) {
                        System.out.println("(Are you sure the key is valid?)");
                    }
                    return;
                }

                //Read the obtained data
                StringBuilder response = new StringBuilder();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonObject dataObject = jsonObject.getAsJsonObject("data");
                JsonArray latestFilesIndexes = dataObject.getAsJsonArray("latestFilesIndexes");

                //Modpack name and URL from the API
                String modpackName = dataObject.get("name").getAsString();
                String url = dataObject.getAsJsonObject("links").get("websiteUrl").getAsString();

                //Current version from instance
                int currentVersion = 0;
                //Latest version from api
                int latestFileId = 0;

                //Update latestFileId
                if (latestFilesIndexes != null && !latestFilesIndexes.isEmpty()) {
                    JsonObject firstEntry = latestFilesIndexes.get(0).getAsJsonObject();
                    if (firstEntry.has("fileId")) {
                        latestFileId = firstEntry.get("fileId").getAsInt();
                        System.out.println("Latest file ID: " + latestFileId);
                    } else {
                        System.out.println("fileId not found in the first entry.");
                    }
                } else {
                    System.out.println("latestFilesIndexes is missing or empty.");
                }

                //Update currentVersion
                if (Files.exists(curseforgeMinecraftInstanceFileLocation)) {
                    System.out.println("Curse Forge Version file exists. Checking for updates...");
                    try (Reader reader = Files.newBufferedReader(curseforgeMinecraftInstanceFileLocation)) {
                        JsonObject dataObjectInstance = JsonParser.parseReader(reader).getAsJsonObject();

                        if (dataObjectInstance.has("installedModpack")) {
                            JsonObject installedModpack = dataObjectInstance.getAsJsonObject("installedModpack");

                            if (installedModpack.has("installedFile")) {
                                JsonObject installedFile = installedModpack.getAsJsonObject("installedFile");
                                if (installedFile.has("id")) {
                                    currentVersion = installedFile.get("id").getAsInt();
                                    System.out.println("Installed File ID: " + currentVersion);
                                } else {
                                    System.out.println("ID not found in installedFile.");
                                }
                            } else {
                                System.out.println("installedFile not found in installedModpack.");
                            }
                        } else {
                            System.out.println("installedModpack not found in JSON.");
                        }
                    } catch (IOException e) {
                        System.out.println("No curseforge instance file found!");
                    }
                }

                //Inform the player about the update
                if (currentVersion < latestFileId) {
                    player.sendSystemMessage(Component.translatable("chat.bblcore.modpack_update", modpackName)
                            .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.bblcore.modpack_website"))))
                            .withStyle(ChatFormatting.BLUE));
                } else {
                    player.sendSystemMessage(Component.translatable("chat.bblcore.modpack_up_date").withStyle(ChatFormatting.GREEN));
                }

                System.out.println("");

            } catch (IOException e) {
                System.out.println("Error while testing connection! " + e.getMessage());
            }
        }
    }
}


