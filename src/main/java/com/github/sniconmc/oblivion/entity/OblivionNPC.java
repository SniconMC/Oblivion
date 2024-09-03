package com.github.sniconmc.oblivion.entity;

import com.github.sniconmc.oblivion.config.OblivionConfig;
import com.github.sniconmc.oblivion.config.OblivionSkin;
import com.github.sniconmc.oblivion.entity.goals.LookAtPlayerGoal;
import com.github.sniconmc.utils.entity.EntityUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.metadata.EntityMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class OblivionNPC extends EntityCreature {

    private final String name;
    private final OblivionConfig config;

    private final PlayerSkin skin;
    private final boolean shouldLookAtPlayers;

    public OblivionNPC(@NotNull String name, @NotNull Pos position, @NotNull Instance instance, @NotNull OblivionConfig config, PlayerSkin skin) {
        super(EntityUtils.getEntityTypeFromNamespace(config.getEntity_type()));
        this.name = name;
        this.position = position;
        this.config = config;
        this.skin = skin;

        this.shouldLookAtPlayers = config.getPosition().shouldLookAtPlayer();

        editEntityMeta(EntityMeta.class, meta -> {
            meta.setHasNoGravity(true);
        });

        Team hiddenName = MinecraftServer.getTeamManager().getTeam("hidden_name");

        // Maybe remove setTeam?
        setTeam(hiddenName);
        hiddenName.addMember(name);

        // Add AI group with LookAtPlayerGoal
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new LookAtPlayerGoal(this, 5)) // Look at players within 5 blocks
                        .build()
        );
    }

    public boolean shouldLookAtPlayers() {
        return shouldLookAtPlayers;
    }

    // Send packets to make the NPC visible only to a specific player
    public void makeVisibleTo(@NotNull Player player) {
        Map<Integer, Metadata.Entry<?>> entries = metadata.getEntries();

        if (entityType == EntityType.PLAYER) {
            var properties = new ArrayList<PlayerInfoUpdatePacket.Property>();
            if (skin.textures() != null && skin.signature() != null) {
                properties.add(new PlayerInfoUpdatePacket.Property("textures", skin.textures(), skin.signature()));
            }
            var entry = new PlayerInfoUpdatePacket.Entry(this.getUuid(), name, properties, false,
                    0, GameMode.SURVIVAL, null, null);
            player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER, entry));
            player.sendPacket(getEntityType().registry().spawnType().getSpawnPacket(this));
            player.sendPacket(setSkinParts(this.config));
        } else {
            player.sendPacket(getEntityType().registry().spawnType().getSpawnPacket(this));
            player.sendPacket(new EntityMetaDataPacket(this.getEntityId(), entries));
        }
    }


    public EntityMetaDataPacket setSkinParts(OblivionConfig config) {
        byte skinPartsBitmask = buildSkinPartsBitmask(config.getSkin());

        return new EntityMetaDataPacket(this.getEntityId(), Map.of(
                17, Metadata.Byte(skinPartsBitmask)));
    }

    public byte buildSkinPartsBitmask(OblivionSkin skin) {
        byte bitmask = 0;

        if (skin.isCape()) bitmask |= 0x01;
        if (skin.isJacket()) bitmask |= 0x02;
        if (skin.isLeft_sleeve()) bitmask |= 0x04;
        if (skin.isRight_sleeve()) bitmask |= 0x08;
        if (skin.isLeft_pants()) bitmask |= 0x10;
        if (skin.isRight_pants()) bitmask |= 0x20;
        if (skin.isHat()) bitmask |= 0x40;

        return bitmask;
    }

    // Despawn the NPC for a specific player
    public void despawnForPlayer(@NotNull Player player) {
        if (this.entityType == EntityType.PLAYER){
            player.sendPacket(new PlayerInfoRemovePacket(this.getUuid()));
        }
        player.sendPacket(new DestroyEntitiesPacket(getEntityId()));
    }
}
