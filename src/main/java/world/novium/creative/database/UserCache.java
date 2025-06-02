package world.novium.creative.database;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import world.novium.creative.database.models.User;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserCache {
    private static UserCache instance;

    private final Cache<UUID, User> cache;
    private final Datastore datastore;

    private UserCache(Datastore datastore) {
        this.datastore = datastore;
        this.cache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(10))
                .maximumSize(1000)
                .build();
    }

    // 🔹 Static initializer
    public static void init(Datastore datastore) {
        if (instance == null) {
            instance = new UserCache(datastore);
        }
    }

    public static UserCache get() {
        if (instance == null) {
            throw new IllegalStateException("UserCache not initialized. Call UserCache.init(datastore) first.");
        }
        return instance;
    }

    // 🔹 Cache operations
    public Optional<User> get(UUID uuid) {
        return Optional.ofNullable(cache.getIfPresent(uuid));
    }

    public User getOrLoad(UUID uuid) {
        return cache.get(uuid, key -> datastore.find(User.class)
                .filter(Filters.eq("_id", key))
                .first());
    }

    public void put(User user) {
        cache.put(user.getUniqueId(), user);
    }

    public void invalidate(UUID uuid) {
        cache.invalidate(uuid);
    }

    public void save(User user) {
        datastore.save(user);
        put(user);
    }

    public void leave(UUID uuid) {
        User user = getOrLoad(uuid);
        if (user != null) {
            save(user);
        }
        invalidate(uuid);
    }

    public CompletableFuture<User> getOrCreateAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            User user = getOrLoad(uuid);
            if (user == null) {
                user = new User(uuid);
                save(user);
            }
            return user;
        });
    }
}
