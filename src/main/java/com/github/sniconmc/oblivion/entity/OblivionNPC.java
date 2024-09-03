package com.github.sniconmc.oblivion.entity;

import net.minestom.server.entity.Player;

import java.util.List;

public class OblivionNPC {

    private OblivionBody body;
    private List<OblivionText> texts;

    public OblivionNPC(OblivionBody body, List<OblivionText> texts) {
        this.body = body;
        this.texts = texts;
    }

    public void addViewer(Player player) {
        body.addViewer(player);

        texts.forEach(text -> text.addViewer(player));

    }

    public void removeViewer(Player player) {
        body.removeViewer(player);

        texts.forEach(text -> text.removeViewer(player));
    }

    public OblivionBody getBody() {
        return body;
    }

    public List<OblivionText> getTexts() {
        return texts;
    }
}
