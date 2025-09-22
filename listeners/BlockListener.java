package pepgames.plugins.pepitemadder.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import pepgames.plugins.pepitemadder.PepItemAdder;
import pepgames.plugins.pepitemadder.item.BaseItem;
import pepgames.plugins.pepitemadder.item.types.BlockItem;

public class BlockListener implements Listener {

    private PepItemAdder plugin;

    public BlockListener(PepItemAdder plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this , plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (!event.getItemInHand().equals(Material.NOTE_BLOCK)) return;

        ItemMeta blockMeta = event.getItemInHand().getItemMeta();

        if (!blockMeta.getPersistentDataContainer().has(BlockItem.BLOCK_KEY , PersistentDataType.STRING)) return;

        NoteBlock placedBlock = (NoteBlock) event.getBlockPlaced().getBlockData();

        String[] blockKeys = blockMeta.getPersistentDataContainer().get(BlockItem.BLOCK_KEY , PersistentDataType.STRING).split(":");

        Instrument instrument = Instrument.valueOf( blockKeys[0].toUpperCase() );
        placedBlock.setInstrument(instrument);

        Integer noteValue = Integer.parseInt( blockKeys[1] );
        placedBlock.setNote(Note.natural(1 , Note.Tone.values()[noteValue % 7]));

        event.getBlockPlaced().setBlockData( placedBlock );
    }

}
