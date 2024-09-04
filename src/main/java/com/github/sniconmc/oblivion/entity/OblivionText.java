package com.github.sniconmc.oblivion.entity;

import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;
import com.github.sniconmc.utils.text.TextUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.EntityPositionPacket;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OblivionText extends Entity {

    private List<String> text;
    private int id;
    private final Set<Player> viewers = new HashSet<>();
    private Pos newPosition;

    public OblivionText(@NotNull int rowId, @NotNull Pos position, @NotNull List<String> text) {
        super(EntityType.TEXT_DISPLAY);
        this.id = rowId;
        this.position = position;
        this.newPosition = position;
        this.text = text;
        editEntityMeta(TextDisplayMeta.class, meta -> {
            meta.setHasNoGravity(true);
            meta.setText(Component.text("Default Text"));
            meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
        });
    }


    @Override
    public void updateNewViewer(@NotNull Player player) {
        editEntityMeta(TextDisplayMeta.class, meta -> meta.setText(TextUtils.convertStringToComponent(List.of(PlaceholderReplacer.replacePlaceholders(player, text.getFirst())))));

        Map<Integer, Metadata.Entry<?>> entries = metadata.getEntries();

        if (!this.viewers.contains(player)) {
            // spawn entity
            super.updateNewViewer(player);
            this.viewers.add(player);
        }
        // Send the SpawnEntityPacket only to this player
        player.sendPacket(new EntityMetaDataPacket(this.getEntityId(), entries));


        player.sendPacket(EntityPositionPacket.getPacket(this.getEntityId(), this.newPosition ,this.position, false));
        this.position = this.newPosition;

    }

    @Override
    public void updateOldViewer(@NotNull Player player) {
        editEntityMeta(TextDisplayMeta.class, meta -> meta.setText(TextUtils.convertStringToComponent(List.of(PlaceholderReplacer.replacePlaceholders(player, text.getFirst())))));

        Map<Integer, Metadata.Entry<?>> entries = metadata.getEntries();

        // Send the SpawnEntityPacket only to this player
        player.sendPacket(new EntityMetaDataPacket(this.getEntityId(), entries));
        this.viewers.remove(player);
        super.updateOldViewer(player);
    }

    public void updatePos(Pos pos){
        this.newPosition = pos.add(0, (id*0.4) , 0);
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setNewPosition(Pos previosPos) {
        this.newPosition = previosPos;
    }

    public Pos getNewPosition() {
        return newPosition;
    }
}
