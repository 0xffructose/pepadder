package pepgames.plugins.pepitemadder.item.container;

import dev.triumphteam.gui.guis.BaseGui;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import pepgames.plugins.pepitemadder.interfaces.ISheepDolly;

public abstract class Container {

    public enum Type { FUEL , STORAGE };
    @Getter public Type type;

    public int rows;
    public BaseGui containerGui;

    public static String CONTAINER_PATH = "container" , TYPE = "type" , ROWS = "rows" , FUEL_TYPE = "fuel_settings.fuel_type" , FUEL_MODEL_DATA = "fuel_settings.fuel_model_data";

    public static Container parseContainer(FileConfiguration config , String itemPath) {
        if (config.getConfigurationSection(itemPath + "." + CONTAINER_PATH) ==  null) return null;
        if (config.getConfigurationSection(itemPath + "." + CONTAINER_PATH).getKeys(false).isEmpty()) return null;

        Container.Type type = Container.Type.valueOf(config.getString(itemPath + "." + CONTAINER_PATH + "." + TYPE));
        if (type == null) return null;

        if (type.equals(Type.STORAGE)) {

            Integer rows = config.getInt(itemPath + "." + CONTAINER_PATH + "." + ROWS);

            StorageContainer storageContainer = new StorageContainer(rows);
            return storageContainer;

        } else if (type.equals(Type.FUEL)) {

            Integer rows = config.getInt(itemPath + "." + CONTAINER_PATH + "." + ROWS);
            Material fuelType = Material.valueOf(config.getString(itemPath + "." + CONTAINER_PATH + "." + FUEL_TYPE));
            Integer customModelData = config.getInt(itemPath + "." + CONTAINER_PATH + "." + FUEL_MODEL_DATA);

            FuelContainer fuelContainer = new FuelContainer(rows , fuelType , customModelData);
            return fuelContainer;
        }

        return null;
    }

}
