package pepgames.plugins.pepitemadder.mechanics.actions;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import pepgames.plugins.TextUtil;
import pepgames.plugins.pepitemadder.PepItemAdder;
import pepgames.plugins.pepitemadder.interfaces.IAction;
import pepgames.plugins.pepitemadder.item.ticker.Ticker;
import pepgames.plugins.pepitemadder.item.types.ToolItem;

public class TickerAction implements IAction {

    private String params , identifier;

    public TickerAction(String params , String identifier) {
        this.params = params; this.identifier = identifier;
    }

    @Override
    public void process(Player player) {
        String[] seperatedParams = params.split(" ");
        switch (seperatedParams[0].toLowerCase()) {
            case "change":
                Ticker ticker = ToolItem.playerPackedFeature.get(player.getUniqueId()).get(identifier).ticker;

                if (ticker.isRunning()) ticker.cancel();
                else ticker.scheduleTimer(PepItemAdder.getInstance() , ticker.startDelay , ticker.period);

                break;
        }
    }
}
