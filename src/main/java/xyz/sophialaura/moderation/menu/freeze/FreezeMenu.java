package xyz.sophialaura.moderation.menu.freeze;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.sophialaura.moderation.ModerationPlugin;
import xyz.sophialaura.moderation.menu.api.ItemBuilder;
import xyz.sophialaura.moderation.menu.api.Menu;

public class FreezeMenu {

    private static final ModerationPlugin plugin;

    static {
        plugin = ModerationPlugin.getInstance();
    }

    public static void openInventory(Player player) {
        Menu menu = new Menu("Freeze", 5, () -> {
            if (plugin.getStaffMode().isFrozen(player)) {
                FreezeMenu.openInventory(player);
            }
        });

        menu.setItem(22, ItemBuilder.create(Material.SIGN).name("§bFreeze").lore("§7Join in discord").build());

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
        menu.setItem(27, white);
        menu.setItem(35, white);
        menu.setItem(36, blue);
        menu.setItem(37, white);
        menu.setItem(38, blue);
        menu.setItem(39, blue);
        menu.setItem(40, white);
        menu.setItem(41, blue);
        menu.setItem(42, blue);
        menu.setItem(43, white);
        menu.setItem(44, blue);

        menu.open(player);
    }

}
