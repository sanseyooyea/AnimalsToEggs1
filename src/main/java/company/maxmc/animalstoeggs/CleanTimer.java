package company.maxmc.animalstoeggs;

import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.MonsterEggs;
import org.bukkit.material.SpawnEgg;

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

    private Random random = new Random();

//    @Deprecated
//    public Runnable cleanTask = () -> {
//
//        int maxAnimalAmount = Integer.parseInt(dataMap.get("max_animals").toString());
//
//        List<World> enableWorld = getEnabledWorlds();
//        for (World world : enableWorld) {
//            List<Entity> entities = Bukkit.getWorld(world.getName()).getEntities();
//            for (Entity e : entities) {
//                if (e instanceof Animals) {
//
//                    Animals a = (Animals) e;
//                    Entity[] entitiesInTheChunk = a.getLocation().getChunk().getEntities();
//                    Map<EntityType, Integer> animalsInTheChunk = getAnimalsInTheChunk(entitiesInTheChunk);
//
//                    //启用清理程序
//                    for (Map.Entry<EntityType, Integer> entityTypeIntegerEntry : animalsInTheChunk.entrySet()) {
//                        if (entityTypeIntegerEntry.getValue() >= maxAnimalAmount) {
//
//                            int amount = getEggAmountAfterChange();
//                            if (amount == 0) {
//                                continue;
//                            }
//
//                            ItemStack animalEggsItemStack = new ItemStack(Material.MONSTER_EGG);
//                            SpawnEggMeta meta = (SpawnEggMeta) animalEggsItemStack.getItemMeta();
//                            meta.setSpawnedType(entityTypeIntegerEntry.getKey());
//                            animalEggsItemStack.setAmount(amount);
//
//                            Chunk selectedChunk = e.getLocation().getChunk();
//                            HashMap<Integer, ItemStack> leftThings = new HashMap<>();
//                            for (int x = 0; x <= 15; x++) {
//                                for (int y = 0; y <= 15; y++) {
//                                    for (int z = 0; z <= 255; z++) {
//                                        if (Material.CHEST.equals(selectedChunk.getBlock(x, y, z).getType())) {
//                                            Chest chest = (Chest) selectedChunk.getBlock(x, y, z);
//                                            boolean isChestHasEmptySpace = chest.getInventory().firstEmpty() != -1;
//                                            HashMap<Integer, ItemStack> tempMap = chest.getInventory().addItem(animalEggsItemStack);
//                                            if (!isChestHasEmptySpace) {
//                                                continue;
//                                            }
//                                            if (tempMap.size() > 0) {
//                                                leftThings.put(Integer.parseInt(tempMap.keySet().toArray()[0].toString()), tempMap.get(0));
//                                            }
//                                            if (x == 15 && y == 15 && z == 255 && leftThings.size() > 0) {
//                                                e.getLocation().getBlock().setType(Material.CHEST);
//                                                Chest newChest = (Chest) e.getLocation().getBlock();
//                                                for (int k = 1; k < leftThings.size(); k++) {
//                                                    newChest.getInventory().addItem(leftThings.get(k));
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    };

    private final Runnable cleanRunnable = () -> {
        Bukkit.getConsoleSender().sendMessage("§8[§3ATE§8] §b开始清理...");
        int maxAnimalAmount = Integer.parseInt(dataMap.get("max_animals").toString());

        List<World> enableWorld = getEnabledWorlds();
        for (World world : enableWorld) {
            for (Chunk loadedChunk : world.getLoadedChunks()) {
                Map<EntityType, List<Entity>> animalsInTheChunk = getAnimalsInTheChunk0(loadedChunk.getEntities());
                for (Map.Entry<EntityType, List<Entity>> entityTypeIntegerEntry : animalsInTheChunk.entrySet()) {
                    if (entityTypeIntegerEntry.getValue().size() <= maxAnimalAmount) {
                        continue;
                    }

                    // generate chest
                    Location loc = entityTypeIntegerEntry.getValue().get(0).getLocation();
                    Location chestLoc = generateChest(loadedChunk, loc);

                    // remove entities
                    for (Entity entity : entityTypeIntegerEntry.getValue()) {
                        entity.remove();
                    }

                    // generate eggs
                    int amount = generateEggAmount(entityTypeIntegerEntry.getValue().size());
                    if (amount > 64) {
                        amount = 64;
                    }

                    // generate item
                    ItemStack animalEggsItemStack = new ItemStack(Material.MONSTER_EGG);
                    SpawnEggMeta meta = (SpawnEggMeta) animalEggsItemStack.getItemMeta();
                    meta.setSpawnedType(entityTypeIntegerEntry.getKey());
                    animalEggsItemStack.setItemMeta(meta);
                    animalEggsItemStack.setAmount(amount);

                    // add item into chest
                    ((Chest) chestLoc.getBlock().getState()).getBlockInventory().addItem(animalEggsItemStack);
                }
            }
        }
        Bukkit.getConsoleSender().sendMessage("§8[§3ATE§8] §a清理完成!");
    };

    private List<World> getEnabledWorlds() {
        return ((List<?>) dataMap.get("worlds")).stream().map((name) -> Bukkit.getWorld((String) name)).collect(Collectors.toList());
    }

    public Runnable getCleanRunnable() {
        return cleanRunnable;
    }

    @Deprecated
    private Map<EntityType, Integer> getAnimalsInTheChunk(Entity[] entitiesInTheChunk) {
        Map<EntityType, Integer> animalsInTheChunk = new HashMap<>();
        for (Entity entity : entitiesInTheChunk) {
            if (entity instanceof Animals) {
                EntityType type = entity.getType();
                int currentAmount = animalsInTheChunk.getOrDefault(type , 0);
                animalsInTheChunk.put(type, currentAmount+1);
            }
        }
        return animalsInTheChunk;
    }

    private Map<EntityType, List<Entity>> getAnimalsInTheChunk0(Entity[] entitiesInTheChunk) {
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
}
