package pepgames.plugins.pepitemadder.mechanics.actions;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pepgames.plugins.pepitemadder.interfaces.IAction;
import pepgames.plugins.pepitemadder.item.ticker.Ticker;

import java.util.ArrayList;
import java.util.List;

public class ActionsParser {

    // private static String ACTIONS_PATH = "actions";

    public static List<IAction> parseActions(FileConfiguration config , String parentPath , String actionsPath , String identifier) {

        // Parent Path -> item.mechanics.condition.actions

        if (config.getStringList(parentPath + "." + actionsPath).isEmpty()) return null;

        List<IAction> actions = new ArrayList<>();
        for (String actionLine : config.getStringList(parentPath + "." + actionsPath)) {

            String[] actionSeperated = new String[2];
            actionSeperated[0] = actionLine.substring(0 , actionLine.indexOf(' '));
            actionSeperated[1] = actionLine.substring(actionLine.indexOf(']') + 2);

            switch (actionSeperated[0].toLowerCase()) {
                case "[message]":
                    actions.add(new MessageAction(actionSeperated[1] , identifier));
                    break;
                case "[container]":
                    actions.add(new ContainerAction(actionSeperated[1] , identifier));
                    break;
                case "[ticker]":
                    actions.add(new TickerAction(actionSeperated[1] , identifier));
                    break;
                case "[essentials]":
                    actions.add(new EssentialsAction(actionSeperated[1] , identifier));
                    break;
            }

        }

        return actions;
    }

    public static void runActions(List<IAction> actions , Player player) {
        if (actions.isEmpty() || actions == null) return;

        for (IAction action : actions) {
            action.process(player);
        }
    }

}
