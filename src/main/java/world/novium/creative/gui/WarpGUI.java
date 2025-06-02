package world.novium.creative.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import world.novium.creative.managers.WarpManager;
import world.novium.creative.utils.MessageUtils;

import java.util.Collection;

public class WarpGUI {

    /**
     * Builds and returns a GUI for displaying warps.
     * This GUI will show all warps managed by the provided WarpManager,
     * allowing players to click on an item to teleport to the corresponding warp location.
     *
     * @param player The player for whom the GUI is being built.
     * @param warpManager An instance of WarpManager to retrieve warp data.
     * @return A Gui object populated with warp items and a close button.
     */
    public static Gui buildGUI(Player player, WarpManager warpManager) {
        Gui gui = Gui.gui()
                .title(MessageUtils.parse("<gradient:#00FFFF:#007FFF>Warps</gradient>"))
                .rows(3)
                .disableAllInteractions()
                .create();

        Collection<WarpManager.Warp> warps = warpManager.getWarps();

        int slot = 0;

        for (WarpManager.Warp warp : warps) {
            if (slot >= (gui.getRows() - 1) * 9) {
                break;
            }

            // Create a GUI item for the current warp
            GuiItem warpItem = ItemBuilder.from(Material.ENDER_PEARL)
                    .name(MessageUtils.parse("<green>" + warp.name()))
                    .lore(
                            MessageUtils.parse("<gray>Welt: <white>" + warp.location().getWorld().getName()),
                            MessageUtils.parse("<gray>X: <white>" + warp.location().getBlockX()),
                            MessageUtils.parse("<gray>Y: <white>" + warp.location().getBlockY()),
                            MessageUtils.parse("<gray>Z: <white>" + warp.location().getBlockZ()),
                            MessageUtils.parse("<yellow>Klicke zum Teleportieren!")
                    )
                    .asGuiItem(event -> {
                        player.teleport(warp.location());
                        gui.close(player);
                        MessageUtils.send(player, "<green>Du wurdest zu Warp " + warp.name() + " teleportiert!");
                    });

            gui.setItem(slot, warpItem);
            slot++;
        }

        GuiItem closeButton = ItemBuilder.from(Material.BARRIER)
                .name(MessageUtils.parse("<red>Schließen</red>"))
                .asGuiItem(event -> gui.close(player));

        gui.setItem(gui.getRows() - 1, 4, closeButton);

        return gui;
    }
}
