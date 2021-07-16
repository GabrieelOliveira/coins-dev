package io.github.yeetzy.currencies.registry;

import io.github.yeetzy.currencies.CurrencyPlugin;
import io.github.yeetzy.currencies.economy.ranking.task.RankingUpdateTask;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

@Data(staticConstructor = "of")
public final class TaskRegistry {

    private final CurrencyPlugin plugin;

    public void init() {
        BukkitScheduler scheduler = Bukkit.getScheduler();

        scheduler.runTaskTimerAsynchronously(
                plugin,
                new RankingUpdateTask(
                        plugin.getEconomyStorage().getEconomyDao(),
                        plugin.getRankingRepositoryRegistry().getRankingRepository()
                ),
                0,
                5 * 60 * 20
        );

    }

}
