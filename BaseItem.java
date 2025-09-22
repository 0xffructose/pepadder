package pepgames.plugins.pepitemadder.item;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import pepgames.plugins.pepitemadder.PepItemAdder;

public abstract class BaseItem {

    public enum Type { CUSTOM , TOOL , ARMOR , BLOCK , FURNITURE , BACKPACK };
    public static NamespacedKey ITEM_IDENTIFIER = new NamespacedKey(PepItemAdder.getInstance() , "pia_identifier");

    @Getter public Type type;
    public String identifier; public Boolean stackable;

}
