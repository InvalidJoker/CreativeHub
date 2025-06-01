package world.novium.creative;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.triumphteam.gui.TriumphGui;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import world.novium.creative.base.Module;
import world.novium.creative.modules.general.GeneralModule;
import world.novium.creative.modules.world.WorldModule;

import java.util.List;

public class CreativePlugin extends JavaPlugin {
    private static CreativePlugin instance;

    private final List<@NotNull Module<?>> modules = List.of(
            new GeneralModule(),
            new WorldModule()
    );

    public static CreativePlugin getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();

        TriumphGui.init(this);

        // Register modules
        modules.forEach(module -> {
            module.enable();
            getLogger().info("Module " + module.getClass().getSimpleName() + " has been enabled.");
        });

        getLogger().info("CreativePlugin has been enabled!");


    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();

        // Disable modules
        modules.forEach(module -> {
            module.disable();
            getLogger().info("Module " + module.getClass().getSimpleName() + " has been disabled.");
        });
        // Plugin shutdown logic
        getLogger().info("CreativePlugin has been disabled!");
    }
}
