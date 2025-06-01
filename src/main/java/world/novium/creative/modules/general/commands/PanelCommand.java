package world.novium.creative.modules.general.commands;

import dev.jorel.commandapi.CommandAPICommand;
import world.novium.creative.base.Command;

public class PanelCommand implements Command {
    @Override
    public CommandAPICommand build() {
        return new CommandAPICommand("panel")
                .withPermission("novium.panel")
                .executesPlayer((player, args) -> {
                    // Logic to open the panel for the player
                    player.sendMessage("Opening panel...");
                    // Here you would typically open a GUI or perform some action
                });
    }
}
