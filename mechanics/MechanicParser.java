package pepgames.plugins.pepitemadder.mechanics;

import org.bukkit.configuration.file.FileConfiguration;
import pepgames.plugins.pepitemadder.interfaces.IAction;
import pepgames.plugins.pepitemadder.mechanics.actions.ActionsParser;
import pepgames.plugins.pepitemadder.mechanics.types.ConditionalMechanic;
import pepgames.plugins.pepitemadder.mechanics.types.Conditions;
import pepgames.plugins.pepitemadder.mechanics.types.StaticMechanic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MechanicParser {

    private static String MECHANICS_PATH = "mechanics";

    public static Map<String , Mechanic> parseMechanic(FileConfiguration config , String itemPath , String identifier) {

        if (config.getConfigurationSection(itemPath + "." + MECHANICS_PATH) == null) return null;
        if (config.getConfigurationSection(itemPath + "." + MECHANICS_PATH).getKeys(false).isEmpty()) return null;

        Map<String , Mechanic> mechanics = new HashMap<>();

        for (String eventKey : config.getConfigurationSection(itemPath + "." + MECHANICS_PATH).getKeys(false)) {

            if (Conditions.isConditionalEvent(eventKey)) {

                Map<String , List<IAction>> conditionToAction = new HashMap<>();
                for (String condition : config.getConfigurationSection(itemPath + "." + MECHANICS_PATH + "." + eventKey).getKeys(false)) {
                    if (!Conditions.isCondition(eventKey , condition)) continue;

                    List<IAction> actions = ActionsParser.parseActions(config , itemPath + "." + MECHANICS_PATH + "." + eventKey + "." + condition , "actions" , identifier);
                    conditionToAction.put(Conditions.getCondition(eventKey , condition) , actions);
                }

                mechanics.put(eventKey , new ConditionalMechanic(conditionToAction));

            } else {

                List<IAction> actions = ActionsParser.parseActions(config , itemPath + "." + MECHANICS_PATH , "actions" , identifier);
                mechanics.put(eventKey , new StaticMechanic(actions));

            }

        }

        return mechanics;
    }

}
