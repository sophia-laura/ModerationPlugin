package xyz.sophialaura.moderation.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sophialaura.moderation.ModerationPlugin;
import xyz.sophialaura.moderation.menu.report.ReportsMenu;

public class ReportsCommand implements CommandExecutor {

    private final ModerationPlugin plugin;

    public ReportsCommand(ModerationPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && sender.hasPermission("command.reports")) {
            final Player player = (Player) sender;
            if(args.length == 0) {
                ReportsMenu.openInventory(player);
            } else if(args.length == 1) {
                String subCommand = args[0];
                if(subCommand.equals("purge")) {
                    plugin.getReportDao().clearAll();
                    plugin.getReportService().purgeAll();
                    player.sendMessage("§aAll reports have been deleted.");
                }
            }
        }
        return false;
    }
}
