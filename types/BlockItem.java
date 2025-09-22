package pepgames.plugins.pepitemadder.item.types;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import pepgames.plugins.pepitemadder.PepItemAdder;
import pepgames.plugins.pepitemadder.item.BaseItem;
import java.io.File;

public class BlockItem extends BaseItem {

    private CustomItem item;
    public static final NamespacedKey BLOCK_KEY = new NamespacedKey(PepItemAdder.getInstance() , "pep_block");
    private static String BLOCK = "block" , VAR_A = "var_a" , VAR_B = "var_b";

    public BlockItem(CustomItem item) {
        this.item = item; identifier = item.identifier;
        type = Type.BLOCK;
    }

    public ItemStack getItemStack() {
        return item.getItemStack();
    }

    public static BlockItem parseBlock(File configFile , FileConfiguration config) {

        if (config.getKeys(false).isEmpty() || config.getKeys(false).size() > 1) return null;

        String itemPath = config.getKeys(false).stream().findFirst().orElse("");

        Material materialType = Material.valueOf( config.getString(itemPath + "." + CustomItem.MATERIAL) );
        if (!materialType.equals(Material.NOTE_BLOCK)) return null;

        BlockItem block = new BlockItem(CustomItem.parseItem(configFile , config));

        String instrument = config.getString(itemPath + "." + BLOCK + "." + VAR_A);
        Integer varB = config.getInt(itemPath + "." + BLOCK + "." + VAR_B);

        block.item.getItemStack().getItemMeta().getPersistentDataContainer().set(BLOCK_KEY , PersistentDataType.STRING , instrument + ":" + varB);

        return block;
    }
}
