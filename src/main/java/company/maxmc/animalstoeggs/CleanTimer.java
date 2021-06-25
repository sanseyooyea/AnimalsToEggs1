package company.maxmc.animalstoeggs;

import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MonsterEggs;

import java.util.*;

/**
 * @author SanseYooyea
 */
public class CleanTimer {

    private LinkedHashMap<String, Object> dataMap;

    public CleanTimer(LinkedHashMap<String, Object> dataMap){
        this.dataMap = dataMap;
    }

    Runnable cleanTask = () -> {

        int maxAnimalAmount = Integer.parseInt(dataMap.get("max_animals").toString());

        List<World> enableWorld = getEnabledWorlds();
        for (World world : enableWorld){
            List<Entity> entities = Bukkit.getWorld(world.getName()).getEntities();
            for (Entity e : entities) {
                if (e instanceof Animals){

                    Animals a = (Animals) e;
                    Entity[] entitysInTheChunk = a.getLocation().getChunk().getEntities();
                    LinkedHashMap<Animals,Integer> animalsInTheChunk = getAnimalsInTheChunk(entitysInTheChunk);

                    //启用清理程序
                    for (int i = 0; i < animalsInTheChunk.size(); i++){
                        if (animalsInTheChunk.get(i) >= maxAnimalAmount){

                            Animals[] selectedAnimal = (Animals[]) animalsInTheChunk.keySet().toArray();
                            int amount = getEggAmountAfterChange();
                            if (amount ==0){
                                continue;
                            }
                            ItemStack animalEggsItemStack = new MonsterEggs(selectedAnimal[i].getEntityId()).toItemStack(amount);

                            Chunk selectdChunk = selectedAnimal[i].getLocation().getChunk();
                            HashMap<Integer, ItemStack> leftThings = new HashMap<>();
                            for (int x = 0; x <= 15; x++){
                                for (int y = 0; y <= 15; y++) {
                                    for (int z = 0; z <= 255; z++) {
                                        if ("CHEST".equals(selectdChunk.getBlock(x, y, z).getType().toString())){
                                            Chest chest = (Chest) selectdChunk.getBlock(x, y, z);
                                            boolean isChestHasEmptySpace = chest.getInventory().firstEmpty() != -1;
                                            HashMap<Integer, ItemStack> tempMap = chest.getInventory().addItem(animalEggsItemStack);
                                            if(!isChestHasEmptySpace){
                                                continue;
                                            }
                                            if (tempMap.size() > 0){
                                                leftThings.put(Integer.parseInt(tempMap.keySet().toArray()[0].toString()), tempMap.get(0));
                                            }
                                            if (x == 15 && y == 15 && z==255 && leftThings.size() > 0){
                                                selectedAnimal[0].getLocation().getBlock().setType(Material.CHEST);
                                                Chest newChest = (Chest) selectedAnimal[0].getLocation().getBlock();
                                                for (int k = 1; k < leftThings.size(); k++){
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

    private List<World> getEnabledWorlds(){
        List<World> enabledWorlds = new ArrayList<>();
        if (dataMap.get("worlds") instanceof ArrayList<?>){
            for(Object o:(List< ? >) dataMap.get("worlds")){
                enabledWorlds.add((World) o);
            }
        }
        return enabledWorlds;
    }

    private LinkedHashMap<Animals,Integer> getAnimalsInTheChunk(Entity[] entitysInTheChunk){
        LinkedHashMap<Animals,Integer> animalsInTheChunk = new LinkedHashMap<>();
        for (Entity ee : entitysInTheChunk) {
            if (ee instanceof Animals) {
                Animals aa = (Animals) ee;
                int currentAmount = animalsInTheChunk.get(aa);
                animalsInTheChunk.remove(aa);
                animalsInTheChunk.put(aa, currentAmount + 1);
            }
        }
        return animalsInTheChunk;
    }

    private int getEggAmountAfterChange() {
        int amount = 0;
        Random random = new Random();
        int chance = Integer.parseInt(dataMap.get("chance").toString());
        int maxNumber = 100 / chance;
        if (random.nextInt(maxNumber) == 0){
            amount ++;
        }
        return amount;
    }

}
