package pepgames.plugins.pepitemadder.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.persistence.PersistentDataType;
import pepgames.plugins.pepitemadder.PepItemAdder;
import pepgames.plugins.pepitemadder.item.BaseItem;
import pepgames.plugins.pepitemadder.item.types.ToolItem;
import pepgames.plugins.pepitemadder.mechanics.types.ConditionalMechanic;

public class DefaultListener implements Listener {

    private PepItemAdder plugin;

    public DefaultListener(PepItemAdder plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this , plugin);
    }

    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (!event.getItem().getItemMeta().getPersistentDataContainer().has(BaseItem.ITEM_IDENTIFIER , PersistentDataType.STRING)) return;
        if (!plugin.getItemManager().hasItem( event.getItem().getItemMeta().getPersistentDataContainer().get(BaseItem.ITEM_IDENTIFIER , PersistentDataType.STRING) )) return;

        BaseItem baseItem = plugin.getItemManager().getItem( event.getItem().getItemMeta().getPersistentDataContainer().get(BaseItem.ITEM_IDENTIFIER , PersistentDataType.STRING) );

        if (baseItem instanceof ToolItem) {

            ToolItem toolItem = (ToolItem) baseItem;
            if (!toolItem.getMechanics().containsKey("on_interact")) return;

            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                ((ConditionalMechanic) toolItem.getMechanics().get("on_interact")).processActions("RIGHT" , event.getPlayer());
            else if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))
                ((ConditionalMechanic) toolItem.getMechanics().get("on_interact")).processActions("LEFT" , event.getPlayer());

        }
    }

}
