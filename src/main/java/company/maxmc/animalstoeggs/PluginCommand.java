package company.maxmc.animalstoeggs;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author SanseYooyea
 */
public class PluginCommand implements CommandExecutor {

    public static final String RELOAD_COMMAND = "reload";
    public static final String HELP_COMMAND = "help";
    public static final String CLEAN_COMMAND = "clean";

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (args.length == 0 || (args.length==1 && HELP_COMMAND.equalsIgnoreCase(args[0]))) {
            sender.sendMessage("§e§l§m========§9§l[转变动物插件帮助系统]§e§l§m========");
            sender.sendMessage("§9 - §f/ate help §9- §7查看AnimalsToEggs插件的所有指令");
            sender.sendMessage("§9 - §f/ate clean §9- §7强制使用清理程序");
            sender.sendMessage("§9 - §f/ate reload §9- §7重载AnimalsToEggs插件的配置");
            sender.sendMessage("§e§l§m========§9§l[转变动物插件帮助系统]§e§l§m========");
        }
        if (args.length == 1) {
            if (RELOAD_COMMAND.equalsIgnoreCase(args[0])){

            }
            if (CLEAN_COMMAND.equalsIgnoreCase(args[0])){
                CleanTimer ct = new CleanTimer(AnimalsToEggs.dataMap);
                ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
                ses.schedule(ct.cleanTask, 1, TimeUnit.SECONDS);
            }
        }
        return false;
    }
}
