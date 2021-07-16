package io.github.yeetzy.currencies.command;

import com.google.common.collect.ImmutableMap;
import io.github.yeetzy.currencies.CurrencyPlugin;
import io.github.yeetzy.currencies.economy.data.User;
import io.github.yeetzy.currencies.economy.data.type.EconomyType;
import io.github.yeetzy.currencies.economy.ranking.RankingRepository;
import io.github.yeetzy.currencies.util.ColorUtil;
import io.github.yeetzy.currencies.util.number.NumberFormat;
import io.github.yeetzy.currencies.view.EconomyRankingView;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class MoneyCommand {

    private final CurrencyPlugin plugin;

    @Command(
            name = "coin",
            aliases = {"coins", "money"}
    )
    public void coinCommand(Context<CommandSender> context, @Optional Player target) {
        CommandSender sender = context.getSender();

        if (target == null) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                User user = plugin.getEconomyStorage().get(player.getUniqueId());

                if (user == null) return;

                double gold = user.getGold();

                player.sendMessage(
                        ColorUtil.colored(
                                String.format(
                                        "&aVocê possui %s",
                                        NumberFormat.format(gold, EconomyType.GOLD)
                                )
                        )
                );

            } else {
                sender.sendMessage(ColorUtil.colored("&cEste comando é destinado apenas à jogadores."));
            }

        } else {

            User user = plugin.getEconomyStorage().get(target.getUniqueId());

            if (user == null) return;

            double gold = user.getGold();

            sender.sendMessage(
                    ColorUtil.colored(
                            String.format(
                                    "&f%s&a possui &f%s",
                                    target.getName(),
                                    NumberFormat.format(gold, EconomyType.GOLD)
                            )
                    )
            );

        }

    }

    @Command(
            name = "coin.deposit",
            aliases = {"depositar", "adicionar", "add"}
    )
    public void goldAddCommand(Context<CommandSender> context, Player target, double amount) {
        CommandSender sender = context.getSender();

        User user = plugin.getEconomyStorage().get(target.getUniqueId());

        if (user == null) return;

        user.depositGold(amount);

        sender.sendMessage(
                ColorUtil.colored(
                        String.format(
                                "&aVocê depositou &f%s&a na conta de &f%s",
                                NumberFormat.format(amount, EconomyType.GOLD),
                                target.getName()
                        )
                )
        );

    }

    @Command(
            name = "coin.withdraw",
            aliases = {"remover", "remove"},
            permission = "economy.admin"
    )
    public void goldRemoveCommand(Context<CommandSender> context, Player target, double amount) {
        CommandSender sender = context.getSender();

        User user = plugin.getEconomyStorage().get(target.getUniqueId());

        if (user == null) return;

        user.withdrawGold(amount);

        sender.sendMessage(
                ColorUtil.colored(
                        String.format(
                                "&aVocê removeu &f%s&a da conta de &f%s",
                                NumberFormat.format(amount, EconomyType.GOLD),
                                target.getName()
                        )
                )
        );
    }

    @Command(
            name = "coin.set",
            aliases = {"alterar", "setar"},
            permission = "economy.admin"
    )
    public void goldSetCommand(Context<CommandSender> context, Player target, double amount) {
        CommandSender sender = context.getSender();

        User user = plugin.getEconomyStorage().get(target.getUniqueId());

        if (user == null) return;

        user.setGold(amount);

        sender.sendMessage(
                ColorUtil.colored(
                        String.format(
                                "&aVocê alterou a quantia de ouros de &f%s&a para &f%s",
                                NumberFormat.format(amount, EconomyType.GOLD),
                                target.getName()
                        )
                )
        );
    }

    @Command(
            name = "coin.top",
            aliases = {"ranking"}
    )
    public void goldRankingCommand(Context<Player> context) {
        Player player = context.getSender();

        RankingRepository rankingRepository = plugin.getRankingRepositoryRegistry().getRankingRepository();
        List<User> goldRankedUsers = rankingRepository.getRankedUsers().stream()
                .sorted(Comparator.comparing(User::getGold))
                .collect(Collectors.toList());

        EconomyRankingView economyRankingView = new EconomyRankingView();
        economyRankingView.open(player, ImmutableMap.of(
                "economyType", "GOLD",
                "rankedUsers", goldRankedUsers
                )
        );
    }

}
