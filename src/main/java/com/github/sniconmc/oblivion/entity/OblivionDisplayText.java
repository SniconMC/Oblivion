package com.github.sniconmc.oblivion.entity;

import com.github.sniconmc.utils.text.TextUtils;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class OblivionDisplayText extends Entity {

    private String uuid;
    private Integer rowId;
    private int entityId;
    private Pos parentPos;
    private List<String> text;

    public OblivionDisplayText(@NotNull int rowId, @NotNull Pos position, @NotNull List<String> text) {
        super(EntityType.TEXT_DISPLAY);
        this.rowId = rowId;
        this.position = position.add(0,rowId*0.4,0);
        this.text = text;

        editEntityMeta(TextDisplayMeta.class, meta -> {
            meta.setHasNoGravity(true);
            meta.setText(TextUtils.convertStringToComponent(text));
            meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
        });
    }

    // Send packets to make the NPC visible only to a specific player
    public void makeVisibleTo(@NotNull Player player) {
        Map<Integer, Metadata.Entry<?>> entries = metadata.getEntries();

        // Send the SpawnEntityPacket only to this player
        player.sendPacket(getEntityType().registry().spawnType().getSpawnPacket(this));
        player.sendPacket(new EntityMetaDataPacket(this.getEntityId(), entries));
    }

    // Despawn the NPC for a specific player
    public void despawnForPlayer(@NotNull Player player) {
        player.sendPacket(new DestroyEntitiesPacket(getEntityId()));
        this.remove();
    }

}
