package world.novium.creative.commands.impl;

import dev.jorel.commandapi.CommandTree;
import dev.triumphteam.gui.guis.Gui;
import world.novium.creative.commands.Command;
import world.novium.creative.gui.WarpGUI;
import world.novium.creative.managers.WarpManager;

public class WarpsCommand implements Command {
    private final WarpManager manager;

    public WarpsCommand(WarpManager manager) {
        this.manager = manager;
    }

    @Override
    public CommandTree build() {
        return new CommandTree("warps")
                .executesPlayer((player, args) -> {
                    Gui gui = WarpGUI.buildGUI(player, manager);

                    gui.open(player);
                });
    }
}
