package io.github.yeetzy.currencies.view;

import io.github.yeetzy.currencies.economy.data.User;
import io.github.yeetzy.currencies.economy.data.type.EconomyType;
import io.github.yeetzy.currencies.util.ColorUtil;
import io.github.yeetzy.currencies.util.item.CustomItem;
import io.github.yeetzy.currencies.util.item.ItemBuilder;
import io.github.yeetzy.currencies.util.number.NumberFormat;
import me.saiintbrisson.minecraft.PreRenderViewContext;
import me.saiintbrisson.minecraft.View;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EconomyRankingView extends View {

    public EconomyRankingView() {
        super(6, "Ranking");
    }

    @Override
    protected void onOpen(PreRenderViewContext context) {
        Player player = context.getPlayer();

        EconomyType economyType = EconomyType.valueOf(context.get("economyType"));
        List<User> rankedUsers = context.get("rankedUsers");

        context.setInventoryTitle("Ranking - " + economyType.getDisplayName());

        slot(1, 5).withItem(new ItemBuilder(economyType.getIcon())
                .name(economyType.getDisplayNameColored())
                .lore(economyType.getDescription())
                .addFlags(ItemFlag.values())
                .build()
        ).cancelOnClick();

        int[] slot = {3, 2};

        AtomicInteger position = new AtomicInteger(1);

        for (int i = 0; i < 10; i++) {

            try {

                User user = rankedUsers.get(i);
                OfflinePlayer userPlayer = Bukkit.getOfflinePlayer(user.getUniqueId());

                slot(slot[0], slot[1]).withItem(new CustomItem(Material.SKULL_ITEM)
                        .name(ColorUtil.colored(String.format("&a%s &8-&f %s", position.getAndIncrement(), userPlayer.getName())))
                        .lore(
                                ColorUtil.colored(
                                        "",
                                        String.format(
                                                "&7Com a quantia de &f%s&7 %s.",
                                                NumberFormat.format(user.get(economyType)),
                                                economyType.getDisplayNamePlural().toLowerCase()
                                        )
                                )
                        )
                        .addFlags(ItemFlag.values())
                        .owner(userPlayer.getName())
                        .build()
                ).cancelOnClick();

            } catch (Throwable ignored) {

                slot(slot[0], slot[1]).withItem(new CustomItem(Material.SKULL_ITEM)
                        .name(ColorUtil.colored("&c???"))
                        .lore(ColorUtil.colored("&7Esta posição ainda não foi preenchida."))
                        .addFlags(ItemFlag.values())
                        .data((short) 3)
                        .build()
                ).cancelOnClick();

            }

            slot[1]++;

            if (slot[1] == 9) {
                slot[0] = 4;
                slot[1] = 4;
            }

        }

        long count = rankedUsers.stream()
                .filter(user -> user.getUniqueId().equals(player.getUniqueId()))
                .count();

        slot(6, 5).withItem(new CustomItem(Material.SKULL_ITEM)
                .owner(player.getName())
                .name(ColorUtil.colored(String.format("&f%sº&7 (sua posição)", count)))
                .lore("")
                .owner(player.getName())
                .addFlags(ItemFlag.values())
                .build()
        ).cancelOnClick();
    }

}

