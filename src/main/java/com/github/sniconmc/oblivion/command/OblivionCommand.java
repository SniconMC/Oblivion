package com.github.sniconmc.oblivion.command;

import com.github.sniconmc.oblivion.OblivionManager;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import com.github.sniconmc.utils.text.TextUtils;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;


import java.util.List;

public class OblivionCommand extends Command {

    public OblivionCommand() {
        super("oblivion");
        setDefaultExecutor((sender, context) -> {
        });

        var commandArgument = ArgumentType.Enum("command", OblivionCommandEnum.class);

        addSyntax((commandSender, commandContext) -> {

            if (!(commandSender instanceof Player player)){
                return;
            }

            String reload = String.valueOf(commandContext.get(commandArgument));

            switch (reload) {
                case "reload", "r" -> {
                    OblivionManager.reloadOblivions();
                    commandSender.sendMessage(TextUtils.convertStringToComponent(List.of("<green>Reloaded Oblivion!</green>")));
                }
                case "hide" -> {
                    OblivionManager.removeViewerToAllNpcs(player);
                    commandSender.sendMessage(TextUtils.convertStringToComponent(List.of("<yellow>Oblivions went into hiding!</yellow>")));
                }
                case "show" -> {
                    OblivionManager.addViewerToAllNpcs(player);
                    commandSender.sendMessage(TextUtils.convertStringToComponent(List.of("<green>Wild Oblivions appeared!</green>")));
                }
            }

        }, commandArgument);

    }
}
