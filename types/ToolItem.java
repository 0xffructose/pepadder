package pepgames.plugins.pepitemadder.item.types;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import pepgames.plugins.pepitemadder.item.BaseItem;
import pepgames.plugins.pepitemadder.item.util.PackedFeature;
import pepgames.plugins.pepitemadder.mechanics.Mechanic;
import pepgames.plugins.pepitemadder.mechanics.MechanicParser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ToolItem extends BaseItem {

    private final CustomItem item;
    @Getter private Map<String , Mechanic> mechanics;
    @Getter private PackedFeature packedFeature;

    public static Map<UUID , Map<String , PackedFeature>> playerPackedFeature = new HashMap<>();

    public ToolItem(CustomItem item) {
        this.item = item; identifier = item.identifier;
        type = Type.TOOL;

        mechanics = new HashMap<>();
    }

    public ItemStack getItemStack() {
        return item.getItemStack();
    }

    public static ToolItem parseTool(File configFile , FileConfiguration config) {

        if (config.getKeys(false).isEmpty() || config.getKeys(false).size() > 1) return null;

        String itemPath = config.getKeys(false).stream().findFirst().orElse("");

        ToolItem tool = new ToolItem(CustomItem.parseItem(configFile , config));

        tool.mechanics = MechanicParser.parseMechanic(config , itemPath , tool.identifier);
        tool.packedFeature = PackedFeature.parseFeatures(config , itemPath , tool.identifier);

        return tool;
    }
}
