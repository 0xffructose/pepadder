package pepgames.plugins.pepitemadder.item.ticker;

import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pepgames.plugins.pepitemadder.interfaces.IAction;
import pepgames.plugins.pepitemadder.interfaces.ISheepDolly;
import pepgames.plugins.pepitemadder.mechanics.actions.ActionsParser;

import java.util.List;

public class CountdownTicker extends Ticker implements ISheepDolly {

    public Integer time , secondsLeft;
    @Setter private List<IAction> beforeActions , afterActions;

    public CountdownTicker(@Nullable Player player , @NonNull Integer startDelay , @NonNull Integer period , @NonNull Integer countdownFrom) {
        attachedPlayer = player;

        this.startDelay = startDelay; this.period = period;
        time = countdownFrom; secondsLeft = countdownFrom;
    }

    public CountdownTicker(@Nullable Player player , @NonNull Integer startDelay , @NonNull Integer period , @NonNull Integer countdownFrom , List<IAction> eachTickActions , List<IAction> beforeActions , List<IAction> afterActions ) {
        attachedPlayer = player;

        this.startDelay = startDelay; this.period = period;
        time = countdownFrom; secondsLeft = countdownFrom;

        actionsForEachTick = eachTickActions; this.beforeActions = beforeActions; this.afterActions = afterActions;
    }

    public void addSeconds(@NonNull Integer seconds) {
        secondsLeft += seconds;
    }

    @Override
    public void handle() {
        if (secondsLeft == time && beforeActions != null) ActionsParser.runActions(beforeActions , attachedPlayer);

        secondsLeft--;

        if (secondsLeft < 1 && afterActions != null) { ActionsParser.runActions(afterActions , attachedPlayer); cancel(); return; }

        ActionsParser.runActions(actionsForEachTick , attachedPlayer);
    }

    @Override
    public CountdownTicker clone() {
        return new CountdownTicker(null , startDelay , period , time , actionsForEachTick , beforeActions , afterActions);
    }
}
