package pepgames.plugins.pepitemadder.item.ticker;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import pepgames.plugins.pepitemadder.interfaces.IAction;
import pepgames.plugins.pepitemadder.mechanics.actions.ActionsParser;

import java.util.List;

public abstract class Ticker {

    public enum Type { STANDARD , COUNTDOWN };
    @Getter public Type type;

    public Player attachedPlayer;

    public int startDelay , period;
    public List<IAction> actionsForEachTick;

    private BukkitRunnable bukkitRunnable;

    public static String TICKER_PATH = "ticker" , TYPE = "type" , START_DELAY = "start_delay" , PERIOD = "period" , ACTIONS = "each_tick_actions";
    public static String COUNTDOWN_FROM = "countdown_settings.countdown_from" , BEFORE_ACTIONS = "countdown_settings.before_actions" , AFTER_ACTIONS = "countdown_settings.after_actions";

    public abstract void handle();

    public void scheduleTimer(JavaPlugin plugin , long startDelay , long period) {
        if (isRunning()) cancel();
        bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                handle();
            }
        };
        bukkitRunnable.runTaskTimer(plugin , startDelay , period);
    }

    public void cancel() {
        if (!isRunning()) return;
        bukkitRunnable.cancel();
    }

    public boolean isRunning() {
        return bukkitRunnable != null && !bukkitRunnable.isCancelled();
    }

    public static Ticker parseTicker(FileConfiguration config , String itemPath , String identifier) {
        if (config.getConfigurationSection(itemPath + "." + TICKER_PATH) ==  null) return null;
        if (config.getConfigurationSection(itemPath + "." + TICKER_PATH).getKeys(false).isEmpty()) return null;

        Ticker.Type type = Ticker.Type.valueOf(config.getString(itemPath + "." + TICKER_PATH + "." + TYPE));
        if (type == null) return null;

        Integer startDelay = config.getInt(itemPath + "." + TICKER_PATH + "." + START_DELAY);
        Integer period = config.getInt(itemPath + "." + TICKER_PATH + "." + PERIOD);

        if (type.equals(Type.STANDARD)) {

            StandardTicker standardTicker = new StandardTicker(null , startDelay , period);

            List<IAction> actions = ActionsParser.parseActions(config , itemPath , TICKER_PATH + "." + ACTIONS , identifier);
            standardTicker.actionsForEachTick = actions;

            return standardTicker;

        } else if (type.equals(Type.COUNTDOWN)) {

            Integer countdownFrom = config.getInt(itemPath + "." + TICKER_PATH + "." + COUNTDOWN_FROM);

            List<IAction> actions = ActionsParser.parseActions(config , itemPath , TICKER_PATH + "." + ACTIONS , identifier);
            List<IAction> beforeActions = ActionsParser.parseActions(config , itemPath , TICKER_PATH + "." + BEFORE_ACTIONS , identifier);
            List<IAction> afterActions = ActionsParser.parseActions(config , itemPath , TICKER_PATH + "." + AFTER_ACTIONS , identifier);

            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "" + countdownFrom + " | " + beforeActions.size() + " | " + afterActions.size());

            CountdownTicker countdownTicker = new CountdownTicker(null , startDelay , period , countdownFrom);
            
            countdownTicker.actionsForEachTick = actions;
            countdownTicker.setBeforeActions(beforeActions); countdownTicker.setAfterActions(afterActions);

            return countdownTicker;
        }

        return null;
    }
}
