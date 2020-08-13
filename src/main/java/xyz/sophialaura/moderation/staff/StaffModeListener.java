package xyz.sophialaura.moderation.staff;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class StaffModeListener implements Listener {

    private final StaffMode staffMode;

    public StaffModeListener(StaffMode staffMode) {
        this.staffMode = staffMode;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        staffMode.getVanish().forEach(uuid -> {
            Player vanish = Bukkit.getPlayer(uuid);
            if (vanish != null) {
                player.hidePlayer(vanish);
            }
        });
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (staffMode.isStaffMode(player)) {
            staffMode.leaveStaffMode(player);
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (staffMode.isStaffMode(player)) {
                event.setCancelled(true);
            }
            if (staffMode.isFrozen(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            final Player player = (Player) event.getDamager();
            if (staffMode.isStaffMode(player) && player.getItemInHand().getType() != Material.STICK) {
                event.setCancelled(true);
            }
            if (staffMode.isFrozen(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        if (staffMode.isStaffMode(player)) {
            event.setCancelled(true);
        }
        if (staffMode.isFrozen(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (staffMode.isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        if (staffMode.isStaffMode(player)) {
            event.setCancelled(true);
        }
        if (staffMode.isFrozen(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (staffMode.isStaffMode(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (staffMode.isStaffMode(player)) {
            event.setCancelled(true);
            event.setBuild(false);
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(FoodLevelChangeEvent event) {
        final Player player = (Player) event.getEntity();
        if (staffMode.isStaffMode(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        if (staffMode.isStaffMode(player) && event.getRightClicked() instanceof Player) {
            final Player target = ((Player) event.getRightClicked()).getPlayer();
            final ItemStack itemInHand = player.getItemInHand();
            if (itemInHand.getType() == Material.PACKED_ICE) {
                if (staffMode.isFrozen(target)) {
                    staffMode.unfreezePlayer(target);
                } else {
                    staffMode.freezePlayer(target);
                }
            } else if (itemInHand.getType() == Material.WATCH) {
                if (staffMode.isVanished(player)) {
                    staffMode.unvanish(player);
                } else {
                    staffMode.vanish(player);
                }
            } else if (itemInHand.getType() == Material.REDSTONE) {
                staffMode.changeGamemode(player);
            } else if (itemInHand.getType() == Material.PAPER) {
                player.performCommand("check " + target.getName() + " status");
            } else if (itemInHand.getType() == Material.DIAMOND_PICKAXE) {
                player.performCommand("check " + target.getName() + " mining");
            } else if (itemInHand.getType() == Material.COMPASS) {
                staffMode.teleportToRandom(player);
            }
        }
    }
}
