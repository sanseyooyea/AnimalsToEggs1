package company.maxmc.animalstoeggs;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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

    Runnable cleanTask = () -> {

        int maxAnimalAmount = Integer.parseInt(dataMap.get("max_animals").toString());

        List<World> enableWorld = getEnabledWorlds();
        for (World world : enableWorld) {
            List<Entity> entities = Bukkit.getWorld(world.getName()).getEntities();
            for (Entity e : entities) {
                if (e instanceof Animals) {

                    Animals a = (Animals) e;
                    Entity[] entitiesInTheChunk = a.getLocation().getChunk().getEntities();
                    Map<EntityType, Integer> animalsInTheChunk = getAnimalsInTheChunk(entitiesInTheChunk);

                    //启用清理程序
                    for (Map.Entry<EntityType, Integer> entityTypeIntegerEntry : animalsInTheChunk.entrySet()) {
                        if (entityTypeIntegerEntry.getValue() >= maxAnimalAmount) {

                            int amount = getEggAmountAfterChange();
                            if (amount == 0) {
                                continue;
                            }

                            ItemStack animalEggsItemStack = new ItemStack(Material.MONSTER_EGG);
                            SpawnEggMeta meta = (SpawnEggMeta) animalEggsItemStack.getItemMeta();
                            meta.setSpawnedType(entityTypeIntegerEntry.getKey());
                            animalEggsItemStack.setAmount(amount);

                            Chunk selectedChunk = e.getLocation().getChunk();
                            HashMap<Integer, ItemStack> leftThings = new HashMap<>();
                            for (int x = 0; x <= 15; x++) {
                                for (int y = 0; y <= 15; y++) {
                                    for (int z = 0; z <= 255; z++) {
                                        if (Material.CHEST.equals(selectedChunk.getBlock(x, y, z).getType())) {
                                            Chest chest = (Chest) selectedChunk.getBlock(x, y, z);
                                            boolean isChestHasEmptySpace = chest.getInventory().firstEmpty() != -1;
                                            HashMap<Integer, ItemStack> tempMap = chest.getInventory().addItem(animalEggsItemStack);
                                            if (!isChestHasEmptySpace) {
                                                continue;
                                            }
                                            if (tempMap.size() > 0) {
                                                leftThings.put(Integer.parseInt(tempMap.keySet().toArray()[0].toString()), tempMap.get(0));
                                            }
                                            if (x == 15 && y == 15 && z == 255 && leftThings.size() > 0) {
                                                e.getLocation().getBlock().setType(Material.CHEST);
                                                Chest newChest = (Chest) e.getLocation().getBlock();
                                                for (int k = 1; k < leftThings.size(); k++) {
                                                    newChest.getInventory().addItem(leftThings.get(k));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    };

    private List<World> getEnabledWorlds() {
        return ((List<?>) dataMap.get("worlds")).stream().map((name) -> Bukkit.getWorld((String) name)).collect(Collectors.toList());
    }

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

    private int getEggAmountAfterChange() {
        int amount = 0;
        Random random = new Random();
        int chance = Integer.parseInt(dataMap.get("chance").toString());
        int maxNumber = 100 / chance;
        if (random.nextInt(maxNumber) == 0) {
            amount++;
        }
        return amount;
    }

}
