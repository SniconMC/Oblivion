package com.github.sniconmc.oblivion.data;

import com.github.sniconmc.oblivion.entity.OblivionDisplayText;
import com.github.sniconmc.oblivion.entity.OblivionNPC;
import net.minestom.server.entity.Entity;

import java.util.List;

public class OblivionHolder {

    private final String filename;
    private final OblivionNPC npc;
    private final List<Entity> texts;


    public OblivionHolder(String filename, OblivionNPC npc, List<Entity> texts) {
        this.filename = filename;
        this.npc = npc;
        this.texts = texts;
    }

    public String getFilename() {
        return filename;
    }

    public OblivionNPC getNPC() {
        return npc;
    }

    public List<Entity> getTexts() {
        return texts;
    }
}
