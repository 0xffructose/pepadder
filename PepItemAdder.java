package pepgames.plugins.pepitemadder;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import pepgames.plugins.ModernConfigManager;
import pepgames.plugins.commands.CommandHandler;
import pepgames.plugins.pepItemAdderAPI;
import pepgames.plugins.pepitemadder.commands.Commands;
import pepgames.plugins.pepitemadder.listeners.BlockListener;
import pepgames.plugins.pepitemadder.listeners.DefaultListener;
import pepgames.plugins.pepitemadder.listeners.FurnitureListener;
import pepgames.plugins.pepitemadder.listeners.SaveListener;
import pepgames.plugins.pepitemadder.managers.ItemManager;
import pepgames.plugins.pepitemadder.managers.WorldManager;

public final class PepItemAdder extends JavaPlugin {

    @Getter private static PepItemAdder instance; BukkitAudiences adventure;
    @Getter private ProtocolManager protocol;

    @Getter private ModernConfigManager configManager; @Getter private ItemManager itemManager;
    @Getter private WorldManager worldManager;

    private DefaultListener defaultListener; private FurnitureListener furnitureListener; private BlockListener blockListener;
    private SaveListener saveListener;

    @Override
    public void onEnable() {
        instance = this; adventure = BukkitAudiences.create(this);
        // protocol = ProtocolLibrary.getProtocolManager();

        // Managers
        configManager = new ModernConfigManager(this);
        itemManager = new ItemManager(this);
        worldManager = new WorldManager(this);

        // Commands
        CommandHandler.registerCommands(Commands.class , this);

        // Listeners
        defaultListener = new DefaultListener(this);
        furnitureListener = new FurnitureListener(this);
        blockListener = new BlockListener(this);

        saveListener = new SaveListener(this);

        Bukkit.getServicesManager().register(pepItemAdderAPI.class , new PepItemAdderApiImpl(this) , this , ServicePriority.Highest);
    }

    @Override
    public void onDisable() {
        instance = null; // protocol.removePacketListeners(this);


        // worldManager.onDisable();
    }

    public @NonNull BukkitAudiences adventure() {
        if (adventure == null) throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        return adventure;
    }
}
