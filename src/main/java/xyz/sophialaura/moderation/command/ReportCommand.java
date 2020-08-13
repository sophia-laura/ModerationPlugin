package xyz.sophialaura.moderation.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.sophialaura.moderation.menu.report.ReportMenu;
import xyz.sophialaura.moderation.utils.HashCooldown;

import java.util.UUID;

public class ReportCommand implements CommandExecutor {

    private final HashCooldown<UUID> cooldown;

    public ReportCommand() {
        this.cooldown = new HashCooldown<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length != 1) {
                player.sendMessage(String.format("§cUsage: /%s <player>", label));
                return false;
            }

            final String playerName = args[0];
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                player.sendMessage("§cPlayer not found.");
                return false;
            }

            if (cooldown.isWaiting(player.getUniqueId())) {
                player.sendMessage("§cYou need wait " + cooldown.getReamingSeconds(player.getUniqueId()) + "s");
                return false;
            }

            cooldown.insert(player.getUniqueId(), 30000);
            ReportMenu.openInventory(player, offlinePlayer);
        }
        return false;
    }
}
