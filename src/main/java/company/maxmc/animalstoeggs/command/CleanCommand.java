package company.maxmc.animalstoeggs.command;

import company.maxmc.animalstoeggs.AnimalsToEggs;
import company.maxmc.animalstoeggs.timer.CleanTimer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SanseYooyea
 */
public class CleanCommand {

    public static void clean(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CleanTimer ct = new CleanTimer(AnimalsToEggs.getDataMap());
            Bukkit.getScheduler().runTaskLater(AnimalsToEggs.getInstance(), ct.getCleanRunnable(), 20);

            sender.sendMessage("§a | 正在开始执行...");
            return;
        }

        Player player = (Player) sender;
        if (player.hasPermission("AnimalsToEggs.admin")) {
            CleanTimer ct = new CleanTimer(AnimalsToEggs.getDataMap());
            Bukkit.getScheduler().runTaskLater(AnimalsToEggs.getInstance(), ct.getCleanRunnable(), 20);

            sender.sendMessage("§a | 正在开始执行...");
            return;
        }

        sender.sendMessage("§c | 权限不足！");
    }
}
