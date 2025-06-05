package world.novium.creative.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import world.novium.creative.managers.WorldManager;

public class WorldListeners implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        World world = event.getFrom();

        int playerCount = world.getPlayers().size();

        if (playerCount == 0 && WorldManager.getLoadedWorld(world.getName()) != null) {
            WorldManager.unloadWorld(event.getPlayer(), true);
        }
    }
}
