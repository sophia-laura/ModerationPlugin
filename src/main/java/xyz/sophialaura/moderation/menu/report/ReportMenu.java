package xyz.sophialaura.moderation.menu.report;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.sophialaura.moderation.ModerationPlugin;
import xyz.sophialaura.moderation.ModerationSettings;
import xyz.sophialaura.moderation.menu.api.Menu;
import xyz.sophialaura.moderation.models.Report;
import xyz.sophialaura.moderation.utils.ConfigItemBuilder;

import java.util.List;

public class ReportMenu {

    private static ModerationPlugin plugin;

    static {
        plugin = ModerationPlugin.getInstance();
    }

    public static void openInventory(Player player, OfflinePlayer offlinePlayer) {
        final List<ConfigItemBuilder> reasons = ModerationSettings.getReportReasonList();
        int count = (reasons.size() / 9) + 1;

        Menu menu = new Menu("Choose the reason", count);

        int w = 0;
        for (ConfigItemBuilder reason : reasons) {
            menu.setItem(w, reason.getBuilder().build(), (clickType, stack, slot) -> {
                final String name = reason.getName();

                final List<String> message = ModerationSettings.getReportMessage();
                message.forEach(s -> {
                    Bukkit.getOnlinePlayers().forEach(staff -> staff.sendMessage(ChatColor.translateAlternateColorCodes('&', s)
                            .replace("$target", offlinePlayer.getName()).replace("$author", player.getName())
                            .replace("$reason", name)));
                });

                final Report report = Report.builder()
                        .author(player.getUniqueId())
                        .target(offlinePlayer.getUniqueId())
                        .reason(name)
                        .date(System.currentTimeMillis())
                        .build();

                plugin.getReportDao().createOrUpdate(report);
                plugin.getReportService().add(report);

                player.sendMessage("§aSend");
                menu.close(player);
            });
            w++;
        }

        menu.open(player);
    }

}
