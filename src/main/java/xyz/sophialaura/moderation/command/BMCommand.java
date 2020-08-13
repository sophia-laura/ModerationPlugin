package xyz.sophialaura.moderation.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sophialaura.moderation.ModerationPlugin;
import xyz.sophialaura.moderation.menu.punish.PunishMainMenu;

import java.util.UUID;

public class BMCommand implements CommandExecutor {

    private final ModerationPlugin plugin;

    public BMCommand(ModerationPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (!player.hasPermission("bm.open")) {
                return false;
            }

            if (args.length != 1) {
                player.sendMessage(String.format("§cUsage: /%s <player>.", label));
                return false;
            }

            UUID uniqueId = null;
            final String playerName = args[0];
            final Player target = Bukkit.getPlayer(playerName);

            if (target != null) {
                uniqueId = target.getUniqueId();
            } else {
                final OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(playerName);
                if (offlineTarget == null) {
                    player.sendMessage("§cPlayer not found.");
                    return false;
                }
                uniqueId = offlineTarget.getUniqueId();
            }
            PunishMainMenu.openInventory(player, uniqueId);
        }
        return false;
    }
}
