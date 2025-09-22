package pepgames.plugins.pepitemadder.mechanics;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public abstract class Mechanic {
    public abstract void processActions(@Nullable String condition , @NonNull Player player);
}
