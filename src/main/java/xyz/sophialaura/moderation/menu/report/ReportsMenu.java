package xyz.sophialaura.moderation.menu.report;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.sophialaura.moderation.ModerationPlugin;
import xyz.sophialaura.moderation.menu.api.ClickType;
import xyz.sophialaura.moderation.menu.api.Menu;
import xyz.sophialaura.moderation.menu.api.SkullBuilder;
import xyz.sophialaura.moderation.models.Report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ReportsMenu {

    private static final ModerationPlugin plugin;

    static {
        plugin = ModerationPlugin.getInstance();
    }

    public static void openInventory(Player player) {
        Menu menu = new Menu("Reports", 6);

        final ImmutableList<Report> all = plugin.getReportService().findAll();
        Map<UUID, List<Report>> reportMap = new LinkedHashMap<>();

        all.forEach(report -> {
            final List<Report> reportList = reportMap.getOrDefault(report.getTarget(), new ArrayList<>());
            reportList.add(report);
            reportMap.put(report.getTarget(), reportList);
        });

        int w = 0;

        Map<String, List<Report>> reasons;
        for (Map.Entry<UUID, List<Report>> entry : reportMap.entrySet().stream().sorted(Comparator.comparingInt(value ->
                value.getValue().size())).collect(Collectors.toList())) {

            final UUID key = entry.getKey();
            final List<Report> value = entry.getValue();
            reasons = new HashMap<>();

            for (Report report : value) {
                final List<Report> reasonList = reasons.getOrDefault(report.getReason(), new ArrayList<>());
                reasonList.add(report);
                reasons.put(report.getReason(), reasonList);
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(key);

            Map<String, List<Report>> finalReasons = reasons;
            ItemStack head = SkullBuilder.create().owner(target).name("§a§l" + target.getName()).lore(lore -> {
                lore.add("");
                lore.add("§f> #Reports : §b§l" + value.size());
                lore.add("");
                lore.add("§f> Report List :");
                finalReasons.forEach((s, reports) -> lore.add("§f" + s + " : §b§l" + reports.size()));
                lore.add("");
                lore.add(target.isOnline() ? "§a§lONLINE" : "§c§lOFFLINE");
            }).build();
            menu.setItem(w, head, (clickType, stack, slot) -> {
                if (clickType == ClickType.LEFT) {
                    if (target.isOnline()) {
                        player.teleport(target.getPlayer().getLocation());
                        menu.open(player);
                    } else {
                        player.sendMessage("§cThis player is offline.");
                    }
                } else if (clickType == ClickType.RIGHT && player.hasPermission("report.delete")) {
                    plugin.getReportService().removeIf(report -> report.getTarget().equals(key));
                    plugin.getReportDao().findAllByTarget(key);
                    ReportsMenu.openInventory(player);
                } else {
                    player.sendMessage("§a§lReports:");
                    player.sendMessage("");
                    DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                    value.forEach(report -> {
                        player.sendMessage("§fAuthor: §b" + Bukkit.getOfflinePlayer(report.getAuthor()).getName());
                        player.sendMessage("§fTarget: §b" + Bukkit.getOfflinePlayer(report.getTarget()).getName());
                        player.sendMessage("§fReason: §b" + report.getReason());
                        player.sendMessage("§fDate: §b" + format.format(report.getDate()));
                        player.sendMessage("");
                    });
                }
            });
            w++;
        }
        menu.open(player);
    }

}
