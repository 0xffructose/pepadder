package pepgames.plugins.pepitemadder.item.types;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import pepgames.plugins.pepitemadder.item.BaseItem;
import java.io.File;
import java.util.UUID;

public class ArmorItem extends BaseItem {

    private final CustomItem item;
    private int decimalColor;
    private double armorProtection , armorToughness;
    private EquipmentSlot armorSlot;

    private static String ARMOR = "armor" , HELMET = "head" , CHESTPLATE = "chest" , LEGGINGS = "legs" , BOOTS = "feet" , COLOR = "color" , ARMOR_PROTECTION = "protection" , ARMOR_TOUGHNESS = "toughness";

    public ArmorItem(CustomItem item) {
        this.item = item; identifier = item.identifier;
        type = Type.ARMOR;
    }

    public ItemStack getItemStack() {
        return item.getItemStack();
    }

    public static ArmorItem parseArmor(File configFile , FileConfiguration config) {

        if (config.getKeys(false).isEmpty() || config.getKeys(false).size() > 1) return null;

        String itemPath = config.getKeys(false).stream().findFirst().orElse("");

        Material materialType = Material.valueOf( config.getString(itemPath + "." + CustomItem.MATERIAL) );

        if (
                !materialType.equals(Material.LEATHER_HELMET) &&
                !materialType.equals(Material.LEATHER_CHESTPLATE) &&
                !materialType.equals(Material.LEATHER_LEGGINGS) &&
                !materialType.equals(Material.LEATHER_BOOTS)
        ) return null;

        ArmorItem armor = new ArmorItem(CustomItem.parseItem(configFile , config));
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) armor.item.getItemStack().getItemMeta();

        if (config.getConfigurationSection(itemPath + "." + ARMOR).getKeys(false).stream().toList().get(0) != null) {
            armor.armorSlot = EquipmentSlot.valueOf( config.getConfigurationSection(itemPath + "." + ARMOR).getKeys(false).stream().toList().get(0).toUpperCase() );
        }

        if (config.get(itemPath + "." + ARMOR + "." + armor.armorSlot.name().toLowerCase() + "." + COLOR) != null) {
            armorMeta.setColor( getColorFromHex( config.getString(itemPath + "." + ARMOR + "." + armor.armorSlot.name().toLowerCase() + "." + COLOR) ) );
        }

        if (config.get(itemPath + "." + ARMOR + "." + armor.armorSlot.name().toLowerCase() + "." + ARMOR_PROTECTION) != null) {
            armor.armorProtection = config.getDouble(itemPath + "." + ARMOR + "." + armor.armorSlot.name().toLowerCase() + "." + ARMOR_PROTECTION);

            AttributeModifier armorModifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "custom_armor_protection",
                    armor.armorProtection,
                    AttributeModifier.Operation.ADD_NUMBER,
                    armor.armorSlot
            );

            armorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR , armorModifier);
        }

        if (config.get(itemPath + "." + ARMOR + "." + armor.armorSlot.name().toLowerCase() + "." + ARMOR_TOUGHNESS) != null) {

            armor.armorToughness = config.getDouble(itemPath + "." + ARMOR + "." + armor.armorSlot.name().toLowerCase() + "." + ARMOR_TOUGHNESS);

            AttributeModifier armorToughnessModifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "custom_armor_toughness",
                    armor.armorToughness,
                    AttributeModifier.Operation.ADD_NUMBER,
                    armor.armorSlot
            );

            armorMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS , armorToughnessModifier);
        }

        armor.item.getItemStack().setItemMeta(armorMeta);

        return armor;
    }

    private static Color getColorFromHex(String hexString) {
        if (hexString.startsWith("#")) hexString = hexString.substring(1);

        if (hexString.length() != 6) throw new IllegalArgumentException("Hex rengi 6 karakter uzunluğunda olmalıdır!");

        int red = Integer.parseInt(hexString.substring(0 , 2) , 16);
        int green = Integer.parseInt(hexString.substring(2 , 4) , 16);
        int blue = Integer.parseInt(hexString.substring(4 , 6) , 16);

        return Color.fromRGB(red , green , blue);
    }
}
