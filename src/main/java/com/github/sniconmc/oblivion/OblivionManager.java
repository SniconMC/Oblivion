package com.github.sniconmc.oblivion;


import com.github.sniconmc.oblivion.config.OblivionConfig;
import com.github.sniconmc.oblivion.entity.OblivionNPC;
import com.github.sniconmc.oblivion.entity.OblivionText;
import com.github.sniconmc.oblivion.entity.OblivionBody;
import com.github.sniconmc.oblivion.utils.LoadOblivion;
import com.github.sniconmc.utils.entity.EntityUtils;
import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import java.io.File;
import java.util.*;

public class OblivionManager {

    private static Set<OblivionNPC> npcs;

    // Load new Gson
    private static Gson gson = new Gson();

    private static final File dataFolder = new File("resources/oblivion");

    private static Map<String, String> dataFileJSONData;

    public OblivionManager() {

        gson = new GsonBuilder().setPrettyPrinting().create();

        dataFileJSONData = new LoadOblivion().load(dataFolder);

        createHiddenNameTeam();
        spawnOblivions();
    }

    public static void reloadOblivions() {
        dataFileJSONData = new LoadOblivion().load(dataFolder);

        despawnOblivions();
        spawnOblivions();

        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            removeViewerToAllNpcs(player);
            addViewerToAllNpcs(player);
        }

    }

    public static void spawnOblivions(){

        Set<OblivionNPC> setOfNPC = new HashSet<>();

        for (String fileName : dataFileJSONData.keySet()) {

            try {

                OblivionConfig config = new Gson().fromJson(dataFileJSONData.get(fileName), OblivionConfig.class);

                EntityType entityType = EntityUtils.getEntityTypeFromNamespace(config.getEntity_type());

                if (entityType == null) {
                    continue;
                }

                OblivionBody npc = new OblivionBody(dataFileJSONData.get(fileName), fileName, entityType);

                List<OblivionText> texts = new ArrayList<>();

                setOfNPC.add(new OblivionNPC(npc, texts));

            } catch (JsonSyntaxException | JsonIOException e) {
                // Handle Gson-specific errors
                OblivionMain.logger.error("Error parsing JSON in:  {}", fileName);
            } catch (Exception e) {
                // Handle any other unexpected exceptions
                OblivionMain.logger.error("Unexpected error in: {}, {}", fileName, e.toString());
            }
        }
        npcs = setOfNPC;
    }

    public static void despawnOblivions(){

        npcs.forEach(oblivionNPC -> {
            oblivionNPC.getBody().remove();
            oblivionNPC.getTexts().forEach(oblivionText -> {

            });
        });
    }

    public static void addViewerToAllNpcs(Player player) {

        for (OblivionNPC npc : npcs) {
            npc.addViewer(player);
        }
    }

    public static void removeViewerToAllNpcs(Player player) {

        for (OblivionNPC npc : npcs) {
            npc.removeViewer(player);
        }

    }
    public static Set<OblivionNPC> getNpcs(){
        return npcs;
    }

    private static void createHiddenNameTeam() {
        Team hiddenName = MinecraftServer.getTeamManager().createTeam("hidden_name");
        hiddenName.setNameTagVisibility(TeamsPacket.NameTagVisibility.NEVER);
    }
}