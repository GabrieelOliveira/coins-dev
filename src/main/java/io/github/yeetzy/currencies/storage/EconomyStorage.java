package io.github.yeetzy.currencies.storage;

import io.github.yeetzy.currencies.CurrencyPlugin;
import io.github.yeetzy.currencies.economy.data.User;
import io.github.yeetzy.currencies.economy.dao.EconomyDao;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public final class EconomyStorage {

    protected final CurrencyPlugin plugin;

    @Getter private EconomyDao economyDao;

    @Getter private final Map<UUID, User> users = new LinkedHashMap<>();

    public void init() {
        economyDao = new EconomyDao(plugin.getSqlExecutor());
        economyDao.createTable();
    }

    public User get(UUID uniqueId) {
        return users.get(uniqueId);
    }

    public User getFromDatabase(UUID uniqueId) {
        return economyDao.selectOne(uniqueId);
    }

    public void storageNew(User user) {
        if (!users.containsKey(user.getUniqueId())) {
            economyDao.insertOne(user);
            addToMemory(user);
        }
    }

    public void saveAndRemove(User user) {
        save(user);
        removeFromMemory(user);
    }

    public void save(User user) {
        economyDao.insertOne(user);
    }

    public void addToMemory(User user) {
        if (!users.containsKey(user.getUniqueId())) {
            users.put(user.getUniqueId(), user);
        }
    }

    public void removeFromMemory(User user) {
        if (users.containsKey(user.getUniqueId())) {
            users.remove(user.getUniqueId(), user);
        }
    }

    public void delete(User user) {
        if (users.containsKey(user.getUniqueId())) {
            removeFromMemory(user);
        }

        economyDao.deleteOne(user);
    }

}

