package world.novium.creative;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.triumphteam.gui.TriumphGui;
import org.bukkit.plugin.java.JavaPlugin;
import world.novium.creative.backend.BackendServer;
import world.novium.creative.commands.Command;
import world.novium.creative.commands.impl.PanelCommand;
import world.novium.creative.commands.impl.WarpCommand;
import world.novium.creative.commands.impl.WarpsCommand;
import world.novium.creative.managers.WarpManager;

import java.util.List;

public class CreativePlugin extends JavaPlugin {
    private static CreativePlugin instance;

    private BackendServer server;
    private Thread serverThread;

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

        saveDefaultConfig();

        CommandAPI.onEnable();

        TriumphGui.init(this);

        WarpManager warpManager = new WarpManager(this);

        registerCommands(warpManager);

        var backendConfig = getConfig().getConfigurationSection("backend");
        boolean backendEnabled = backendConfig != null && backendConfig.getBoolean("enabled", false);

        if (backendEnabled) {
            String authToken = backendConfig.getString("authToken", "auth-token");
            if (authToken.isEmpty()) {
                getLogger().severe("Backend server is enabled but no auth token is provided in the config.");
                return;
            }

            int port = backendConfig.getInt("port", 8080);

            server = new BackendServer(port, authToken);
            serverThread = new Thread(server);

            serverThread.start();

            getLogger().info("Backend server started on port " + port + " with auth token: " + authToken);
        }


        getLogger().info("CreativePlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();

        if (server != null) {
            server.stop();
            if (serverThread != null && serverThread.isAlive()) {
                try {
                    serverThread.join();
                } catch (InterruptedException e) {
                    getLogger().severe("Failed to stop the backend server thread: " + e.getMessage());
                }
            }
        }
        // Plugin shutdown logic
        getLogger().info("CreativePlugin has been disabled!");
    }

    public void registerCommands(WarpManager warpManager) {
        List<Command> commands = List.of(
                new PanelCommand(),
                new WarpCommand(warpManager),
                new WarpsCommand(warpManager)
        );

        commands.forEach(command -> command.build().register());
    }
}
