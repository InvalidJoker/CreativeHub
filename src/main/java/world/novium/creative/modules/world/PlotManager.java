package world.novium.creative.modules.world;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Optional<Plot> claimRandomPlot(Player player) {
        PlotPlayer<?> plotPlayer = PlotPlayer.from(player);
        Optional<PlotArea> plotArea = plotAPI.getPlotAreas("world").stream().findFirst();

        Set<Plot> availablePlots = plotArea
                .map(area -> area.getPlots().stream()
                        .filter(plot -> plot.canClaim(plotPlayer))
                        .collect(Collectors.toSet()))
                .orElse(Set.of());

        if (availablePlots.isEmpty()) {
            player.sendMessage("No available plots to claim.");
            return Optional.empty();
        }

        Plot plot = availablePlots.iterator().next();

        plot.claim(plotPlayer, true, null, true, true);

        return Optional.of(plot);
    }
}
