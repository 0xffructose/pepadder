package pepgames.plugins.pepitemadder.mechanics.actions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import pepgames.plugins.Essentials;
import pepgames.plugins.pepitemadder.interfaces.IAction;
import pepgames.plugins.pepitemadder.item.BaseItem;

public class EssentialsAction implements IAction {

    private String params , identifier;

    public EssentialsAction(String params , String identifier) {
        this.params = params; this.identifier = identifier;
    }

    @Override
    public void process(Player player) {
        String[] seperatedParams = params.split(" ");

        switch (seperatedParams[0].toLowerCase()) {

            case "fly":
                Essentials.changeFlightState(player);
                break;

            case "fly-state":
                if (seperatedParams[1].equalsIgnoreCase("true")) Essentials.setFlightState(player , true);
                else if (seperatedParams[1].equalsIgnoreCase("false")) Essentials.setFlightState(player , false);
                break;

            case "destroy":
                // if (!player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(BaseItem.ITEM_IDENTIFIER , PersistentDataType.STRING)) return;
                // if (!player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(BaseItem.ITEM_IDENTIFIER , PersistentDataType.STRING).equalsIgnoreCase(identifier)) return;

                player.getInventory().getItemInMainHand().setType(Material.AIR);
                break;
        }
    }
}
