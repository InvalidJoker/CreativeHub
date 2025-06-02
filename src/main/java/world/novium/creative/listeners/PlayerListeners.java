package world.novium.creative.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import world.novium.creative.database.UserCache;
import world.novium.creative.utils.MessageUtils;

public class PlayerListeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(MessageUtils.parse("<gray>[<green>+</green>] <white>" + event.getPlayer().getName() + " has joined the server!"));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.quitMessage(MessageUtils.parse("<gray>[<red>-</red>] <white>" + event.getPlayer().getName() + " has left the server!"));
        UserCache.get().leave(event.getPlayer().getUniqueId());
    }

}
