package pepgames.plugins.pepitemadder.item.ticker;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pepgames.plugins.pepitemadder.interfaces.IAction;
import pepgames.plugins.pepitemadder.interfaces.ISheepDolly;
import pepgames.plugins.pepitemadder.mechanics.actions.ActionsParser;

import java.util.List;

public class StandardTicker extends Ticker implements ISheepDolly {

    public StandardTicker(@Nullable Player player , @NonNull Integer startDelay , @NonNull Integer period) {
        attachedPlayer = player;
        this.startDelay = startDelay; this.period = period;
    }

    public StandardTicker(@Nullable Player player , @NonNull Integer startDelay , @NonNull Integer period , @NonNull List<IAction> eachTickActions) {
        attachedPlayer = player;
        this.startDelay = startDelay; this.period = period;
        actionsForEachTick = eachTickActions;
    }

    @Override
    public void handle() {
        ActionsParser.runActions(actionsForEachTick , attachedPlayer);
    }

    @Override
    public StandardTicker clone() {
        return new StandardTicker(null , startDelay , period , actionsForEachTick);
    }
}
