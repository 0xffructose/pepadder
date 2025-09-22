package pepgames.plugins.pepitemadder.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Jigsaw;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import pepgames.plugins.pepitemadder.PepItemAdder;
import pepgames.plugins.pepitemadder.item.BaseItem;
import pepgames.plugins.pepitemadder.item.types.FurnitureItem;

public class FurnitureListener implements Listener {

    private PepItemAdder plugin;

    public FurnitureListener(PepItemAdder plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /*
    @EventHandler
    public void onFurniturePlace(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (event.getItem() == null || !event.getItem().getType().equals(Material.ITEM_FRAME)) return;

        if (!event.getItem().getItemMeta().getPersistentDataContainer().has(BaseItem.ITEM_IDENTIFIER , PersistentDataType.STRING)) return;
        if (!plugin.getItemManager().hasItem( event.getItem().getItemMeta().getPersistentDataContainer().get(BaseItem.ITEM_IDENTIFIER , PersistentDataType.STRING) )) return;

        BaseItem baseItem = plugin.getItemManager().getItem( event.getItem().getItemMeta().getPersistentDataContainer().get(BaseItem.ITEM_IDENTIFIER , PersistentDataType.STRING) );

        if ( baseItem instanceof FurnitureItem ) {

            FurnitureItem furnitureItem = (FurnitureItem) baseItem;
            Bukkit.getConsoleSender().sendMessage(event.getBlockFace().toString());
            if ( furnitureItem.placeLimitations.limitedFaces.contains(event.getBlockFace()) ) return;



            BlockFace placedFace = event.getBlockFace();
            Block blockAbove = event.getClickedBlock().getRelative(placedFace).getRelative(0 , 1 , 0);

            if (blockAbove.getType().equals(Material.AIR)) blockAbove.setType(Material.GLASS);

        }
    }
    */

    @EventHandler
    public void onFurniturePlace(HangingPlaceEvent event) {
        if (!(event.getEntity() instanceof ItemFrame)) return;

        if (!event.getItemStack().getItemMeta().getPersistentDataContainer().has(BaseItem.ITEM_IDENTIFIER, PersistentDataType.STRING))
            return;
        if (!plugin.getItemManager().hasItem(event.getItemStack().getItemMeta().getPersistentDataContainer().get(BaseItem.ITEM_IDENTIFIER, PersistentDataType.STRING)))
            return;

        BaseItem baseItem = plugin.getItemManager().getItem(event.getItemStack().getItemMeta().getPersistentDataContainer().get(BaseItem.ITEM_IDENTIFIER, PersistentDataType.STRING));

        if (baseItem instanceof FurnitureItem) {

            FurnitureItem furnitureItem = (FurnitureItem) baseItem;
            if (furnitureItem.placeLimitations.limitedFaces.contains(event.getBlockFace())) {
                event.setCancelled(true);
                return;
            }

            ItemFrame itemFrame = (ItemFrame) event.getEntity();

            itemFrame.setFixed(true);
            itemFrame.setVisible(false);
            itemFrame.setCustomNameVisible(false);
            itemFrame.setRotation(getFrameRotation(event.getPlayer().getLocation().getYaw()));
            itemFrame.setItem(furnitureItem.getFurnitureModel().getItemStack());

            plugin.getConfigManager().getConfig("world_data").set("item_frame_pos" , itemFrame.getLocation());
            plugin.getConfigManager().saveConfig("world_data");

            BlockFace placedFace = event.getBlockFace();
            Block blockAbove = event.getBlock().getRelative(placedFace).getRelative(0, 0, 0);

            if (blockAbove.getType().equals(Material.AIR)) blockAbove.setType(Material.BARRIER);

            // plugin.getWorldManager().addFurnitureData(event.getPlayer().getWorld().getName(), blockAbove.getLocation(), itemFrame);
            // Bukkit.getConsoleSender().sendMessage(plugin.getWorldManager().worldHolders.size() + "");
        }
    }

    @EventHandler
    public void onFurnitureBreak(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
        if (!event.getClickedBlock().getType().equals(Material.BARRIER)) return;
        // if (!plugin.getWorldManager().worldHolders.containsKey(event.getClickedBlock().getWorld().getName())) return;
        // if (!plugin.getWorldManager().worldHolders.get(event.getClickedBlock().getWorld().getName()).furnitureDatas.containsKey(event.getClickedBlock().getLocation()))
        //    return;

        // plugin.getWorldManager().worldHolders.get(event.getClickedBlock().getWorld().getName()).furnitureDatas.get(event.getClickedBlock().getLocation()).remove();
        // plugin.getWorldManager().worldHolders.get(event.getClickedBlock().getWorld().getName()).furnitureDatas.remove(event.getClickedBlock().getLocation());
        event.getClickedBlock().setType(Material.AIR);
    }

    public static Rotation getFrameRotation(final double yaw) {
        int id = (int) (((Location.normalizeYaw((float) yaw) + 180) * 8 / 360) + 0.5) % 8;
        return Rotation.values()[id];
    }


}
