package com.github.sniconmc.oblivion.config;

import java.util.List;

public class OblivionConfig {

    private List<List<String>> name;
    private String uuid;
    private String entity_type;

    private OblivionPosition position;
    private OblivionSkin skin;
    private OblivionData data;

    private boolean in_tab;
    private String world;

    public List<List<String>> getName() {
        return name;
    }

    public boolean isIn_tab() {
        return in_tab;
    }

    public OblivionSkin getSkin() {
        return skin;
    }

    public OblivionPosition getPosition() {
        return position;
    }

    public OblivionData getData() {
        return data;
    }

    public String getEntity_type() {
        return entity_type;
    }

    public String getUuid() {
        return uuid;
    }

    public String getWorld(){
        return world;
    }
}
