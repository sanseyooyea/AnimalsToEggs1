package company.maxmc.animalstoeggs;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<Location> getAllChestsInChunk(Chunk chunk) {
        List<Location> chestLoc = new ArrayList<>();
        for (BlockState tileEntity : chunk.getTileEntities()) {
            if (tileEntity instanceof Chest) {
                chestLoc.add(tileEntity.getLocation());
            }
        }
        return chestLoc;
    }
}
