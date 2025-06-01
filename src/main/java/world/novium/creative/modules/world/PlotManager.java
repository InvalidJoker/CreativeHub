package world.novium.creative.modules.world;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import org.bukkit.entity.Player;

import java.util.Set;

public class PlotManager {
    private static PlotManager self;
    private final PlotAPI plotAPI;

    public PlotManager() {
        this.plotAPI = new PlotAPI();
        self = this;
    }

    public static PlotManager getInstance() {
        return self;
    }

    public Set<Plot> getPlots(Player player) {
        PlotPlayer<?> plotPlayer = PlotPlayer.from(player);
        return plotAPI.getPlayerPlots(plotPlayer);
    }
}
