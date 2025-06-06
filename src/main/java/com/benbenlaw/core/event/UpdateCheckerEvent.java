package com.benbenlaw.core.event;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.config.CoreModpackConfig;
import com.benbenlaw.core.util.GitExcludedClass;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@EventBusSubscriber(modid = Core.MOD_ID, value = Dist.CLIENT)
public class UpdateCheckerEvent {

    private static final String PREFIX = "https://api.curseforge.com";
    private static final String apiKey = GitExcludedClass.a;
    private static final Path curseforgeMinecraftInstanceFileLocation = Path.of("minecraftinstance.json");
    private static final int projectID = CoreModpackConfig.projectID.get();

    @SubscribeEvent
    public static void onPlayerLoggingInEvent(ClientPlayerNetworkEvent.LoggingIn event) {
        Player player = event.getPlayer();

        if (CoreModpackConfig.updateChecker.get()) {

            // Step 1: Read the local instance file to get the installed modpack info
            if (Files.exists(curseforgeMinecraftInstanceFileLocation)) {
                try (Reader reader = Files.newBufferedReader(curseforgeMinecraftInstanceFileLocation)) {
                    JsonElement parsed = JsonParser.parseReader(reader);

                    if (!parsed.isJsonObject()) {
                        System.out.println("Instance file does not contain a valid JSON object.");
                        player.displayClientMessage(Component.translatable("chat.bblcore.modpack_invalid_instance").withStyle(ChatFormatting.RED), false);
                        return;
                    }

                    JsonObject dataObjectInstance = parsed.getAsJsonObject();

                    // Step 2: Check if installedModpack is null (indicating a dev environment)
                    JsonElement installedModpackElement = dataObjectInstance.get("installedModpack");

                    if (installedModpackElement.isJsonNull()) {
                        System.out.println("No modpack is installed (Dev Environment).");
                        player.displayClientMessage(Component.translatable("chat.bblcore.dev_environment").withStyle(ChatFormatting.YELLOW), false);
                        return; // Do not proceed with the update check, allow player to join
                    }

                    // Step 3: Proceed with the update check if installedModpack exists
                    JsonObject installedModpack = installedModpackElement.getAsJsonObject();

                    // Fetch the installed file's version
                    int currentVersion = 0;
                    if (installedModpack.has("latestFile") && installedModpack.getAsJsonObject("latestFile").has("id")) {
                        currentVersion = installedModpack.getAsJsonObject("latestFile").get("id").getAsInt();
                        System.out.println("Installed File ID from instance: " + currentVersion);
                    } else {
                        System.out.println("Could not find latestFile.id in instance file.");
                        player.displayClientMessage(Component.translatable("chat.bblcore.modpack_no_version").withStyle(ChatFormatting.RED), false);
                        return;
                    }

                    // Step 4: Fetch the latest version from the API
                    try {
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

                        // Read API Response
                        StringBuilder response = new StringBuilder();
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Parse API response
                        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                        JsonObject dataObject = jsonObject.getAsJsonObject("data");
                        JsonArray latestFilesIndexes = dataObject.getAsJsonArray("latestFilesIndexes");

                        // Modpack name and website URL from API
                        String modpackName = dataObject.get("name").getAsString();
                        URI uri = new URI (dataObject.getAsJsonObject("links").get("websiteUrl").getAsString());

                        // Get the latest file ID from the API
                        int latestFileId = 0;
                        if (latestFilesIndexes != null && !latestFilesIndexes.isEmpty()) {
                            JsonObject firstEntry = latestFilesIndexes.get(0).getAsJsonObject();
                            if (firstEntry.has("fileId")) {
                                latestFileId = firstEntry.get("fileId").getAsInt();
                                System.out.println("Latest file ID from API: " + latestFileId);
                            } else {
                                System.out.println("fileId not found in the first entry.");
                            }
                        } else {
                            System.out.println("latestFilesIndexes is missing or empty.");
                        }

                        // Step 5: Compare local version and API version
                        if (currentVersion == 0) {
                            player.displayClientMessage(Component.translatable("chat.bblcore.modpack_no_version").withStyle(ChatFormatting.RED), false);
                        } else if (currentVersion < latestFileId) {
                            player.displayClientMessage(
                                    Component.translatable("chat.bblcore.modpack_update", modpackName)
                                            .setStyle(Style.EMPTY
                                                    .withClickEvent(new ClickEvent.OpenUrl(uri))
                                                    .withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.bblcore.modpack_website")))
                                                    .withColor(net.minecraft.ChatFormatting.BLUE)
                                            ),
                                    false
                            );
                        } else {
                            player.displayClientMessage(Component.translatable("chat.bblcore.modpack_up_to_date").withStyle(ChatFormatting.GREEN), false);
                        }

                    } catch (IOException e) {
                        System.out.println("Error while connecting to the API: " + e.getMessage());
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }

                } catch (IOException e) {
                    System.out.println("Error reading the instance file: " + e.getMessage());
                    player.displayClientMessage(Component.translatable("chat.bblcore.modpack_instance_error").withStyle(ChatFormatting.RED), false);
                }
            } else {
                System.out.println("CurseForge instance file not found.");
                player.displayClientMessage(Component.translatable("chat.bblcore.modpack_no_instance").withStyle(ChatFormatting.RED), false);
            }
        }
    }




}


