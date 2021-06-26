package company.maxmc.animalstoeggs;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author SanseYooyea
 */
public final class AnimalsToEggs extends JavaPlugin {

    private static AnimalsToEggs INSTANCE;
    private static Map<String, Object> dataMap = new HashMap<>();

    public AnimalsToEggs() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("AnimalsToEggs is running...");
        System.out.println("qq:1187586838");

        Objects.requireNonNull(this.getCommand("ate")).setExecutor(new PluginCommand());

        saveDefaultConfig();
        dataMap = loadDataMap();
        CleanTimer ct = new CleanTimer(dataMap);

        ScheduledExecutorService cleanTimer = Executors.newScheduledThreadPool(10);
        cleanTimer.scheduleAtFixedRate(ct.cleanTask,Long.parseLong(dataMap.get("interval").toString()),
                Long.parseLong(dataMap.get("interval").toString()), TimeUnit.SECONDS);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("AnimalsToEggs is stopping...");
        System.out.println("qq:1187586838");
    }

    private Map<String, Object> loadDataMap(){
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("worlds",getConfig().get("worlds"));
        tempMap.put("max_animals",getConfig().get("max_animals"));
        tempMap.put("chance",getConfig().get("chance"));
        tempMap.put("interval",getConfig().get("interval"));
        return tempMap;
    }

    public static Map<String, Object> getDataMap() {
        return dataMap;
    }

    public static void reloadConfig0() {
        INSTANCE.reloadConfig();
        dataMap = INSTANCE.loadDataMap();
    }

    public static AnimalsToEggs getINSTANCE() {
        return INSTANCE;
    }
}
