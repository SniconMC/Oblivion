package com.github.sniconmc.oblivion.utils;

import com.github.sniconmc.oblivion.config.OblivionConfig;
import com.github.sniconmc.oblivion.entity.OblivionDisplayText;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OblivionDisplayUtils {

    public static List<Entity> createTextEntities(Pos pos, OblivionConfig config, Player player) {

        List<Entity> entities = new ArrayList<>();
        int size = config.getName().size();
        for (int i = 0; i < size; i++) {
            OblivionDisplayText textDisplay = new OblivionDisplayText(i, pos,  config.getName().get((size-1)-i));
            textDisplay.makeVisibleTo(player);

            entities.add(textDisplay);
        }

        return entities;

    }
}
