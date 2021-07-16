package io.github.yeetzy.currencies.registry;

import io.github.yeetzy.currencies.CurrencyPlugin;
import io.github.yeetzy.currencies.command.CashCommand;
import io.github.yeetzy.currencies.command.MoneyCommand;
import io.github.yeetzy.currencies.util.ColorUtil;
import lombok.Data;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;

@Data(staticConstructor = "of")
public final class CommandRegistry {

    private final CurrencyPlugin plugin;

    public void init() {
        BukkitFrame bukkitFrame = new BukkitFrame(plugin);

        MessageHolder messageHolder = bukkitFrame.getMessageHolder();

        final String ERROR_MESSAGE = ColorUtil.colored("&cOcorreu um erro durante a execução deste comando!");
        final String INCORRECT_USAGE_MESSAGE = ColorUtil.colored("&cUso incorreto! Utilize /{usage}");
        final String INCORRECT_TARGET_MESSAGE = ColorUtil.colored("&cEste comando foi destinado apenas à jogadores!");
        final String NO_PERMISSION_MESSAGE = ColorUtil.colored("&cVocê não possui permissão para executar este comando.");

        messageHolder.setMessage(MessageType.ERROR, ERROR_MESSAGE);
        messageHolder.setMessage(MessageType.INCORRECT_USAGE, INCORRECT_USAGE_MESSAGE);
        messageHolder.setMessage(MessageType.INCORRECT_TARGET, INCORRECT_TARGET_MESSAGE);
        messageHolder.setMessage(MessageType.NO_PERMISSION, NO_PERMISSION_MESSAGE);

        bukkitFrame.registerCommands(
                new MoneyCommand(plugin),
                new CashCommand(plugin)
        );
    }

}
