package pepgames.plugins.pepitemadder.item.container;

import dev.triumphteam.gui.guis.Gui;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pepgames.plugins.pepitemadder.interfaces.ISheepDolly;
import pepgames.plugins.pepitemadder.item.ticker.CountdownTicker;

public class FuelContainer extends Container implements ISheepDolly {

    public CountdownTicker attachedTo;
    public Material fuelType; public Integer customModelData;

    public FuelContainer(@NonNull Integer rows , @NonNull Material fuelType , @NonNull Integer customModelData) {
        this.rows = rows; this.type = Type.FUEL;
        this.fuelType = fuelType; this.customModelData = customModelData;

        containerGui = Gui.gui().title(Component.text("Fuel")).rows(rows).create();
        containerGui.setCloseGuiAction(event -> {
            for (ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack == null) continue;
                if (itemStack.getType().equals(fuelType) && itemStack.getItemMeta().getCustomModelData() == customModelData) {

                    attachedTo.time += itemStack.getAmount();
                    attachedTo.secondsLeft += itemStack.getAmount();

                    Bukkit.broadcastMessage("ADDED " + itemStack.getAmount());
                } else event.getPlayer().getInventory().addItem(itemStack);
            }
        });
    }

    public FuelContainer(@NonNull Integer rows , @NonNull Material fuelType , @NonNull Integer customModelData , @Nullable CountdownTicker countdownTicker) {
        this.rows = rows; this.type = Type.FUEL;
        this.fuelType = fuelType; this.customModelData = customModelData;
        attachedTo = countdownTicker;

        containerGui = Gui.gui().title(Component.text("Fuel")).rows(rows).create();
        containerGui.setCloseGuiAction(event -> {
            for (ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack == null) continue;
                if (itemStack.getType().equals(fuelType) && itemStack.getItemMeta().getCustomModelData() == customModelData) {

                    attachedTo.time += itemStack.getAmount();
                    attachedTo.secondsLeft += itemStack.getAmount();

                    Bukkit.broadcastMessage("ADDED " + itemStack.getAmount());
                } else event.getPlayer().getInventory().addItem(itemStack);
            }
        });
    }

    @Override
    public Object clone() {
        return new FuelContainer(rows , fuelType , customModelData);
    }
}
