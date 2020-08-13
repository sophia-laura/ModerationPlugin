package xyz.sophialaura.moderation.staff;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import xyz.sophialaura.moderation.menu.api.ItemBuilder;
import xyz.sophialaura.moderation.menu.freeze.FreezeMenu;

import java.util.*;

public class StaffMode {

    private final Set<UUID> staffMode;
    private final Set<UUID> vanish;
    private final Map<UUID, ItemStack[]> inventoryContents;
    private final Map<UUID, ItemStack[]> armorContents;

    private final Set<UUID> freeze;

    public StaffMode() {
        staffMode = new HashSet<>();
        vanish = new HashSet<>();
        inventoryContents = new HashMap<>();
        armorContents = new HashMap<>();
        freeze = new HashSet<>();
    }

    public void joinStaffMode(Player player) {
        final PlayerInventory inventory = player.getInventory();
        this.inventoryContents.put(player.getUniqueId(), inventory.getContents());
        this.armorContents.put(player.getUniqueId(), inventory.getArmorContents());

        inventory.clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setAllowFlight(true);

        staffMode.add(player.getUniqueId());
        vanish.add(player.getUniqueId());

        Bukkit.getOnlinePlayers().forEach(all -> all.hidePlayer(player));

        inventory.setItem(0, ItemBuilder.create(Material.STICK).enchant(Enchantment.KNOCKBACK, 5).name("§bKnockback").build());
        inventory.setItem(1, ItemBuilder.create(Material.COMPASS).name("§aGTP").build());
        inventory.setItem(2, ItemBuilder.create(Material.WATCH).name("§aVanish/Unvanish").build());
        inventory.setItem(3, ItemBuilder.create(Material.PAPER).name("§aCheck Player Status").build());
        inventory.setItem(4, ItemBuilder.create(Material.REDSTONE).name("§aChange gamemode").build());
        inventory.setItem(5, ItemBuilder.create(Material.DIAMOND_PICKAXE).name("§aCheck Mining Stats").flags(ItemFlag.HIDE_ATTRIBUTES).build());
        inventory.setItem(6, ItemBuilder.create(Material.PACKED_ICE).name("§bFreeze").build());

        player.updateInventory();
    }

    public void changeGamemode(Player player) {
        if (staffMode.contains(player.getUniqueId())) {
            switch (player.getGameMode()) {
                case SURVIVAL:
                    player.setGameMode(GameMode.SPECTATOR);
                    break;
                case SPECTATOR:
                    player.setGameMode(GameMode.SURVIVAL);
                    break;
            }
        }
    }

    public void leaveStaffMode(Player player) {
        if (staffMode.contains(player.getUniqueId())) {
            final PlayerInventory inventory = player.getInventory();

            inventory.setArmorContents(armorContents.get(player.getUniqueId()));
            inventory.setContents(inventoryContents.get(player.getUniqueId()));

            staffMode.remove(player.getUniqueId());
            vanish.remove(player.getUniqueId());
            inventoryContents.remove(player.getUniqueId());
            armorContents.remove(player.getUniqueId());

            Bukkit.getOnlinePlayers().forEach(all -> all.showPlayer(player));

            player.setAllowFlight(false);
            player.updateInventory();
        }
    }

    public void teleportToRandom(Player player) {
        boolean check = false;
        final List<Player> collect = new ArrayList<>(Bukkit.getOnlinePlayers());
        final Random random = new Random();
        while (!check) {
            final Player target = collect.get(random.nextInt(collect.size()));
            if (target.hasPermission("command.mod")) {
                continue;
            }
            player.teleport(target.getLocation());
            player.sendMessage("§aTeleported to " + target.getName());
            check = true;
        }
    }

    public void vanish(Player player) {
        if (!vanish.contains(player.getUniqueId())) {
            Bukkit.getOnlinePlayers().forEach(all -> all.hidePlayer(player));
            vanish.add(player.getUniqueId());
        }
    }

    public void unvanish(Player player) {
        if (vanish.contains(player.getUniqueId())) {
            Bukkit.getOnlinePlayers().forEach(all -> all.showPlayer(player));
            vanish.remove(player.getUniqueId());
        }
    }

    public void freezePlayer(Player player) {
        this.freeze.add(player.getUniqueId());
        FreezeMenu.openInventory(player);
    }

    public void unfreezePlayer(Player player) {
        this.freeze.remove(player.getUniqueId());
        player.closeInventory();
    }

    public boolean isStaffMode(Player player) {
        return staffMode.contains(player.getUniqueId());
    }

    public boolean isFrozen(Player player) {
        return freeze.contains(player.getUniqueId());
    }

    public boolean isVanished(Player player) {
        return vanish.contains(player.getUniqueId());
    }

    public Set<UUID> getVanish() {
        return vanish;
    }
}
