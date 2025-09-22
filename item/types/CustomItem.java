package pepgames.plugins.pepitemadder.item.types;

import dev.triumphteam.gui.builder.item.BaseItemBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import pepgames.plugins.TextUtil;
import pepgames.plugins.pepitemadder.item.BaseItem;
import pepgames.plugins.pepitemadder.item.builder.ItemBuilder;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class CustomItem extends BaseItem {

    @Getter private final ItemStack itemStack;

    public static String DISPLAY_NAME = "display_name" , LORE = "lore" , AMOUNT = "amount" , DURABILITY = "durability.max-durability" , MATERIAL = "model.material" , CUSTOM_MODEL_DATA = "model.custom_model_data" , IDENTIFIER = "identifier" , STACKABLE = "stackable";


    public CustomItem(ItemStack itemStack) {
        this.itemStack = itemStack; type = Type.CUSTOM;
    }

    @SneakyThrows
    public static CustomItem parseItem(File configFile , FileConfiguration config) {

        if (config.getKeys(false).isEmpty() || config.getKeys(false).size() > 1) return null;

        String itemPath = config.getKeys(false).stream().findFirst().orElse("");

        Component displayName = Objects.equals(config.getString(itemPath + "." + DISPLAY_NAME), "") ? null : TextUtil.format(config.getString(itemPath + "." + DISPLAY_NAME)) ;
        List<Component> lore = config.getStringList(itemPath + "." + LORE).stream().filter(Objects::nonNull).map(TextUtil::format).collect(Collectors.toList());

        Integer amount = config.getInt(itemPath + "." + AMOUNT);

        Material material = Material.valueOf(config.getString(itemPath + "." + MATERIAL));
        Integer modelData = config.getInt(itemPath + "." + CUSTOM_MODEL_DATA);

        Integer durability = config.getInt(itemPath + "." + DURABILITY);

        CustomItem item = displayName != null ? ItemBuilder.from(material).name(displayName).lore(lore).amount(amount).model(modelData).asCustomItem() : ItemBuilder.from(material).lore(lore).amount(amount).model(modelData).asCustomItem();

        ItemMeta itemMeta = item.itemStack.getItemMeta();
        if (itemMeta instanceof Damageable damageable)
            damageable.setDamage(durability);

        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);

        item.itemStack.setItemMeta(itemMeta);

        if (config.get(itemPath + "." + STACKABLE) != null) item.stackable = config.getBoolean(itemPath + "." + STACKABLE);

        if (config.getString(itemPath + "." + IDENTIFIER) == null) {
            item.identifier = CustomItem.generateIdentifier(); item.setIdentifier(item.identifier);
            config.set(itemPath + "." + IDENTIFIER , item.identifier); config.save(configFile);
        } else item.identifier = config.getString(itemPath + "." + IDENTIFIER); item.setIdentifier(item.identifier);

        return item;
    }

    private void setIdentifier(String identifier) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.getPersistentDataContainer().set(ITEM_IDENTIFIER , PersistentDataType.STRING , identifier);

        itemStack.setItemMeta(itemMeta);
    }

    private static String generateIdentifier(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder referenceCode = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            referenceCode.append(characters.charAt(index));
        }

        return referenceCode.toString();
    }

}
