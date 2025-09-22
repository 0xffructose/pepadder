package pepgames.plugins.pepitemadder.item.container;

import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import pepgames.plugins.pepitemadder.interfaces.ISheepDolly;

public class StorageContainer extends Container implements ISheepDolly  {

    public StorageContainer(int rows) {
        this.rows = rows; this.type = Type.STORAGE;
        containerGui = Gui.storage().title(Component.text("Storage")).rows(rows).create();
    }

    @Override
    public Object clone() {
        return new StorageContainer(rows);
    }
}
