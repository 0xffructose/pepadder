package pepgames.plugins.pepitemadder.managers;

import pepgames.plugins.pepitemadder.PepItemAdder;
import pepgames.plugins.pepitemadder.interfaces.IDisableable;

public class WorldManager implements IDisableable {

    private PepItemAdder plugin;

    public WorldManager(PepItemAdder plugin) {
        this.plugin = plugin;



    }

    @Override
    public void onDisable() {

    }
}
