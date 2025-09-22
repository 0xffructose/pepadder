package pepgames.plugins.pepitemadder.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pepgames.plugins.pepitemadder.PepItemAdder;
import pepgames.plugins.pepitemadder.item.BaseItem;
import pepgames.plugins.pepitemadder.item.container.Container;
import pepgames.plugins.pepitemadder.item.container.FuelContainer;
import pepgames.plugins.pepitemadder.item.container.StorageContainer;
import pepgames.plugins.pepitemadder.item.ticker.CountdownTicker;
import pepgames.plugins.pepitemadder.item.ticker.Ticker;
import pepgames.plugins.pepitemadder.item.types.ToolItem;
import pepgames.plugins.pepitemadder.item.util.PackedFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveListener implements Listener {

    private PepItemAdder plugin;

    public SaveListener(PepItemAdder plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this , plugin);
    }

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent event) {

        if (!ToolItem.playerPackedFeature.containsKey(event.getPlayer().getUniqueId())) return;

        for (String identifier : ToolItem.playerPackedFeature.get(event.getPlayer().getUniqueId()).keySet()) {

            Container container = ToolItem.playerPackedFeature.get(event.getPlayer().getUniqueId()).get(identifier).container;
            if (container != null && container instanceof StorageContainer) {

                // plugin.getConfigManager().getConfig("player_data").set(event.getPlayer().getUniqueId() + "." + identifier + ".container.fuel-amount" , fuelContainer.fuelAmount);
                for (int i = 0; i < container.containerGui.getInventory().getSize(); i++) {
                    ItemStack itemStack = container.containerGui.getInventory().getItem(i);
                    if (itemStack == null) continue;
                    plugin.getConfigManager().getConfig("player_data").set(event.getPlayer().getUniqueId() + "." + identifier + ".container.contents.slot" + i , itemStack.serialize());
                }
            }

            Ticker ticker = ToolItem.playerPackedFeature.get(event.getPlayer().getUniqueId()).get(identifier).ticker;
            if (ticker != null && ticker instanceof CountdownTicker) {

                if (ticker.isRunning()) ticker.cancel();

                CountdownTicker countdownTicker = (CountdownTicker) ticker;
                plugin.getConfigManager().getConfig("player_data").set(event.getPlayer().getUniqueId() + "." + identifier + ".ticker.seconds-left" , countdownTicker.secondsLeft);

            }

            plugin.getConfigManager().saveConfig("player_data");

        }

    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {

        if (plugin.getConfigManager().getConfig("player_data").getConfigurationSection(event.getPlayer().getUniqueId().toString()) == null) return;

        for (String identifier : plugin.getConfigManager().getConfig("player_data").getConfigurationSection(event.getPlayer().getUniqueId().toString()).getKeys(false)) {

            ToolItem toolItem = (ToolItem) plugin.getItemManager().getItemsMap().get(identifier);

            PackedFeature clonedFeature = toolItem.getPackedFeature().clone();

            List<ItemStack> contents = new ArrayList<>();
            if (plugin.getConfigManager().getConfig("player_data").getConfigurationSection(event.getPlayer().getUniqueId() + "." + toolItem.identifier + ".container.contents") != null) {
                for (String key : plugin.getConfigManager().getConfig("player_data").getConfigurationSection(event.getPlayer().getUniqueId() + "." + toolItem.identifier + ".container.contents").getKeys(false)) {
                    Map<String, Object> itemData = plugin.getConfigManager().getConfig("player_data").getConfigurationSection(event.getPlayer().getUniqueId() + "." + toolItem.identifier + ".container.contents." + key).getValues(false);
                    ItemStack item = ItemStack.deserialize(itemData);
                    contents.add(item);
                }
            }

            clonedFeature.adaptFeature(
                    event.getPlayer(),
                    contents,
                    plugin.getConfigManager().getConfig("player_data").getInt(event.getPlayer().getUniqueId() + "." + identifier + ".ticker.seconds-left")
            );

            if (ToolItem.playerPackedFeature.containsKey(event.getPlayer().getUniqueId())) ToolItem.playerPackedFeature.get(event.getPlayer().getUniqueId()).put(identifier , clonedFeature);
            else {
                ToolItem.playerPackedFeature.put(event.getPlayer().getUniqueId(), new HashMap<>());
                ToolItem.playerPackedFeature.get(event.getPlayer().getUniqueId()).put(identifier , clonedFeature);
            }
        }
    }
}
