package com.github.sniconmc.oblivion;


import com.github.sniconmc.oblivion.entity.OblivionNPC;
import com.github.sniconmc.oblivion.entity.OblivionText;
import com.github.sniconmc.oblivion.entity.OblivionBody;
import com.github.sniconmc.oblivion.utils.LoadOblivion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import java.io.File;
import java.util.*;

public class OblivionManager {

    private static Set<OblivionNPC> npcs = new HashSet<>();

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

    }

    public static void spawnOblivions(){
        OblivionMain.logger.debug("Spawning oblivions");
        for (String fileName : dataFileJSONData.keySet()) {

            OblivionBody npc = new OblivionBody(dataFileJSONData.get(fileName), fileName);

            List<OblivionText> texts = new ArrayList<>();

            npcs.add(new OblivionNPC(npc, texts));
        }

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