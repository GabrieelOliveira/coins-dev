package io.github.yeetzy.currencies.economy.data;

import io.github.yeetzy.currencies.economy.data.type.EconomyType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public final class User {

    private final UUID uniqueId;
    private double cash;
    private double gold;

    // deposit methods
    public void depositCash(double amount) {
        this.cash += amount;
    }

    public void depositGold(double amount) {
        this.gold += amount;
    }

    // withdraw methods
    public void withdrawCash(double amount) {
        this.cash -= amount;
    }

    public void withdrawGold(double amount) {
        this.gold -= amount;
    }

    public double get(EconomyType economyType) {
        switch (economyType) {
            case GOLD:
                return this.gold;
            case CASH:
                return this.cash;
            default:
                return 0;
        }
    }

}

