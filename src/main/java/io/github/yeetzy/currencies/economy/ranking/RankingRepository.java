package io.github.yeetzy.currencies.economy.ranking;

import io.github.yeetzy.currencies.economy.data.User;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public final class RankingRepository {

    @Getter private final List<User> rankedUsers = new LinkedList<>();

}
