package pepgames.plugins.pepitemadder.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import pepgames.plugins.commands.Command;
import pepgames.plugins.commands.parameter.Param;
import pepgames.plugins.pepitemadder.PepItemAdder;

public class Commands {

    @Command(names={"pia-get"} , permission="pep.pia.admin")
    public void getItemCommand(Player player , @Param(name="item-name") String itemName) {
        PepItemAdder.getInstance().getItemManager().giveItem(itemName , player);
    }

    @Command(names={"pia-give"} , permission="pep.pia.admin")
    public void giveItemCommand(Player player , @Param(name="target") Player target , @Param(name="item-name") String itemName) {
        PepItemAdder.getInstance().getItemManager().giveItem(itemName , target);

    }

    @Command(names={"pia-find"} , permission="pep.pia.admin")
    public void giveItemCommand(Player player) {
        for (Entity entity : player.getLocation().getWorld().getNearbyEntities(player.getLocation() , 20 , 20 , 20)) {
            if (entity.getType() == EntityType.ITEM_FRAME ) {
                Bukkit.getConsoleSender().sendMessage("FOUND");
                break;
            }
        }
    }

}
