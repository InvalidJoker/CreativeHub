package world.novium.creative.modules.general.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import world.novium.creative.modules.world.PlotManager;
import world.novium.creative.modules.world.WorldManager;
import world.novium.creative.utils.MessageUtils;

public class PanelGUI {
    public static Gui buildGUI(Player player) {
        Gui gui = Gui.gui()
                .title(MessageUtils.parse("<gradient:#ff0000:#ff9900>Creative Hub</gradient>"))
                .rows(2)
                .disableAllInteractions()
                .create();

        GuiItem createWorld = ItemBuilder.from(Material.RED_SAND)
                .name(MessageUtils.parse("<green>Welt erstellen"))
                .lore(MessageUtils.parse(
                        "<gray>Erstelle eine neue Welt, um deine Bauprojekte zu starten."
                ))
                .asGuiItem(event -> {
                    if (WorldManager.worldExists(WorldManager.getWorldName(player))) {
                        gui.close(player);
                        player.sendMessage(MessageUtils.parse("<red>Du hast bereits eine Welt erstellt!"));
                        return;
                    }

                    WorldManager.createWorld(player);
                    player.sendMessage(MessageUtils.parse("<green>Welt erfolgreich erstellt!"));
                });

        GuiItem tpWorld = ItemBuilder.from(Material.ENDER_PEARL)
                .name(MessageUtils.parse("<green>Zur Welt teleportieren"))
                .asGuiItem(event -> {
                    if (!WorldManager.worldExists(WorldManager.getWorldName(player))) {
                        gui.close(player);
                        player.sendMessage(MessageUtils.parse("<red>Du hast noch keine Welt erstellt!"));
                        return;
                    }

                    WorldManager.loadWorld(player);
                    player.sendMessage(MessageUtils.parse("<green>Teleportiere dich zur Welt..."));
                    player.teleport(WorldManager.getLoadedWorld(player).getSpawnLocation());
                });
        GuiItem deleteWorld = ItemBuilder.from(Material.TNT)
                .name(MessageUtils.parse("<red>Welt löschen"))
                .asGuiItem(event -> {
                    if (!WorldManager.worldExists(WorldManager.getWorldName(player))) {
                        gui.close(player);
                        player.sendMessage(MessageUtils.parse("<red>Du hast noch keine Welt erstellt!"));
                        return;
                    }

                    boolean success = WorldManager.deleteWorld(player);
                    if (success) {
                        player.sendMessage(MessageUtils.parse("<green>Welt erfolgreich gelöscht!"));
                    } else {
                        player.sendMessage(MessageUtils.parse("<red>Fehler beim Löschen der Welt!"));
                    }
                });

        GuiItem createPlot = ItemBuilder.from(Material.GRASS_BLOCK)
                .name(MessageUtils.parse("<green>Grundstück erstellen"))
                .lore(MessageUtils.parse(
                        "<gray>Erstelle ein neues Grundstück, um deine Bauprojekte zu starten."
                ))
                .asGuiItem(event -> {
                    PlotManager plotManager = PlotManager.getInstance();

                    if (plotManager.getPlots(player).isEmpty()) {
                        plotManager.claimRandomPlot(player).ifPresent(plot -> {
                            player.sendMessage(MessageUtils.parse("<green>Grundstück erfolgreich erstellt!"));
                        });
                    } else {
                        player.sendMessage(MessageUtils.parse("<red>Du hast bereits ein Grundstück!"));
                    }
                });

        GuiItem tpPlot = ItemBuilder.from(Material.COMPASS)
                .name(MessageUtils.parse("<green>Zum Grundstück teleportieren"))
                .asGuiItem(event -> {
                    PlotManager plotManager = PlotManager.getInstance();

                    if (plotManager.getPlots(player).isEmpty()) {
                        gui.close(player);
                        player.sendMessage(MessageUtils.parse("<red>Du hast noch kein Grundstück erstellt!"));
                        return;
                    }

                    player.performCommand("p home");
                });

        GuiItem deletePlot = ItemBuilder.from(Material.BARRIER)
                .name(MessageUtils.parse("<red>Grundstück löschen"))
                .asGuiItem(event -> {

                    PlotManager plotManager = PlotManager.getInstance();

                    if (plotManager.getPlots(player).isEmpty()) {
                        gui.close(player);
                        player.sendMessage(MessageUtils.parse("<red>Du hast noch kein Grundstück erstellt!"));
                        return;
                    }

                    player.performCommand("p delete confirm");
                });

        gui.setItem(1, 4, createWorld);
        gui.setItem(1, 5, tpWorld);
        gui.setItem(1, 6, deleteWorld);
        gui.setItem(2, 4, createPlot);
        gui.setItem(2, 5, tpPlot);
        gui.setItem(2, 6, deletePlot);
        gui.setItem(1, 1, ItemBuilder.from(Material.BARRIER)
                .name(MessageUtils.parse("<red>Schließen"))
                .asGuiItem(event -> {
                    gui.close(player);
                }));

        return gui;
    }
}
