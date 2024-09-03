package com.github.sniconmc.oblivion;

import com.github.sniconmc.oblivion.command.OblivionCommand;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OblivionMain {

    public static final Logger logger = LoggerFactory.getLogger(OblivionMain.class);

    public static void init() {
        OblivionMain.logger.info("Oblivion initialized");

        new OblivionManager();

        MinecraftServer.getCommandManager().register(new OblivionCommand());
    }
}