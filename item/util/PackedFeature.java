package pepgames.plugins.pepitemadder.item.util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pepgames.plugins.pepitemadder.interfaces.ISheepDolly;
import pepgames.plugins.pepitemadder.item.container.Container;
import pepgames.plugins.pepitemadder.item.container.FuelContainer;
import pepgames.plugins.pepitemadder.item.container.StorageContainer;
import pepgames.plugins.pepitemadder.item.ticker.CountdownTicker;
import pepgames.plugins.pepitemadder.item.ticker.StandardTicker;
import pepgames.plugins.pepitemadder.item.ticker.Ticker;

import java.util.List;

public class PackedFeature implements ISheepDolly {

    public final Container container;
    public final Ticker ticker;


    public PackedFeature() {
        container = null; ticker = null;
    }

    public PackedFeature(@Nullable Container container) {
        this.container = container;
        ticker = null;
    }

    public PackedFeature(@Nullable Container container , @Nullable Ticker ticker) {
        this.container = container;
        this.ticker = ticker;
    }

    public static PackedFeature parseFeatures(FileConfiguration config , String itemPath , String identifier) {
        if (config.getConfigurationSection(itemPath + "." + Container.CONTAINER_PATH).getKeys(false).isEmpty() && config.getConfigurationSection(itemPath + "." + Ticker.TICKER_PATH).getKeys(false).isEmpty()) return null;

        Container _container = Container.parseContainer(config , itemPath);
        Ticker _ticker = Ticker.parseTicker(config , itemPath , identifier);

        if (_ticker instanceof CountdownTicker && _container instanceof FuelContainer) {
            ((FuelContainer) _container).attachedTo = ((CountdownTicker) _ticker);
        }

        PackedFeature packedFeature = new PackedFeature(_container , _ticker);

        return packedFeature;
    }

    public void adaptFeature(Player player , @Nullable List<ItemStack> contents, @Nullable Integer secondsLeft) {
        if (ticker != null)
            ticker.attachedPlayer = player;

        if (ticker != null && ticker instanceof CountdownTicker && secondsLeft != null) {
            CountdownTicker countdownTicker = (CountdownTicker) ticker;
            countdownTicker.time = secondsLeft; countdownTicker.secondsLeft = secondsLeft;
        }

        if (container != null && container instanceof StorageContainer && contents != null && !contents.isEmpty()) {
            StorageContainer storageContainer = (StorageContainer) container;
            storageContainer.containerGui.getInventory().setContents((ItemStack[]) contents.toArray());
        }
    }

    @Override
    public PackedFeature clone() {

        Container copiedContainer = null;
        if (container instanceof StorageContainer) copiedContainer = (StorageContainer) ((StorageContainer) container).clone();
        else if (container instanceof FuelContainer) copiedContainer = (FuelContainer) ((FuelContainer) container).clone();

        Ticker copiedTicker = null;
        if (ticker instanceof StandardTicker) copiedTicker = (StandardTicker) ((StandardTicker) ticker).clone();
        else if (ticker instanceof CountdownTicker) copiedTicker = (CountdownTicker) ((CountdownTicker) ticker).clone();

        if (copiedContainer instanceof FuelContainer && copiedTicker instanceof CountdownTicker) {
            ((FuelContainer) copiedContainer).attachedTo = (CountdownTicker) copiedTicker;
        }

        return new PackedFeature(copiedContainer , copiedTicker);
    }

    public boolean isNull() {
        return container == null && ticker == null;
    }
}
