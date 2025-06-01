package world.novium.creative.modules.general.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.triumphteam.gui.guis.Gui;
import world.novium.creative.base.Command;
import world.novium.creative.modules.general.gui.PanelGUI;

public class PanelCommand implements Command {
    @Override
    public CommandAPICommand build() {
        return new CommandAPICommand("panel")
                .withPermission("novium.panel")
                .executesPlayer((player, args) -> {
                    Gui gui = PanelGUI.buildGUI(player);

                    gui.open(player);
                });
    }
}
