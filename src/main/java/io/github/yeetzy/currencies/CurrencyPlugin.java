package io.github.yeetzy.currencies;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import io.github.yeetzy.currencies.registry.CommandRegistry;
import io.github.yeetzy.currencies.registry.ListenerRegistry;
import io.github.yeetzy.currencies.registry.RankingRepositoryRegistry;
import io.github.yeetzy.currencies.registry.TaskRegistry;
import io.github.yeetzy.currencies.storage.EconomyStorage;
import io.github.yeetzy.currencies.view.EconomyRankingView;
import lombok.Getter;
import me.bristermitten.pdm.PluginDependencyManager;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class CurrencyPlugin extends JavaPlugin {

    private SQLConnector sqlConnector;
    private SQLExecutor sqlExecutor;

    private EconomyStorage economyStorage;

    private RankingRepositoryRegistry rankingRepositoryRegistry;

    @Override
    public void onEnable() {
        PluginDependencyManager.of(this).loadAllDependencies().thenRun(() -> {
            try {

                sqlConnector = new SQLiteDatabaseType(new File(getDataFolder(), "storage/economies.sqlite")).connect();
                sqlExecutor = new SQLExecutor(this.sqlConnector);

                economyStorage = new EconomyStorage(this);
                economyStorage.init();

                rankingRepositoryRegistry = RankingRepositoryRegistry.of(this);
                rankingRepositoryRegistry.init();

                ViewFrame viewFrame = new ViewFrame(this);
                viewFrame.addView(new EconomyRankingView());
                viewFrame.register();

                CommandRegistry.of(this).init();
                ListenerRegistry.of(this).init();
                TaskRegistry.of(this).init();

                getLogger().info("Plugin inicializado com sucesso!");

            } catch (Throwable t) {
                t.printStackTrace();
                getLogger().severe("Ocorreu um erro durante a inicialização do plugin!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        });
    }

    public static CurrencyPlugin getInstance() {
        return getPlugin(CurrencyPlugin.class);
    }

}
