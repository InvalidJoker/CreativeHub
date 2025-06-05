package world.novium.creative.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import world.novium.creative.managers.WorldManager;
import world.novium.creative.utils.MessageUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SnapshotGUI {

    private static final int SNAPSHOTS_PER_PAGE = 21;

    public static Gui buildSnapshotGUI(Player player, int page) {
        Gui gui = Gui.gui()
                .title(MessageUtils.parse("<gradient:#00ff00:#00ffff>Welt-Snapshots</gradient>"))
                .rows(6)
                .disableAllInteractions()
                .create();

        // Check if player has a world
        if (!WorldManager.worldExists(WorldManager.getWorldName(player))) {
            gui.setItem(3, 5, ItemBuilder.from(Material.BARRIER)
                    .name(MessageUtils.parse("<red>Keine Welt gefunden"))
                    .lore(MessageUtils.parse("<gray>Du musst zuerst eine Welt erstellen!"))
                    .asGuiItem());

            addNavigationItems(gui, player, page, new String[0]);
            return gui;
        }

        // Get snapshots
        String[] snapshots = WorldManager.listSnapshots(player.getUniqueId());

        if (snapshots.length == 0) {
            gui.setItem(3, 5, ItemBuilder.from(Material.PAPER)
                    .name(MessageUtils.parse("<yellow>Keine Snapshots vorhanden"))
                    .lore(MessageUtils.parse("<gray>Erstelle deinen ersten Snapshot!"))
                    .asGuiItem());
        } else {
            populateSnapshots(gui, player, snapshots, page);
        }

        // Add create snapshot button
        gui.setItem(6, 2, ItemBuilder.from(Material.ARROW)
                .name(MessageUtils.parse("<green>Neuen Snapshot erstellen"))
                .lore(MessageUtils.parse(
                        "<gray>Erstelle einen Snapshot deiner aktuellen Welt."
                ), MessageUtils.parse("<yellow>Klicke, um einen automatischen Snapshot zu erstellen.")
                )
                .asGuiItem(event -> {
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                    String snapshotName = "snapshot_" + timestamp;

                    gui.close(player);
                    player.sendMessage(MessageUtils.parse("<yellow>Erstelle Snapshot..."));

                    if (WorldManager.createSnapshot(player.getUniqueId(), snapshotName)) {
                        player.sendMessage(MessageUtils.parse("<green>Snapshot erfolgreich erstellt: " + snapshotName));
                    } else {
                        player.sendMessage(MessageUtils.parse("<red>Fehler beim Erstellen des Snapshots!"));
                    }
                }));

        addNavigationItems(gui, player, page, snapshots);

        return gui;
    }

    private static void populateSnapshots(Gui gui, Player player, String[] snapshots, int page) {
        int startIndex = page * SNAPSHOTS_PER_PAGE;
        int endIndex = Math.min(startIndex + SNAPSHOTS_PER_PAGE, snapshots.length);

        int slot = 10; // Start at row 2, column 2 (1-indexed)
        int itemsInRow = 0;

        for (int i = startIndex; i < endIndex; i++) {
            String snapshot = snapshots[i];
            String displayName = snapshot.replace(".zip", "");

            // Skip to next row if we've placed 7 items in current row
            if (itemsInRow >= 7) {
                slot += 2; // Skip the edge columns
                itemsInRow = 0;
            }

            List<String> lore = new ArrayList<>();
            lore.add("<gray>Snapshot: <white>" + displayName);
            lore.add("");
            lore.add("<green>Linksklick: <white>Snapshot laden");
            lore.add("<red>Rechtsklick: <white>Snapshot löschen");
            lore.add("");
            lore.add("<yellow>⚠ Warnung: Das Laden überschreibt deine aktuelle Welt!");

            List<Component> loreComponents = new ArrayList<>();

            for (String line : lore) {
                loreComponents.add(MessageUtils.parse(line));
            }



            GuiItem snapshotItem = ItemBuilder.from(Material.FILLED_MAP)
                    .name(MessageUtils.parse("<aqua>" + displayName))
                    .lore(loreComponents.toArray(new Component[0]))
                    .asGuiItem(event -> {
                        if (event.getClick() == ClickType.LEFT) {
                            // Load snapshot
                            gui.close(player);
                            player.sendMessage(MessageUtils.parse("<yellow>Lade Snapshot..."));

                            if (WorldManager.loadSnapshot(player.getUniqueId(), displayName)) {
                                player.sendMessage(MessageUtils.parse("<green>Snapshot erfolgreich geladen!"));
                                // Teleport player to the world
                                WorldManager.loadWorld(player);
                                if (WorldManager.getLoadedWorld(player) != null) {
                                    player.teleport(WorldManager.getLoadedWorld(player).getSpawnLocation());
                                }
                            } else {
                                player.sendMessage(MessageUtils.parse("<red>Fehler beim Laden des Snapshots!"));
                            }
                        } else if (event.getClick() == ClickType.RIGHT) {
                            // Delete snapshot with confirmation
                            openDeleteConfirmation(player, displayName);
                        }
                    });

            gui.setItem(slot, snapshotItem);
            slot++;
            itemsInRow++;
        }
    }

    private static void openDeleteConfirmation(Player player, String snapshotName) {
        Gui confirmGui = Gui.gui()
                .title(MessageUtils.parse("<red>Snapshot löschen bestätigen"))
                .rows(3)
                .disableAllInteractions()
                .create();

        List<Component> lore = new ArrayList<>();

        lore.add(MessageUtils.parse("<gray>Möchtest du den Snapshot wirklich löschen?"));
        lore.add(MessageUtils.parse(""));
        lore.add(MessageUtils.parse("<white>" + snapshotName));
        lore.add(MessageUtils.parse(""));
        lore.add(MessageUtils.parse("<red>Diese Aktion kann nicht rückgängig gemacht werden!"));


        // Confirmation message
        confirmGui.setItem(2, 5, ItemBuilder.from(Material.PAPER)
                .name(MessageUtils.parse("<yellow>Snapshot löschen?"))
                .lore(lore.toArray(new Component[0]))
                .asGuiItem());

        // Confirm delete
        confirmGui.setItem(3, 3, ItemBuilder.from(Material.RED_CONCRETE)
                .name(MessageUtils.parse("<red>Ja, löschen"))
                .asGuiItem(event -> {
                    confirmGui.close(player);

                    if (WorldManager.deleteSnapshot(player.getUniqueId(), snapshotName)) {
                        player.sendMessage(MessageUtils.parse("<green>Snapshot erfolgreich gelöscht!"));
                    } else {
                        player.sendMessage(MessageUtils.parse("<red>Fehler beim Löschen des Snapshots!"));
                    }

                    // Reopen snapshot GUI
                    buildSnapshotGUI(player, 0).open(player);
                }));

        // Cancel
        confirmGui.setItem(3, 7, ItemBuilder.from(Material.GREEN_CONCRETE)
                .name(MessageUtils.parse("<green>Abbrechen"))
                .asGuiItem(event -> {
                    confirmGui.close(player);
                    // Reopen snapshot GUI
                    buildSnapshotGUI(player, 0).open(player);
                }));

        confirmGui.open(player);
    }

    private static void addNavigationItems(Gui gui, Player player, int page, String[] snapshots) {
        int totalPages = (int) Math.ceil((double) snapshots.length / SNAPSHOTS_PER_PAGE);

        // Previous page
        if (page > 0) {
            gui.setItem(6, 4, ItemBuilder.from(Material.ARROW)
                    .name(MessageUtils.parse("<yellow>Vorherige Seite"))
                    .lore(MessageUtils.parse("<gray>Seite " + page + " von " + Math.max(1, totalPages)))
                    .asGuiItem(event -> buildSnapshotGUI(player, page - 1).open(player)));
        }

        // Next page
        if (page < totalPages - 1) {
            gui.setItem(6, 6, ItemBuilder.from(Material.ARROW)
                    .name(MessageUtils.parse("<yellow>Nächste Seite"))
                    .lore(MessageUtils.parse("<gray>Seite " + (page + 2) + " von " + totalPages))
                    .asGuiItem(event -> buildSnapshotGUI(player, page + 1).open(player)));
        }

        // Page info
        if (totalPages > 1) {
            gui.setItem(6, 5, ItemBuilder.from(Material.BOOK)
                    .name(MessageUtils.parse("<white>Seite " + (page + 1) + " von " + totalPages))
                    .lore(MessageUtils.parse("<gray>Snapshots: " + snapshots.length))
                    .asGuiItem());
        }

        // Back to main menu
        gui.setItem(6, 8, ItemBuilder.from(Material.BARRIER)
                .name(MessageUtils.parse("<red>Zurück"))
                .lore(MessageUtils.parse("<gray>Zurück zum Hauptmenü"))
                .asGuiItem(event -> {
                    gui.close(player);
                    PanelGUI.buildGUI(player).open(player);
                }));

        // Close button
        gui.setItem(6, 9, ItemBuilder.from(Material.BARRIER)
                .name(MessageUtils.parse("<red>Schließen"))
                .asGuiItem(event -> gui.close(player)));
    }
}