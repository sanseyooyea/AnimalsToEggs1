package company.maxmc.animalstoeggs.timer;

import org.bukkit.Bukkit;

import java.util.Map;

/**
 * @author SanseYooyea
 */
public class ShoutTimer {

    private Map<String,Object> dataMap;

    public ShoutTimer(Map<String,Object> dataMap) {
        this.dataMap = dataMap;
    }

    private final Runnable shoutRunnable = () ->
            Bukkit.broadcastMessage(String.format(dataMap.get("clean_message").toString(), Integer.parseInt(dataMap.get("advance_notice_time").toString())));

    public Runnable getShoutRunnable() {
        return shoutRunnable;
    }
}