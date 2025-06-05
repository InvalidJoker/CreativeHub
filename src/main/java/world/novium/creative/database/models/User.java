package world.novium.creative.database.models;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.List;
import java.util.UUID;

@Entity("users")
public class User {
    @Id
    private UUID uniqueId;
    private List<Achievement> achievements;

    public User() {}

    public User(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public boolean hasAchievement(Achievement achievement) {
        return achievements != null && achievements.contains(achievement);
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }
}