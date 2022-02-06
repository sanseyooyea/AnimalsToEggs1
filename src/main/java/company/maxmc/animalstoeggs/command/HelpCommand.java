package company.maxmc.animalstoeggs.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SanseYooyea
 */
public class HelpCommand {

    public static void sendHelpMessage(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§a | §eAnimalsToEggs §2帮助系统");
            sender.sendMessage("§a | §7/ate help §f- §2获取帮助系统");
            sender.sendMessage("§a | §7/ate clean §f- §2强制使用清理程序");
            sender.sendMessage("§a | §7/ate reload §f- §2重载插件配置");
            return;
        }

        Player player = (Player) sender;
        if (player.hasPermission("AnimalsToEggs.admin")) {
            sender.sendMessage("§a | §eAnimalToEggs §2帮助系统");
            sender.sendMessage("§a | §7/ate help §f- §2获取帮助系统");
            sender.sendMessage("§a | §7/ate clean §f- §2强制使用清理程序");
            sender.sendMessage("§a | §7/ate reload §f- §2重载插件配置");
            return;
        }

        sender.sendMessage("§c | 权限不足！");

    }
}
