package com.github.sniconmc.oblivion.entity;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OblivionNPC {

    private String name;
    private String config;
    private EntityType entityType;

    private OblivionBody body;
    private List<OblivionText> texts = new ArrayList<>(); // Initialize the map; // Use a Map to store text per player
    private boolean has_text = false;
    private boolean has_body = false;

    private OblivionBody createBody() {
        OblivionBody npc = new OblivionBody(config, name, entityType);
        has_body = true;
        return npc;
    }

    private List<OblivionText> createTexts() {
        List<OblivionText> texts = new ArrayList<>();
        Pos namePos = body.getPosition().add(0, body.getEyeHeight() + 0.45, 0);
        int size = body.getText().size();
        for (int i = 0; i < size; i++) {
            texts.add(new OblivionText(i, namePos, body.getText().get((size - 1) - i)));
        }
        has_text = true;
        return texts;
    }

    public void addViewer(Player player) {
        if (!has_body) {
            body = createBody();
        }
        body.setConfig(config);
        body.updateNewViewer(player);

        if (!has_text) {
            texts = createTexts(); // Create new texts for this player
        }

        texts.forEach(text -> {
            text.setText(body.getText().get(body.getText().size() - text.getId() - 1));
            text.updatePos(body.getPosition().add(0,body.getEyeHeight() + 0.45,0));

            text.updateNewViewer(player);
        });
    }

    public void removeViewer(Player player) {
        body.updateOldViewer(player);

        if (texts != null) {
            texts.forEach(text -> text.updateOldViewer(player));
        }
    }

    public OblivionBody getBody() {
        return body;
    }

    public List<OblivionText> getTexts(Player player) {
        return texts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }


}

