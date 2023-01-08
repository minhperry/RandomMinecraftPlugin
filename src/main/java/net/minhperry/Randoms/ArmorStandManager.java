package net.minhperry.Randoms;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minhperry.SomePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ArmorStandManager implements Listener {
    private static final double OFFSET_X = 0.0;
    private static final double OFFSET_Y = 0.0;
    private static final double OFFSET_Z = 0.0;

    private final Map<UUID, ArmorStand> armorStandMap = new HashMap<>();

    public void enable() {
        Bukkit.getPluginManager().registerEvents(this, SomePlugin.getInstance());
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateArmorStand(player);
                }
            }
        }.runTaskTimer(SomePlugin.getInstance(), 0, 1);
    }

    public void disable() {
        for (ArmorStand armorStand : armorStandMap.values()) {
            armorStand.remove();
        }
        armorStandMap.clear();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        Location location = player.getLocation();
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setCustomNameVisible(true);
        armorStandMap.put(player.getUniqueId(), armorStand);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ArmorStand armorStand = armorStandMap.remove(player.getUniqueId());
        if (armorStand != null) {
            armorStand.remove();
        }
    }

    private void updateArmorStand(Player player) {
        ArmorStand armorStand = armorStandMap.get(player.getUniqueId());
        if (armorStand != null) {
            Location location = player.getLocation().add(OFFSET_X, OFFSET_Y, OFFSET_Z);
            armorStand.teleport(location);
            armorStand.setCustomName(String.format("X: %.2f Y: %.2f Z: %.2f", location.getX(), location.getY(), location.getZ()));
        }
    }
}