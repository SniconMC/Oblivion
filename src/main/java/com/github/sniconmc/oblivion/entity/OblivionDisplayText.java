package com.github.sniconmc.oblivion.entity;

import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;
import com.github.sniconmc.utils.text.TextUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class OblivionDisplayText extends Entity {

    private final List<String> text;

    public OblivionDisplayText(@NotNull int rowId, @NotNull Pos position, @NotNull List<String> text) {
        super(EntityType.TEXT_DISPLAY);
        this.position = position.add(0,rowId*0.4,0);
        this.text = text;
        editEntityMeta(TextDisplayMeta.class, meta -> {
            meta.setHasNoGravity(true);
            meta.setText(Component.empty());
            meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
        });
    }

    // Despawn the NPC for a specific player
    public void despawnForPlayer(@NotNull Player player) {
        player.sendPacket(new DestroyEntitiesPacket(getEntityId()));
        this.remove();
    }


    @Override
    public void updateNewViewer(@NotNull Player player) {

        editEntityMeta(TextDisplayMeta.class, meta -> meta.setText(TextUtils.convertStringToComponent(List.of(PlaceholderReplacer.replacePlaceholders(player, text.toString())))));

        Map<Integer, Metadata.Entry<?>> entries = metadata.getEntries();

        // spawn entity
        super.updateNewViewer(player);

        // Send the SpawnEntityPacket only to this player
        player.sendPacket(new EntityMetaDataPacket(this.getEntityId(), entries));
    }
}
