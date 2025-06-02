package world.novium.creative.database;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.novium.creative.database.models.User;

@SuppressWarnings("removal")
public class Database {
    private final Logger logger = LoggerFactory.getLogger(Database.class);
    private static Datastore datastore;

    public static Datastore getDatastore() {
        if (datastore == null) {
            throw new IllegalStateException("Datastore is not initialized. Call connect() first.");
        }
        return datastore;
    }

    public void connect(
            @NotNull ConfigurationSection configSection
    ) {
        String host = configSection.getString("uri");
        String database = configSection.getString("database", "creative");

        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Database URI cannot be null or empty");
        }

        var client = MongoClients.create(host);

        datastore = Morphia.createDatastore(client, database);

        datastore.getMapper().map(User.class);

        datastore.ensureIndexes();

        logger.info("Connected to database");
    }


}
