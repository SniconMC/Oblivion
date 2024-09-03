package com.github.sniconmc.oblivion.entity;

import com.github.sniconmc.oblivion.OblivionMain;
import com.github.sniconmc.oblivion.config.OblivionConfig;
import com.github.sniconmc.oblivion.config.OblivionSkin;
import com.github.sniconmc.oblivion.entity.goals.LookAtPlayerGoal;
import com.github.sniconmc.oblivion.instance.OblivionInstance;
import com.github.sniconmc.utils.entity.EntityUtils;
import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;
import com.github.sniconmc.utils.skin.SkinUtils;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.metadata.EntityMeta;
import net.minestom.server.entity.metadata.PlayerMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OblivionBody extends EntityCreature {

    private final String name;
    private final String config;

    private PlayerSkin skin;
    private List<List<String>> text;
    private boolean shouldLookAtPlayers;

    public OblivionBody(@NotNull String config, String name, EntityType entityType) {
        super(entityType);

        this.name = name;
        this.text = new ArrayList<>();
        this.config = config;
        this.shouldLookAtPlayers = true;

        editEntityMeta(EntityMeta.class, meta -> {
            meta.setHasNoGravity(true);
        });

        Team hiddenName = MinecraftServer.getTeamManager().getTeam("hidden_name");

        hiddenName.addMember(name);
        // Add AI group with LookAtPlayerGoal
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new LookAtPlayerGoal(this, 5)) // Look at players within 5 blocks
                        .build()
        );
    }

    public List<List<String>> getText() {
        return text;
    }

    public boolean shouldLookAtPlayers() {
        return shouldLookAtPlayers;
    }

    private boolean isPlayer() {
        return entityType == EntityType.PLAYER;
    }

    public void setData(OblivionConfig config, Player player) {

        OblivionSkin configSkin = config.getSkin();

        Instance instance = OblivionInstance.getInstanceMap().get(config.getWorld());

        this.text = config.getName();
        this.skin = SkinUtils.getSkin(player, config.getSkin().getPlayer(), "", config.getSkin().getTexture(), config.getSkin().getSignature());
        this.shouldLookAtPlayers = config.getPosition().shouldLookAtPlayer();

        setInstance(instance == null ? player.getInstance() : instance, config.getPosition().getPositionWithPitchAndYaw());

        if (isPlayer()){
            editEntityMeta(PlayerMeta.class, meta -> {
                meta.setCapeEnabled(configSkin.isCape());
                meta.setJacketEnabled(configSkin.isJacket());
                meta.setLeftSleeveEnabled(configSkin.isLeft_sleeve());
                meta.setRightSleeveEnabled(configSkin.isRight_sleeve());
                meta.setLeftLegEnabled(configSkin.isLeft_pants());
                meta.setRightLegEnabled(configSkin.isRight_pants());
                meta.setHatEnabled(configSkin.isHat());
            });
        }

    }




    @Override
    public void updateNewViewer(@NotNull Player player) {

        OblivionMain.logger.debug("Updating NPC viewer for player {}", player.getUsername());
        String placeholderReplacedJson = PlaceholderReplacer.replacePlaceholders(player, config);

        try {

            OblivionConfig config = new Gson().fromJson(placeholderReplacedJson, OblivionConfig.class);

            setData(config, player);

        } catch (JsonSyntaxException | JsonIOException e) {
            // Handle Gson-specific errors
            OblivionMain.logger.error("Error parsing JSON in:  {}", name);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            OblivionMain.logger.error("Unexpected error in: {}, {}", name, e.toString());
        }


        if (entityType == EntityType.PLAYER) {
            // provide skin data
            List<PlayerInfoUpdatePacket.Property> properties;
            if (skin != null) properties = List.of(new PlayerInfoUpdatePacket.Property("textures", skin.textures(), skin.signature()));
            else properties = List.of();

            PlayerInfoUpdatePacket.Entry entry = new PlayerInfoUpdatePacket.Entry(
                    this.getUuid(), name,
                    properties, false,
                    0, GameMode.SURVIVAL,
                    null, null
            );

            // send tab list entry
            player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER, entry));
        }


        // spawn entity
        super.updateNewViewer(player);
    }

    @Override
    public void updateOldViewer(@NotNull Player player) {
        // despawn entity
        super.updateOldViewer(player);

        // delete tab list entry
        player.sendPacket(new PlayerInfoRemovePacket(this.getUuid()));
    }
}
