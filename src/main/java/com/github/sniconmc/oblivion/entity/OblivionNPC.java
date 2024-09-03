package com.github.sniconmc.oblivion.entity;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;

import java.util.*;

public class OblivionNPC {

    private OblivionBody body;
    private Map<Player, List<OblivionText>> playerTexts; // Use a Map to store text per player

    public OblivionNPC(OblivionBody body) {
        this.body = body;
        this.playerTexts = new HashMap<>(); // Initialize the map
    }

    private List<OblivionText> createTexts(Player player) {
        List<OblivionText> texts = new ArrayList<>();
        Pos namePos = body.getPosition().add(0, body.getEyeHeight() + 0.45, 0);
        int size = body.getText().size();
        for (int i = 0; i < size; i++) {
            texts.add(new OblivionText(i, namePos, body.getText().get((size - 1) - i)));
        }
        return texts;
    }

    public void addViewer(Player player) {
        body.updateNewViewer(player);
        List<OblivionText> texts = createTexts(player); // Create new texts for this player

        playerTexts.put(player, texts); // Store player-specific texts

        texts.forEach(text -> text.updateNewViewer(player));
    }

    public void removeViewer(Player player) {
        body.updateOldViewer(player);

        List<OblivionText> texts = playerTexts.remove(player); // Remove player-specific texts
        if (texts != null) {
            texts.forEach(text -> text.updateOldViewer(player));
            texts.forEach(Entity::remove); // Remove the entity
        }
    }

    public OblivionBody getBody() {
        return body;
    }

    public List<OblivionText> getTexts(Player player) {
        return playerTexts.getOrDefault(player, List.of());
    }

    public Set<Player> getPlayersWithTexts() {
        return playerTexts.keySet();
    }
}

