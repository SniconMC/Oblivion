package com.github.sniconmc.oblivion;

import com.github.sniconmc.oblivion.config.OblivionConfig;
import com.github.sniconmc.oblivion.config.OblivionSkin;
import com.github.sniconmc.oblivion.data.OblivionHolder;
import com.github.sniconmc.oblivion.entity.OblivionDisplayText;
import com.github.sniconmc.oblivion.entity.OblivionNPC;
import com.github.sniconmc.oblivion.instance.OblivionInstance;
import com.github.sniconmc.oblivion.utils.LoadOblivion;
import com.github.sniconmc.oblivion.utils.OblivionDisplayUtils;
import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;
import com.github.sniconmc.utils.skin.SkinUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OblivionManager {


    // Load new Gson
    private static Gson gson = new Gson();

    private static final File dataFolder = new File("resources/oblivion");

    private static Map<String, String> dataFileJSONData;

    private static Map<Player, List<OblivionHolder>> oblivionHolderMap = new HashMap<>();

    public OblivionManager() {

        gson = new GsonBuilder().setPrettyPrinting().create();

        dataFileJSONData = new LoadOblivion().load(dataFolder);

        createHiddenNameTeam();
    }

    public static void reloadOblivions() {
        dataFileJSONData = new LoadOblivion().load(dataFolder);

        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            despawnOblivions(player);
            spawnOblivions(player);
        }
    }

    public static void spawnOblivions(Player player) {

        List<OblivionHolder> holders = oblivionHolderMap.getOrDefault(player, new ArrayList<>());

        for (String fileName : dataFileJSONData.keySet()) {

            String placeholderReplacedJson = PlaceholderReplacer.replacePlaceholders(player, dataFileJSONData.get(fileName));

            try {

                // Parse the JSON string into an OblivionConfig object
                OblivionConfig config = gson.fromJson(placeholderReplacedJson, OblivionConfig.class);

                Instance instance = OblivionInstance.getInstanceMap().get(config.getWorld());
                if (instance == null) {
                    OblivionMain.logger.warn("Instance was null, resorting to player instance");
                    OblivionMain.logger.warn("This could break things, beware!");
                    instance = player.getInstance();
                }

                Pos pos = config.getPosition().getPositionWithPitchAndYaw();

                OblivionSkin skinData = config.getSkin();

                PlayerSkin skin = SkinUtils.getSkin(player, skinData.getPlayer(), "", skinData.getTexture(), skinData.getSignature());

                OblivionNPC npc = new OblivionNPC(fileName, pos, instance, config, skin);
                npc.makeVisibleTo(player);

                Pos displayPos = pos.add(0, npc.getEyeHeight() + 0.45, 0);

                OblivionHolder holder = new OblivionHolder(fileName, npc , OblivionDisplayUtils.createTextEntities(displayPos, config, player));

                holders.add(holder);
            } catch (JsonSyntaxException | JsonIOException e) {
                // Handle Gson-specific errors
                OblivionMain.logger.error("Error parsing JSON in:  {}", fileName);
            } catch (Exception e) {
                // Handle any other unexpected exceptions
                OblivionMain.logger.error("Unexpected error in: {}, {}", fileName, e.toString());
            }
        }

        oblivionHolderMap.put(player, holders);
    }

    public static void despawnOblivions(Player player) {
        List<OblivionHolder> holders = oblivionHolderMap.get(player);
        if (holders == null) {
            return;
        }

        for (OblivionHolder holder : holders) {
            OblivionNPC npc = holder.getNPC();
            npc.despawnForPlayer(player);

            List<Entity> entities = holder.getTexts();

            for (Entity entity : entities) {
                OblivionDisplayText text = (OblivionDisplayText) entity;
                text.despawnForPlayer(player);

            }
        }
    }


    private static void createHiddenNameTeam() {
        Team hiddenName = MinecraftServer.getTeamManager().createTeam("hidden_name");
        hiddenName.setNameTagVisibility(TeamsPacket.NameTagVisibility.NEVER);
    }
}