package company.maxmc.animalstoeggs;

import company.maxmc.animalstoeggs.command.PluginCommand;
import company.maxmc.animalstoeggs.timer.CleanTimer;
import company.maxmc.animalstoeggs.timer.ShoutTimer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author SanseYooyea
 */
public final class AnimalsToEggs extends JavaPlugin {

    private static AnimalsToEggs instance;
    private static Map<String, Object> dataMap = new HashMap<>();

    public AnimalsToEggs() {
        instance = this;
    }

    @Override
    public void onEnable() {
        getLogger().info("AnimalsToEggs 插件启动中...");
        getLogger().info("插件作者：SanseYooyea");
        getLogger().info("作者QQ：1187586838");

        Objects.requireNonNull(this.getCommand("ate")).setExecutor(new PluginCommand());

        saveDefaultConfig();
        loadDataMap();

        CleanTimer ct = new CleanTimer(dataMap);
        ShoutTimer st = new ShoutTimer(dataMap);
        ScheduledExecutorService cleanTimer = Executors.newScheduledThreadPool(2);
        ScheduledExecutorService shoutTimer = Executors.newScheduledThreadPool(1);
        long shoutTimerInitialDelay = Long.parseLong(dataMap.get("interval").toString())-Long.parseLong(dataMap.get("advance_notice_time").toString());
        cleanTimer.scheduleAtFixedRate(ct.getCleanRunnable(),
                Long.parseLong(dataMap.get("interval").toString()),
                Long.parseLong(dataMap.get("interval").toString()),
                TimeUnit.SECONDS);
        shoutTimer.scheduleAtFixedRate(st.getShoutRunnable(),
                shoutTimerInitialDelay,
                Long.parseLong(dataMap.get("interval").toString()),
                TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        getLogger().info("AnimalsToEggs 插件关闭中...");
        getLogger().info("插件作者：SanseYooyea");
        getLogger().info("作者QQ：1187586838");
    }

    public void loadDataMap(){
        if (!dataMap.isEmpty()) {
            dataMap.clear();
        }

        dataMap.put("worlds",getConfig().get("worlds"));
        dataMap.put("max_animals",getConfig().get("max_animals"));
        dataMap.put("chance",getConfig().get("chance"));
        dataMap.put("interval",getConfig().get("interval"));
        dataMap.put("advance_notice_time",getConfig().get("advance_notice_time"));
        dataMap.put("clean_message",getConfig().get("clean_message"));
    }

    public static Map<String, Object> getDataMap() {
        return dataMap;
    }

    public static AnimalsToEggs getInstance() {
        return instance;
    }
}
