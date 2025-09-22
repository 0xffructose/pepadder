package pepgames.plugins.pepitemadder.mechanics.actions;

import org.bukkit.entity.Player;
import pepgames.plugins.TextUtil;
import pepgames.plugins.pepitemadder.PepItemAdder;
import pepgames.plugins.pepitemadder.interfaces.IAction;
import pepgames.plugins.pepitemadder.item.ticker.CountdownTicker;
import pepgames.plugins.pepitemadder.item.ticker.Ticker;
import pepgames.plugins.pepitemadder.item.types.ToolItem;

public class MessageAction implements IAction {

    private String identifier;
    private String params;

    public MessageAction(String params , String identifier) {
        this.params = params; this.identifier = identifier;
    }

    @Override
    public void process(Player player) {
        String[] seperatedParams = params.split(" ");

        switch (seperatedParams[0].toLowerCase()) {
            case "seconds-left":
                Ticker ticker = ToolItem.playerPackedFeature.get(player.getUniqueId()).get(identifier).ticker;
                if (ticker != null && ticker instanceof CountdownTicker) {
                    PepItemAdder.getInstance().adventure().player(player).sendMessage(
                            TextUtil.format("" + ((CountdownTicker) ticker).secondsLeft )
                    );
                }
                break;
            default:
                PepItemAdder.getInstance().adventure().player(player).sendMessage(
                        TextUtil.format(params)
                );
                break;
        }

    }
}
