package pepgames.plugins.pepitemadder.item;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pepgames.plugins.pepitemadder.item.types.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ItemParser {

    private static String ARMORS_FOLDER = "armors" , BLOCKS_FOLDER = "blocks" , FURNITURES_FOLDER = "furnitures" , ITEMS_FOLDER = "items";
    private static String CUSTOM_ITEMS_FOLDER = "custom" , TOOL_ITEMS_FOLDER = "tool";

    public static Map<String , BaseItem> parseAll(File contentsFolder) {
        if (!contentsFolder.isDirectory() || contentsFolder.listFiles() == null) return null;

        Map<String , BaseItem> items = new HashMap<>();

        for (File file : contentsFolder.listFiles()) {
            if (!file.isDirectory() || file.listFiles() == null) continue;

            // Parsing Items (Tools & Custom)
            if ( file.getName().equalsIgnoreCase(ITEMS_FOLDER) ) {

                for (File subFolder : file.listFiles()) {
                    if (!subFolder.isDirectory() || subFolder.listFiles() == null) continue;

                    if (subFolder.getName().equalsIgnoreCase(CUSTOM_ITEMS_FOLDER) ) {
                        for (File subFile : subFolder.listFiles()) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + subFile.getName());
                            if (!subFile.getName().endsWith(".yml")) continue;

                            FileConfiguration subFileConfig = YamlConfiguration.loadConfiguration(subFile);
                            CustomItem item = CustomItem.parseItem(subFile , subFileConfig);

                            items.put(item.identifier, item);
                            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + item.identifier + " | ITEM | LOADED");
                        }
                    }

                    if (subFolder.getName().equalsIgnoreCase(TOOL_ITEMS_FOLDER) ) {
                        for (File subFile : subFolder.listFiles()) {
                            if (subFile.isDirectory()) continue;

                            FileConfiguration subFileConfig = YamlConfiguration.loadConfiguration(subFile);
                            ToolItem tool = ToolItem.parseTool(subFile , subFileConfig);

                            items.put(tool.identifier , tool);
                            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + tool.identifier + " | TOOL | LOADED");
                        }
                    }
                }
            }

            if ( file.getName().equalsIgnoreCase(FURNITURES_FOLDER) ) {
                for (File subFile : file.listFiles()) {
                    if (subFile.isDirectory()) continue;

                    FileConfiguration subFileConfig = YamlConfiguration.loadConfiguration(subFile);
                    FurnitureItem furniture = FurnitureItem.parseFurniture(subFile , subFileConfig);

                    items.put(furniture.identifier , furniture);
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + furniture.identifier + " | FURNITURE | LOADED");
                }

            }

            if ( file.getName().equalsIgnoreCase(ARMORS_FOLDER) ) {

                for (File subFile : file.listFiles()) {
                    if (!subFile.isDirectory() || subFile.listFiles() == null) continue;

                    for (File armorSubFile : subFile.listFiles()) {
                        if (armorSubFile.isDirectory()) continue;

                        FileConfiguration armorSubFileConfig = YamlConfiguration.loadConfiguration(armorSubFile);
                        ArmorItem armor = ArmorItem.parseArmor(armorSubFile , armorSubFileConfig);

                        items.put(armor.identifier , armor);
                        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + armor.identifier + " | ARMOR | LOADED");
                    }

                }

            }

            if ( file.getName().equalsIgnoreCase(BLOCKS_FOLDER) ) {

                for (File subFile : file.listFiles()) {
                    if (subFile.isDirectory()) continue;

                    FileConfiguration config = YamlConfiguration.loadConfiguration(subFile);
                    BlockItem block = BlockItem.parseBlock(subFile , config);

                    items.put(block.identifier , block);
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + block.identifier + " | BLOCK | LOADED");

                }

            }
        }

        return items;
    }

}
