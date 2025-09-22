package pepgames.plugins.pepitemadder.mechanics.types;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pepgames.plugins.pepitemadder.interfaces.IAction;
import pepgames.plugins.pepitemadder.mechanics.Mechanic;

import java.util.List;

public class StaticMechanic extends Mechanic {

    @Getter private final List<IAction> actions;

    public StaticMechanic(List<IAction> actions) {
        this.actions = actions;
    }

    @Override
    public void processActions(@Nullable String condition , @NonNull Player player) {
        if (actions == null || actions.isEmpty()) return;
        for (IAction action : actions) {
            action.process(player);
        }
    }

}
