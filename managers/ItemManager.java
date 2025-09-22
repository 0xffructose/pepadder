package pepgames.plugins.pepitemadder.managers;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pepgames.plugins.TextUtil;
import pepgames.plugins.pepitemadder.PepItemAdder;
import pepgames.plugins.pepitemadder.interfaces.IDisableable;
import pepgames.plugins.pepitemadder.item.BaseItem;
import pepgames.plugins.pepitemadder.item.ItemParser;
import pepgames.plugins.pepitemadder.item.types.*;
import pepgames.plugins.pepitemadder.item.util.PackedFeature;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemManager implements IDisableable {

    private PepItemAdder plugin;
    @Getter private Map<String , BaseItem> itemsMap;

    private final File contentsFolder;

    public ItemManager(PepItemAdder plugin) {
        this.plugin = plugin;

        contentsFolder = new File(plugin.getDataFolder() , "/contents/");
        if (!contentsFolder.exists()) contentsFolder.mkdirs();

        itemsMap = ItemParser.parseAll(contentsFolder);
    }

    public boolean hasItem(String identifier) {
        return itemsMap.containsKey(identifier);
    }

    public BaseItem getItem(String item){
        if (!hasItem(item)) return null;
        return itemsMap.get(item);
    }

    public void giveItem(String itemName , Player player) {
        BaseItem baseItem = PepItemAdder.getInstance().getItemManager().getItem(itemName);
        if (baseItem == null) {
            PepItemAdder.getInstance().adventure().player(player).sendMessage(
                    TextUtil.format(PepItemAdder.getInstance().getConfigManager().getConfig("messages").getString("messages.item-doesnt-exists"))
            );
            return;
        }

        if (baseItem.getType().equals(BaseItem.Type.CUSTOM)) player.getInventory().addItem( ((CustomItem) baseItem).getItemStack() );
        else if (baseItem.getType().equals(BaseItem.Type.FURNITURE)) player.getInventory().addItem( ((FurnitureItem) baseItem).getFurnitureFrame() );
        else if (baseItem.getType().equals(BaseItem.Type.ARMOR)) player.getInventory().addItem( ((ArmorItem) baseItem).getItemStack() );
        else if (baseItem.getType().equals(BaseItem.Type.BLOCK)) player.getInventory().addItem( ((BlockItem) baseItem).getItemStack() );
        else if (baseItem.getType().equals(BaseItem.Type.TOOL)) {
            if ( ((ToolItem) baseItem).getPackedFeature() != null && !((ToolItem) baseItem).getPackedFeature().isNull() ) {
                PackedFeature clonedFeature = ((ToolItem) baseItem).getPackedFeature().clone();
                clonedFeature.adaptFeature(
                        player,
                        null,
                        null
                );

                if (ToolItem.playerPackedFeature.containsKey(player.getUniqueId())) ToolItem.playerPackedFeature.get(player.getUniqueId()).put(itemName , clonedFeature);
                else {
                    ToolItem.playerPackedFeature.put(player.getUniqueId(), new HashMap<>());
                    ToolItem.playerPackedFeature.get(player.getUniqueId()).put(itemName , clonedFeature);
                }
            }
            player.getInventory().addItem( ((ToolItem) baseItem).getItemStack() );
        }
    }

    public void addPackedFeatureForPlayer(UUID player , String reference , PackedFeature packedFeature) {
        if (!(itemsMap.get(reference) instanceof ToolItem)) return;
        if ( ToolItem.playerPackedFeature.containsKey(player) ) return;

    }

    @Override
    public void onDisable() {

    }
}
