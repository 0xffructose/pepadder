package pepgames.plugins.pepitemadder.mechanics.types;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pepgames.plugins.pepitemadder.interfaces.IAction;
import pepgames.plugins.pepitemadder.mechanics.Mechanic;

import java.util.List;
import java.util.Map;

@Getter
public final class ConditionalMechanic extends Mechanic {

    final Map<String , List<IAction>> conditionToActions;

    public ConditionalMechanic(@NonNull final Map<String , List<IAction>> conditionToActions) {
        this.conditionToActions = conditionToActions;
    }

    @Override
    public void processActions(@Nullable final String condition , @NonNull final Player player) {
        if (conditionToActions.get(condition) == null || conditionToActions.get(condition).isEmpty()) return;
        for (IAction action : conditionToActions.get(condition)) {
            action.process(player);
        }
    }
}
