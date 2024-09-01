package com.github.sniconmc.oblivion.instance;

import net.minestom.server.instance.Instance;

import java.util.HashMap;
import java.util.Map;

public class OblivionInstance {

    private static Map<String, Instance> instanceMap = new HashMap<>();

    public static Map<String, Instance> getInstanceMap() {
        return instanceMap;
    }

    public static void setInstanceMap(Map<String, Instance> instanceMap) {
        OblivionInstance.instanceMap = instanceMap;
    }
}
