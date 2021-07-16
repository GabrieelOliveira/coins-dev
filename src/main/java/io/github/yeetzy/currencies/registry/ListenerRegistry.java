package io.github.yeetzy.currencies.registry;

import io.github.yeetzy.currencies.CurrencyPlugin;
import io.github.yeetzy.currencies.listener.UserConnectListener;
import io.github.yeetzy.currencies.listener.UserDisconnectListener;
import lombok.Data;
import org.bukkit.Bukkit;

@Data(staticConstructor = "of")
public final class ListenerRegistry {

    private final CurrencyPlugin plugin;

    public void init() {
        Bukkit.getPluginManager().registerEvents(
                new UserConnectListener(plugin.getEconomyStorage()),
                plugin
        );

        Bukkit.getPluginManager().registerEvents(
                new UserDisconnectListener(plugin.getEconomyStorage()),
                plugin
        );
    }

}
