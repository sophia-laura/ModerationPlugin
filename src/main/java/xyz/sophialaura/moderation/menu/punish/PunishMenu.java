package xyz.sophialaura.moderation.menu.punish;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.sophialaura.moderation.ModerationPlugin;
import xyz.sophialaura.moderation.models.PunishReason;
import xyz.sophialaura.moderation.models.PunishType;
import xyz.sophialaura.moderation.menu.api.ItemBuilder;
import xyz.sophialaura.moderation.menu.api.Menu;
import xyz.sophialaura.moderation.utils.DateUtils;

import java.util.UUID;

public class PunishMenu {

    public static void openInventory(Player player, PunishType type, UUID target) {
        Menu menu = new Menu(type.getName() + " menu", 6);

        final ItemStack blue = ItemBuilder.create(Material.STAINED_GLASS_PANE).name("§a").durability(3).build();
        final ItemStack white = ItemBuilder.create(Material.STAINED_GLASS_PANE).name("§a").build();

        menu.setItem(0, blue);
        menu.setItem(1, white);
        menu.setItem(2, blue);
        menu.setItem(3, blue);
        menu.setItem(4, white);
        menu.setItem(5, blue);
        menu.setItem(6, blue);
        menu.setItem(7, white);
        menu.setItem(8, blue);

        menu.setItem(9, white);
        menu.setItem(17, white);

        menu.setItem(18, blue);
        menu.setItem(26, blue);

        menu.setItem(27, blue);
        menu.setItem(35, blue);

        menu.setItem(36, white);
        menu.setItem(44, white);

        menu.setItem(45, blue);
        menu.setItem(46, white);
        menu.setItem(47, blue);
        menu.setItem(48, blue);

        menu.setItem(49, ItemBuilder.create(Material.ARROW).name("§aReturn").build(), (clickType, stack, slot) ->
                PunishMainMenu.openInventory(player, target));

        menu.setItem(50, blue);
        menu.setItem(51, blue);
        menu.setItem(52, white);
        menu.setItem(53, blue);

        final ImmutableList<PunishReason> reasons = ModerationPlugin.getInstance().getPunishReasonService().findAllByType(type);
        int w = 10;
        for (PunishReason reason : reasons) {
            if (menu.hasItem(w)) {
                w++;
            }
            menu.setItem(w, ItemBuilder.create(Material.PAPER).name("§a" + reason.getReason()).lore(
                    "§f" + type.getName() + " time: §7" + DateUtils.formatDifference(reason.getPunishExpires()),
                    "",
                    "§7Click here to punish §e" + Bukkit.getOfflinePlayer(target).getName(),
                    "",
                    "§f§lLEFT click §7to appears for everyone in the chat",
                    "§f§lRIGHT click §7to only appears for the staff"
            ).build());
            w++;
        }

        menu.open(player);
    }

}
