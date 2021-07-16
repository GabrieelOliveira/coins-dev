package io.github.yeetzy.currencies.economy.ranking.task;

import io.github.yeetzy.currencies.economy.ranking.RankingRepository;
import io.github.yeetzy.currencies.economy.dao.EconomyDao;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RankingUpdateTask implements Runnable {

    private final EconomyDao economyDao;
    private final RankingRepository rankingRepository;

    @Override
    public void run() {
        rankingRepository.getRankedUsers().clear();
        rankingRepository.getRankedUsers().addAll(economyDao.selectAll());
    }

}
