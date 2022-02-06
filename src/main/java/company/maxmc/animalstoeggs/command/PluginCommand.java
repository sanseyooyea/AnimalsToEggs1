package company.maxmc.animalstoeggs.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author SanseYooyea
 */
public class PluginCommand implements CommandExecutor {

    public static final String RELOAD_COMMAND = "reload";
    public static final String HELP_COMMAND = "help";
    public static final String CLEAN_COMMAND = "clean";

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (args.length == 0) {
            HelpCommand.sendHelpMessage(sender);
            return true;
        }

        if (args.length == 1) {
            if (HELP_COMMAND.equalsIgnoreCase(args[0])) {
                HelpCommand.sendHelpMessage(sender);
                return true;
            }

            if (RELOAD_COMMAND.equalsIgnoreCase(args[0])){
                ReloadCommand.reloadConfig(sender);
                return true;
            }

            if (CLEAN_COMMAND.equalsIgnoreCase(args[0])){
                CleanCommand.clean(sender);
                return true;
            }
        }

        sender.sendMessage("§c | 指令错误！");
        HelpCommand.sendHelpMessage(sender);
        return true;
    }
}
