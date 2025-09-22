package pepgames.plugins.pepitemadder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pepgames.plugins.pepItemAdderAPI;
import pepgames.plugins.pepitemadder.item.BaseItem;
import pepgames.plugins.pepitemadder.item.types.CustomItem;
import pepgames.plugins.pepitemadder.item.types.FurnitureItem;
import pepgames.plugins.pepitemadder.item.types.ToolItem;
import pepgames.plugins.pepitemadder.item.util.PackedFeature;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PepItemAdderApiImpl implements pepItemAdderAPI {

    private final PepItemAdder plugin;

    public PepItemAdderApiImpl(PepItemAdder plugin) {
        this.plugin = plugin;
    }

    @Override
    public ItemStack getItem(String itemID, UUID playerUUID) {

        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return null;

        if (!plugin.getItemManager().hasItem(itemID)) return null;

        BaseItem baseItem = PepItemAdder.getInstance().getItemManager().getItem(itemID);
        if (baseItem == null) return null;


        if (baseItem.getType().equals(BaseItem.Type.CUSTOM)) {
            CustomItem customItem = (CustomItem) baseItem;
            return customItem.getItemStack();
        }
        if (baseItem.getType().equals(BaseItem.Type.FURNITURE)) {
            FurnitureItem furnitureItem = (FurnitureItem) baseItem;
            return furnitureItem.getFurnitureFrame();
        }
        if (baseItem.getType().equals(BaseItem.Type.TOOL)) {
            ToolItem toolItem = (ToolItem) baseItem;
            if (!toolItem.getPackedFeature().isNull()) {
                PackedFeature clonedFeature = toolItem.getPackedFeature().clone();
                clonedFeature.adaptFeature(
                        player,
                        null,
                        null
                );

                if (ToolItem.playerPackedFeature.containsKey(player.getUniqueId()))
                    ToolItem.playerPackedFeature.get(player.getUniqueId()).put(itemID, clonedFeature);
                else {
                    ToolItem.playerPackedFeature.put(player.getUniqueId(), new HashMap<>());
                    ToolItem.playerPackedFeature.get(player.getUniqueId()).put(itemID, clonedFeature);
                }
            }

            return toolItem.getItemStack();
        }
        return null;
    }
}
