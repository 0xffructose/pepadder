package pepgames.plugins.pepitemadder.item.util;

import lombok.NonNull;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceLimitations {

    public List<BlockFace> limitedFaces;

    private static BlockFace floorFace = BlockFace.UP , ceilingFace = BlockFace.DOWN;
    private static List<BlockFace> wallFaces = Arrays.asList(BlockFace.NORTH , BlockFace.SOUTH , BlockFace.WEST , BlockFace.EAST);

    public PlaceLimitations(@NonNull Boolean floor , @NonNull Boolean ceiling , @NonNull Boolean walls) {
        limitedFaces = new ArrayList<>();

        if (!floor) limitedFaces.add(floorFace);
        if (!ceiling) limitedFaces.add(ceilingFace);
        if (!walls) limitedFaces.addAll(wallFaces);

    }

    public PlaceLimitations() {
        limitedFaces = new ArrayList<>();
    }

    public void setLimitedFaces(@NonNull Boolean floor , @NonNull Boolean ceiling , @NonNull Boolean walls) {
        if (!floor) limitedFaces.add(floorFace);
        if (!ceiling) limitedFaces.add(ceilingFace);
        if (!walls) limitedFaces.addAll(wallFaces);
    }

}
