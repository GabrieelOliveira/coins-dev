package io.github.yeetzy.currencies.registry;

import io.github.yeetzy.currencies.CurrencyPlugin;
import io.github.yeetzy.currencies.economy.ranking.RankingRepository;
import lombok.Data;

@Data(staticConstructor = "of")
public final class RankingRepositoryRegistry {

    private final CurrencyPlugin plugin;

    private RankingRepository rankingRepository;

    public void init() {
        rankingRepository = new RankingRepository();
    }

}
