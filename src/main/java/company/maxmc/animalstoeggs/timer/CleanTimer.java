package company.maxmc.animalstoeggs.timer;

import company.maxmc.animalstoeggs.Utils;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SanseYooyea
 */
public class CleanTimer {

    private Map<String, Object> dataMap;

    public CleanTimer(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    private final Random random = new Random();

    private final Runnable cleanRunnable = () -> {
        Bukkit.getConsoleSender().sendMessage("§8[§3ATE§8] §b开始清理...");
        int maxAnimalAmount = Integer.parseInt(dataMap.get("max_animals").toString());

        List<World> enableWorld = getEnabledWorlds();
        for (World world : enableWorld) {
            if (world == null){
                continue;
            }

            for (Chunk loadedChunk : world.getLoadedChunks()) {
                Map<EntityType, List<Entity>> animalsInTheChunk = getAnimalsInTheChunk(loadedChunk.getEntities());
                for (Map.Entry<EntityType, List<Entity>> entityTypeIntegerEntry : animalsInTheChunk.entrySet()) {

                    int  animalAmount = entityTypeIntegerEntry.getValue().size();
                    int tempAmount = animalAmount;

                    if (animalAmount <= maxAnimalAmount) {
                        continue;
                    }

                    // generate chest
                    Location entityLocation = entityTypeIntegerEntry.getValue().get(0).getLocation();
                    Location chestLocation = generateChest(loadedChunk, entityLocation);

                    // remove entities until left %MAX_AMOUNT% animals
                    for (Entity entity : entityTypeIntegerEntry.getValue()) {
                        entity.remove();
                        tempAmount--;
                        if (tempAmount <= maxAnimalAmount){
                            break;
                        }
                    }

                    // generate eggs
                    int amount = generateEggAmount(animalAmount-maxAnimalAmount);
                    if (amount > 64) {
                        amount = 64;
                    }
                    if (amount == 0){
                        continue;
                    }

                    // generate item
                    ItemStack animalEggsItemStack = new ItemStack(Material.MONSTER_EGG);
                    SpawnEggMeta meta = (SpawnEggMeta) animalEggsItemStack.getItemMeta();
                    meta.setSpawnedType(entityTypeIntegerEntry.getKey());
                    animalEggsItemStack.setItemMeta(meta);
                    animalEggsItemStack.setAmount(amount);

                    // add item into chest
                    ((Chest) chestLocation.getBlock().getState()).getBlockInventory().addItem(animalEggsItemStack);
                }
            }
        }
        Bukkit.getConsoleSender().sendMessage("§8[§3ATE§8] §a清理完成!");
    };

    public Runnable getCleanRunnable() {
        return cleanRunnable;
    }

    private List<World> getEnabledWorlds() {
        return ((List<?>) dataMap.get("worlds")).stream().map((name) -> Bukkit.getWorld((String) name)).collect(Collectors.toList());
    }

    private Map<EntityType, List<Entity>> getAnimalsInTheChunk(Entity[] entitiesInTheChunk) {
        Map<EntityType, List<Entity>> animalsInTheChunk = new HashMap<>();
        for (Entity entity : entitiesInTheChunk) {
            if (entity instanceof Animals) {
                EntityType type = entity.getType();
                @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
                List<Entity> current = animalsInTheChunk.computeIfAbsent(type , (k) -> new ArrayList<>());
                current.add(entity);
            }
        }
        return animalsInTheChunk;
    }

    private Location generateChest(Chunk chunk, Location target) {
        List<Location> allChestsInChunk = Utils.getAllChestsInChunk(chunk);
        for (Location location : allChestsInChunk) {
            Chest chest = (Chest) location.getBlock().getState();
            Inventory blockInventory = chest.getBlockInventory();
            if (blockInventory.firstEmpty() != -1) {
                return chest.getLocation();
            }
        }
        target.getBlock().setType(Material.CHEST);
        return target;
    }

    private int generateEggAmount(int total) {
        int result = 0;
        for (int i = 0; i < total; i++) {
            final int chance = (int) dataMap.get("chance");
            int value = random.nextInt(100);
            if (value < chance) {
                result++;
            }
        }
        return result;
    }
}
