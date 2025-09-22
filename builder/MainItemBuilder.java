package pepgames.plugins.pepitemadder.item.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import pepgames.plugins.TextUtil;
import pepgames.plugins.pepitemadder.PepItemAdder;
import pepgames.plugins.pepitemadder.item.BaseItem;
import pepgames.plugins.pepitemadder.item.types.CustomItem;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class MainItemBuilder<B extends MainItemBuilder<B>> {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    private static Field DISPLAY_NAME_FIELD;
    private static Field LORE_FIELD;

    static {
        try {
            final Class<?> metaClass = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".inventory.CraftMetaItem");

            DISPLAY_NAME_FIELD = metaClass.getDeclaredField("displayName");
            DISPLAY_NAME_FIELD.setAccessible(true);

            LORE_FIELD = metaClass.getDeclaredField("lore");
            LORE_FIELD.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    protected MainItemBuilder(@NotNull final ItemStack itemStack) {
        if (itemStack == null) return;

        this.itemStack = itemStack;
        itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
    }

    public B name(final Component name) {
        if (itemMeta == null) return (B) this;

        try {
            DISPLAY_NAME_FIELD.set(itemMeta , TextUtil.serializeComponent(name));
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }

        return (B) this;
    }

    public B amount(final int amount) {
        itemStack.setAmount(amount);
        return (B) this;
    }

    public B lore(final List<Component> lore) {
        if (itemMeta == null) return (B) this;

        List<Object> serializedLore = lore.stream()
                .filter(Objects::nonNull)
                .map(TextUtil::serializeComponent)
                .collect(Collectors.toList());

        try {
            LORE_FIELD.set(itemMeta , serializedLore);
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }

        return (B) this;
    }

    public B model(final int modelData) {
        itemMeta.setCustomModelData(modelData);
        return (B) this;
    }

    public ItemStack build() {
        // itemMeta.getPersistentDataContainer().set(PepItemAdder.getInstance().getIdKey() , PersistentDataType.INTEGER , id);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public CustomItem asCustomItem() {
        return new CustomItem(build());
    }


}
