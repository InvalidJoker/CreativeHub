package world.novium.creative.managers;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import world.novium.creative.CreativePlugin;
import world.novium.creative.utils.FlatWorldGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldManager {

    private static final Map<String, World> loadedWorlds = new HashMap<>();

    public static boolean worldExists(String name) {
        return Bukkit.getWorldContainer().toPath().resolve(name).toFile().exists();
    }

    public static String getWorldName(Player player) {
        return "world_" + player.getUniqueId();
    }

    public static File getWorldFolder(UUID uuid) {
        String worldName = "world_" + uuid;
        return Bukkit.getWorldContainer().toPath().resolve(worldName).toFile();
    }

    public static void createWorld(Player player) {
        String name = getWorldName(player);

        if (worldExists(name)) return;

        World world = Bukkit.createWorld(new WorldCreator(name).generator(new FlatWorldGenerator()));
        if (world != null) {
            loadedWorlds.put(name, world);
        }
    }

    public static void loadWorld(Player player) {
        String name = getWorldName(player);

        if (loadedWorlds.containsKey(name)) {
            loadedWorlds.get(name);
            return;
        }
        if (!worldExists(name)) return;

        World world = Bukkit.createWorld(new WorldCreator(name));
        if (world != null) {
            loadedWorlds.put(name, world);
        }
    }

    public static void unloadWorld(Player player, boolean save) {
        String name = getWorldName(player);

        World world = loadedWorlds.remove(name);
        if (world != null) {
            Bukkit.unloadWorld(world, save);
        }
    }

    public static boolean deleteWorld(Player player) {
        String name = getWorldName(player);

        World world = Bukkit.getWorld(name);
        if (world != null) {
            world.getPlayers().forEach(p -> {
                p.sendMessage("Your world is being deleted. You will be teleported to the main world.");
                p.teleport(Bukkit.getWorlds().getFirst().getSpawnLocation());
            });
        }

        if (worldExists(name)) {
            System.out.println("Deleting world: " + name);
            Bukkit.unloadWorld(name, false);
            Bukkit.getScheduler().runTaskLater(CreativePlugin.getInstance(), () -> {
                if (deleteWorldDirectory(name)) {
                    System.out.println("World directory deleted successfully: " + name);
                } else {
                    System.out.println("Failed to delete world directory: " + name);
                }
            }, 20L);
            loadedWorlds.remove(name);
            return true;
        }
        return false;
    }

    public static World getLoadedWorld(Player player) {
        String name = getWorldName(player);
        return loadedWorlds.get(name);
    }

    public static World getLoadedWorld(String name) {
        return loadedWorlds.get(name);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean deleteWorldDirectory(String worldName) {
        File worldFolder = Bukkit.getWorldContainer().toPath().resolve(worldName).toFile();
        try {
            Files.walk(worldFolder.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            CreativePlugin.getInstance().getLogger().warning("Failed to delete world directory: " + worldName);
            return false;
        }
        return !worldFolder.exists();
    }
}
