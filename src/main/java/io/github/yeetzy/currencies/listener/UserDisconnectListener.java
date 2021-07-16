package io.github.yeetzy.currencies.listener;

import io.github.yeetzy.currencies.economy.data.User;
import io.github.yeetzy.currencies.storage.EconomyStorage;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public final class UserDisconnectListener implements Listener {

    protected final EconomyStorage economyStorage;

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        User user = economyStorage.get(player.getUniqueId());

        if (user != null) {
            economyStorage.saveAndRemove(user);
        }
    }

}
