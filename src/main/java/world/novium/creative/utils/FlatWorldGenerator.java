package world.novium.creative.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Random;

public class FlatWorldGenerator extends ChunkGenerator {
    @Override
    public java.util.@NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Collections.emptyList();
    }

    /**
     * Generates the chunk data for a specific chunk, implementing the flat world layers.
     * This method combines the logic from Kotlin's generateBedrock and generateSurface.
     *
     * @param world The world this chunk belongs to.
     * @param random A Random object for generating random elements.
     * @param chunkX The X coordinate of the chunk.
     * @param chunkZ The Z coordinate of the chunk.
     * @return A ChunkData object representing the generated chunk.
     */
    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
        // Create a new ChunkData instance.
        ChunkData chunk = createChunkData(world);

        // Loop through each block column within the chunk (16x16 blocks)
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Bedrock layer at y=-63 (as per Kotlin code)
                chunk.setBlock(x, -63, z, Material.BEDROCK);

                // Generate layers from y=-62 up to y=192 (as per Kotlin code)
                for (int y = -62; y <= 192; y++) {
                    if (y < 190) {
                        // Equivalent to MaterialSetTag.BASE_STONE_OVERWORLD.values.random(ktRandom)
                        // Using STONE as a common base stone for simplicity in standard Bukkit.
                        chunk.setBlock(x, y, z, Material.STONE);
                    } else if (y == 190) {
                        chunk.setBlock(x, y, z, Material.MOSS_BLOCK);
                    } else if (y == 191) {
                        chunk.setBlock(x, y, z, Material.GRASS_BLOCK);
                    }
                }
                // Set the biome for the entire column to PLAINS
                biome.setBiome(x, z, Biome.PLAINS);
            }
        }
        return chunk;
    }

    /**
     * Defines the fixed spawn point for the world.
     *
     * @param world The world.
     * @param random A Random object.
     * @return The spawn location.
     */
    @Override
    public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
        // Set the spawn point to be at x=0, y=194 (just above the highest generated layer), z=0
        return new Location(world, 0, 194, 0);
    }
}