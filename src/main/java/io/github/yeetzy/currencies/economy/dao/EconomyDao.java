package io.github.yeetzy.currencies.economy.dao;

import com.henryfabio.sqlprovider.executor.SQLExecutor;
import io.github.yeetzy.currencies.economy.data.User;
import io.github.yeetzy.currencies.economy.dao.adapter.UserAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public final class EconomyDao {

    private final String TABLE = "economy_data";

    private final SQLExecutor sqlExecutor;

    public void createTable() {
        sqlExecutor.updateQuery("CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
                "uniqueId CHAR(36) NOT NULL PRIMARY KEY," +
                "cash DOUBLE NOT NULL," +
                "gold DOUBLE NOT NULL" +
                ");"
        );
    }

    public User selectOne(UUID uniqueId) {
        return sqlExecutor.resultOneQuery(String.format("SELECT * FROM %s WHERE uniqueId = ?", TABLE),
                statement -> statement.set(1, uniqueId.toString()),
                UserAdapter.class
        );
    }

    public Set<User> selectAll() {
        return sqlExecutor.resultManyQuery(String.format("SELECT * FROM %s", TABLE),
                k -> {
                },
                UserAdapter.class
        );
    }

    public Set<User> selectAll(String query) {
        return sqlExecutor.resultManyQuery(String.format("SELECT * FROM %s %s", TABLE, query),
                k -> {
                },
                UserAdapter.class
        );
    }

    public void insertOne(User user) {
        sqlExecutor.updateQuery(
                String.format("REPLACE INTO %s VALUES(?,?,?);", TABLE),
                statement -> {
                    statement.set(1, user.getUniqueId().toString());
                    statement.set(2, user.getGold());
                    statement.set(3, user.getCash());
                }
        );
    }

    public void deleteOne(User user) {
        sqlExecutor.updateQuery(String.format("DELETE FROM %s WHERE uniqueId = '%s'",
                TABLE,
                user.getUniqueId().toString()
        ));
    }

}

