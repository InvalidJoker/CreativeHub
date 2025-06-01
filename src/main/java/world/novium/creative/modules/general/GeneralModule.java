package world.novium.creative.modules.general;

import world.novium.creative.base.Module;
import world.novium.creative.modules.general.commands.PanelCommand;

import java.util.List;

public class GeneralModule implements Module<GeneralModule> {

    @Override
    public void enable() {
        getLogger().info("General module enabled.");

        PanelCommand panel = new PanelCommand();

        registerCommands(List.of(panel));
    }

    @Override
    public void disable() {
        getLogger().info("General module disabled.");
        // Additional logic for disabling the general module can be added here
    }
}
