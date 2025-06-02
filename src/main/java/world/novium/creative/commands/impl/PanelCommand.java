package world.novium.creative.commands.impl;

import dev.jorel.commandapi.CommandTree;
import dev.triumphteam.gui.guis.Gui;
import world.novium.creative.commands.Command;
import world.novium.creative.gui.PanelGUI;

public class PanelCommand implements Command {
    @Override
    public CommandTree build() {
        return new CommandTree("panel")
                .executesPlayer((player, args) -> {
                    Gui gui = PanelGUI.buildGUI(player);

                    gui.open(player);
                });
    }
}
