package io.github.yeetzy.currencies.economy.dao.adapter;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import io.github.yeetzy.currencies.economy.data.User;

import java.util.UUID;

public final class UserAdapter implements SQLResultAdapter<User> {

    @Override
    public User adaptResult(SimpleResultSet resultSet) {
        return User.builder()
                .uniqueId(UUID.fromString(resultSet.get("uniqueId")))
                .cash(resultSet.get("cash"))
                .gold(resultSet.get("gold"))
                .build();
    }

}
