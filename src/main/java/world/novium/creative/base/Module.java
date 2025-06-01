package world.novium.creative.base;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface Module<T extends Module<T>> {
    void enable();

    void disable();

    default Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }

    default void registerCommands(List<@NotNull Command> commands) {
        commands.forEach(command -> {
            command.build().register();
        });
    }
}