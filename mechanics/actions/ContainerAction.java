package pepgames.plugins.pepitemadder.mechanics.actions;

import org.bukkit.entity.Player;
import pepgames.plugins.pepitemadder.interfaces.IAction;
import pepgames.plugins.pepitemadder.item.types.ToolItem;

public class ContainerAction implements IAction {

    private String params , identifier;

    public ContainerAction(String params , String identifier) {
        this.params = params; this.identifier = identifier;
    }

    @Override
    public void process(Player player) {
        String[] seperatedParams = params.split(" ");
        switch (seperatedParams[0].toLowerCase()) {
            case "open":
                ToolItem.playerPackedFeature.get(player.getUniqueId()).get(identifier).container.containerGui.open(player);
                break;
        }
    }

}
