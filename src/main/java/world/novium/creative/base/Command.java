package world.novium.creative.base;

import dev.jorel.commandapi.CommandAPICommand;

/**
 * Represents a wrapper for a CommandAPICommand-based command.
 */
public interface Command {
    /**
     * Builds and returns the CommandAPICommand instance.
     *
     * @return the constructed CommandAPICommand
     */
    CommandAPICommand build();
}