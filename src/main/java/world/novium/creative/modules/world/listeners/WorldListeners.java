package world.novium.creative.modules.world.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import world.novium.creative.modules.world.WorldManager;

public class WorldListeners implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        World world = event.getFrom();

        // get players on world
        int playerCount = world.getPlayers().size();

        if (playerCount == 0 && WorldManager.getLoadedWorld(world.getName()) != null) {
            WorldManager.unloadWorld(event.getPlayer(), true);
        }
    }
}
