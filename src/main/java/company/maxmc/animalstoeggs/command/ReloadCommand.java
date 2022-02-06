package company.maxmc.animalstoeggs.command;

import company.maxmc.animalstoeggs.AnimalsToEggs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SanseYooyea
 */
public class ReloadCommand {

    public static void reloadConfig(CommandSender sender) {
        if (!(sender instanceof Player)) {
            AnimalsToEggs.getInstance().reloadConfig();
            AnimalsToEggs.getInstance().loadDataMap();

            sender.sendMessage("§a | 配置文件重载成功");
            return;
        }

        Player player = (Player) sender;
        if (player.hasPermission("AnimalsToEggs.admin")) {
            AnimalsToEggs.getInstance().reloadConfig();
            AnimalsToEggs.getInstance().loadDataMap();

            sender.sendMessage("§a | 配置文件重载成功");
            return;
        }

        sender.sendMessage("§c | 权限不足！");

    }
}
