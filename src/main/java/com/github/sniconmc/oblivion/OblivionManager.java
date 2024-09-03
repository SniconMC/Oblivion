package com.github.sniconmc.oblivion;


import com.github.sniconmc.oblivion.entity.OblivionDisplayText;
import com.github.sniconmc.oblivion.entity.OblivionNPC;
import com.github.sniconmc.oblivion.utils.LoadOblivion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import java.io.File;
import java.util.Map;

public class OblivionManager {


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

        for (String fileName : dataFileJSONData.keySet()) {

            OblivionNPC npc = new OblivionNPC(dataFileJSONData.get(fileName), fileName);

            Pos namePos = npc.getPosition().add(0, npc.getEyeHeight()+ 0.45, 0);

            int size = npc.getText().size();
            for (int i = 0; i < size; i++) {
                 new OblivionDisplayText(i, namePos,  npc.getText().get((size-1)-i));
            }
        }

    }

    private static void createHiddenNameTeam() {
        Team hiddenName = MinecraftServer.getTeamManager().createTeam("hidden_name");
        hiddenName.setNameTagVisibility(TeamsPacket.NameTagVisibility.NEVER);
    }
}