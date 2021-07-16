package io.github.yeetzy.currencies.economy.data.type;

import io.github.yeetzy.currencies.util.ColorUtil;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public enum EconomyType {

    CASH(
            "Cash",
            "&eCash",
            "Cash",
                    Arrays.asList(
                            "§6Moeda mais cara da terra"
                    ),
            Material.GOLD_INGOT
    ),
    GOLD(
            "Coin",
            "&aCoin",
            "Coins",
                    Arrays.asList(
                            "§8Moera padrão"
                    ),
            Material.MAP
    );

    private String displayName;
    private String displayNameColored;
    private String displayNamePlural;

    private List<String> description;

    private Material icon;

    EconomyType(String displayName, String displayNameColored, String displayNamePlural, List<String> description, Material icon) {
        this.displayName = displayName;
        this.displayNameColored = displayNameColored;
        this.displayNamePlural = displayNamePlural;
        this.description = description;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayNameColored() {
        return displayNameColored;
    }

    public void setDisplayNameColored(String displayNameColored) {
        this.displayNameColored = displayNameColored;
    }

    public String getDisplayNamePlural() {
        return displayNamePlural;
    }

    public void setDisplayNamePlural(String displayNamePlural) {
        this.displayNamePlural = displayNamePlural;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public Material getIcon() {
        return icon;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }
}
