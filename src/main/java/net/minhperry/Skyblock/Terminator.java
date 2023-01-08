package net.minhperry.Skyblock;

import java.util.HashMap;
import java.util.Map;

import net.minhperry.SomePlugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Terminator implements Listener, CommandExecutor {
    // Map to store players that are holding the Terminator bow and shooting arrows
    Map<Player, BukkitRunnable> shootingPlayers = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            if (item == null || item.getType() != Material.BOW || !item.hasItemMeta()) {
                return;
            }

            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta == null || !itemMeta.getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Terminator")) {
                return;
            }

            double spread = 0.2;
            Vector direction = player.getLocation().getDirection().multiply(4);
            double y = direction.getY();

            for (int i = -1; i <= 1; i++) {
                double angle = (spread * i) - (spread / 2);
                double x = direction.getX() * Math.cos(angle) - direction.getZ() * Math.sin(angle);
                double z = direction.getX() * Math.sin(angle) + direction.getZ() * Math.cos(angle);
                Vector newDirection = new Vector(x, y, z);

                Arrow arrow = player.launchProjectile(Arrow.class);
                arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
                arrow.setVelocity(newDirection);
                arrow.setShooter(player);
                arrow.setDamage(10.0);
            }

        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (item == null || item.getType() != Material.BOW || !item.hasItemMeta()) {
                return;
            }

            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta == null || !itemMeta.getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Terminator")) {
                return;
            }

            double spread = 0.2;
            Vector direction = player.getLocation().getDirection().multiply(4);
            double y = direction.getY();

            for (int i = -1; i <= 1; i++) {
                double angle = (spread * i) - (spread / 2);
                double x = direction.getX() * Math.cos(angle) - direction.getZ() * Math.sin(angle);
                double z = direction.getX() * Math.sin(angle) + direction.getZ() * Math.cos(angle);
                Vector newDirection = new Vector(x, y, z);

                Arrow arrow = player.launchProjectile(Arrow.class);
                arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
                arrow.setVelocity(newDirection);
                arrow.setShooter(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Stop the task if the player leaves the server
        if (shootingPlayers.containsKey(player)) {
            shootingPlayers.get(player).cancel();
            shootingPlayers.remove(player);
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Give the player a Terminator bow
            ItemStack terminatorBow = new ItemStack(Material.BOW);
            ItemMeta bowMeta = terminatorBow.getItemMeta();
            bowMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Terminator");
            bowMeta.setUnbreakable(true);
            bowMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            terminatorBow.setItemMeta(bowMeta);
            player.getInventory().addItem(terminatorBow);
        }
        return true;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.ARROW) {
            return;
        }
        event.getEntity().remove();
    }
}