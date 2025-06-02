package world.novium.creative.commands;

import dev.jorel.commandapi.CommandTree;

/**
 * Represents a wrapper for a CommandAPICommand-based command.
 */
public interface Command {
    /**
     * Builds and returns the CommandAPICommand instance.
     *
     * @return the constructed CommandAPICommand
     */
    CommandTree  build();
}