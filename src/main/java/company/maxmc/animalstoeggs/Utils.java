package company.maxmc.animalstoeggs;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SanseYooyea
 */
public class Utils {
    public static List<Location> getAllChestsInChunk(Chunk chunk) {
        List<Location> chestLocation = new ArrayList<>();
        for (BlockState tileEntity : chunk.getTileEntities()) {
            if (tileEntity instanceof Chest) {
                chestLocation.add(tileEntity.getLocation());
            }
        }
        return chestLocation;
    }
}
