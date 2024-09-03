package com.github.sniconmc.oblivion.entity;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import java.util.List;

public class OblivionNPC {

    private OblivionBody body;
    private List<OblivionText> texts;

    public OblivionNPC(OblivionBody body, List<OblivionText> texts) {
        this.body = body;
        this.texts = texts;
    }

    private void createTexts() {

        Pos namePos = body.getPosition().add(0, body.getEyeHeight()+ 0.45, 0);
        int size = body.getText().size();
        for (int i = 0; i < size; i++) {
            texts.add(new OblivionText(i, namePos,  body.getText().get((size-1)-i)));
        }
    }

    public void addViewer(Player player) {
        body.updateNewViewer(player);
        createTexts();

        texts.forEach(text -> text.updateNewViewer(player));

    }

    public void removeViewer(Player player) {
        body.updateOldViewer(player);

        texts.forEach(text -> text.updateOldViewer(player));
    }

    public OblivionBody getBody() {
        return body;
    }

    public List<OblivionText> getTexts() {
        return texts;
    }
}
