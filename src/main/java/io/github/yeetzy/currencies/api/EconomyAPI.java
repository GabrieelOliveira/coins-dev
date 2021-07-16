package io.github.yeetzy.currencies.api;

import com.google.common.collect.Sets;
import io.github.yeetzy.currencies.CurrencyPlugin;
import io.github.yeetzy.currencies.economy.data.User;
import io.github.yeetzy.currencies.storage.EconomyStorage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EconomyAPI {

    @Getter private static final EconomyAPI instance = new EconomyAPI();

    protected final CurrencyPlugin plugin = CurrencyPlugin.getInstance();

    private final EconomyStorage storage = plugin.getEconomyStorage();

    /**
     * Search all users to look for one with the entered custom filter.
     *
     * @param filter custom filter to search
     * @return {@link Optional} with the user found
     */
    public Optional<User> findUserByFilter(Predicate<User> filter) {
        return allUsers().stream()
                .filter(filter)
                .findFirst();
    }

    /**
     * Search all users to look for every with the entered custom filter.
     *
     * @param filter custom filter to search
     * @return {@link Set} with all users found
     */
    public Set<User> findUsersByFilter(Predicate<User> filter) {
        return allUsers().stream()
                .filter(filter)
                .collect(Collectors.toSet());
    }

    /**
     * Search all users to look for every with the entered custom filter.
     *
     * @param uniqueId user UUID
     * @return {@link Optional} with the user found
     */
    public Optional<User> findUserByUniqueId(UUID uniqueId) {
        return allUsers().stream()
                .filter(user -> user.getUniqueId().equals(uniqueId))
                .findFirst();
    }

    /**
     * Search all users to look for every with the entered custom filter.
     *
     * @param player an online player
     * @return {@link Optional} with the user found
     */
    public Optional<User> findUserByPlayer(Player player) {
        return allUsers().stream()
                .filter(user -> user.getUniqueId().equals(player.getUniqueId()))
                .findFirst();
    }

    /**
     * Retrieve all users loaded so far.
     *
     * @return {@link Set} with users
     */
    public Set<User> allUsers() {
        return Sets.newLinkedHashSet(storage.getUsers().values());
    }

}
