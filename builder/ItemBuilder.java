package pepgames.plugins.pepitemadder.item.builder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemBuilder extends MainItemBuilder<ItemBuilder> {

    ItemBuilder(final ItemStack itemStack) {
        super(itemStack);
    }

    public static ItemBuilder from(final Material material) {
        return new ItemBuilder(new ItemStack(material));
    }
}
