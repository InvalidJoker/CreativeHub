package world.novium.creative.modules.world;

import world.novium.creative.base.Module;

public class WorldModule implements Module<WorldModule> {
    @Override
    public void enable() {
        System.out.println("WorldManager module enabled.");
    }

    @Override
    public void disable() {
        // Logic to disable the world manager module
        System.out.println("WorldManager module disabled.");
    }
}
