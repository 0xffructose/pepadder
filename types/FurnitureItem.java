package pepgames.plugins.pepitemadder.item.types;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import pepgames.plugins.pepitemadder.PepItemAdder;
import pepgames.plugins.pepitemadder.item.BaseItem;
import pepgames.plugins.pepitemadder.item.util.PlaceLimitations;

import java.io.File;

public class FurnitureItem extends BaseItem {

    @Getter private final CustomItem furnitureModel;
    public PlaceLimitations placeLimitations;

    private static String BEHAVIOURS_PLACEABLE_ON = "behaviours.placeable_on" , FLOOR = "floor" , CEILING = "ceiling" , WALLS = "walls";

    @Getter private ItemStack FurnitureFrame;
    private static NamespacedKey INVISIBLE_KEY = new NamespacedKey(PepItemAdder.getInstance() , "invisible");

    public FurnitureItem(CustomItem item) {
        furnitureModel = item; this.identifier = item.identifier;
        type = Type.FURNITURE;

        placeLimitations = new PlaceLimitations();

            FurnitureFrame = FurnitureItem.createInvisibleItemFrame(furnitureModel);
    }

    public static FurnitureItem parseFurniture(File configFile , FileConfiguration config) {
        if (config.getKeys(false).isEmpty() || config.getKeys(false).size() > 1) return null;

        String itemPath = config.getKeys(false).stream().findFirst().orElse("");

        FurnitureItem furnitureItem = new FurnitureItem(CustomItem.parseItem(configFile , config));

        furnitureItem.placeLimitations.setLimitedFaces(
                config.getBoolean(itemPath + "." + BEHAVIOURS_PLACEABLE_ON + "." + FLOOR),
                config.getBoolean(itemPath + "." + BEHAVIOURS_PLACEABLE_ON + "." + CEILING),
                config.getBoolean(itemPath + "." + BEHAVIOURS_PLACEABLE_ON + "." + WALLS)
        );

        return furnitureItem;
    }

    private static ItemStack createInvisibleItemFrame(CustomItem containedItem) {
        ItemStack itemStack = new ItemStack(Material.ITEM_FRAME , 1);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setCustomModelData(containedItem.getItemStack().getItemMeta().getCustomModelData());
        itemMeta.getPersistentDataContainer().set(CustomItem.ITEM_IDENTIFIER , PersistentDataType.STRING , containedItem.identifier);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
