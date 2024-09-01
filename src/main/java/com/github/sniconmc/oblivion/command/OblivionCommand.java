package com.github.sniconmc.oblivion.command;

import com.github.sniconmc.oblivion.OblivionManager;
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

        var reloadArgument = ArgumentType.Literal("reload");

        addSyntax((commandSender, commandContext) -> {

            if (!(commandSender instanceof Player player)){
                return;
            }

            String reload = commandContext.get(reloadArgument);

            switch (reload) {
                case "reload", "r" -> {
                    OblivionManager.reloadOblivions();
                    commandSender.sendMessage(TextUtils.convertStringToComponent(List.of("<green>Reloaded Oblivion!</green>")));
                }
            }

        }, reloadArgument);

    }
}
