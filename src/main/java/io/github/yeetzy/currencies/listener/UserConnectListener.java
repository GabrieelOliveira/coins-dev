package io.github.yeetzy.currencies.listener;

import io.github.yeetzy.currencies.economy.data.User;
import io.github.yeetzy.currencies.storage.EconomyStorage;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public final class UserConnectListener implements Listener {

    protected final EconomyStorage economyStorage;

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        CompletableFuture.runAsync(()->{
        User profile = economyStorage.getFromDatabase(player.getUniqueId());

        if (profile == null) {
            economyStorage.storageNew(User.builder()
                    .uniqueId(player.getUniqueId())
                    .gold(0)
                    .cash(0)
                    .build()
            );
        } else {
            economyStorage.addToMemory(profile);
        }
        });
    }

}
