package net.minhperry.Randoms;

import net.minhperry.SomePlugin;
import net.minhperry.Utils.Wools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Locale;
import java.util.UUID;

public class PathWriter implements Listener {

    static UUID CryoJam = UUID.fromString("e353c178-c5ee-457c-a99f-06fea22e4dfb");
    static int woolNum = 0;

    @EventHandler
    public static void onThrowSnowball(ProjectileLaunchEvent ev) {
        final boolean[] sentMessageOnce = {false};
        Entity ent = ev.getEntity();


        if (ent instanceof Snowball) {
            if (((Snowball) ent).getShooter() instanceof Player) {

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (ent.isDead()) cancel();

                        Location loc = ent.getLocation();
                        Block bl = ent.getWorld().getHighestBlockAt(loc);

                        Player p = (Player) ((Snowball) ent).getShooter();

                        Material mat = p.getInventory().getItemInOffHand().getType();

                        if (mat.isBlock()) {
                            bl.setType(mat);
                        } else {
                            // TODO: Colorful wool if holding dyes
                            if (mat.toString().toLowerCase(Locale.ROOT).endsWith("dye")) {
                                bl.setType(Wools.getWoolTypes()[(woolNum++) % Wools.getAmountColors()]);
                            } else {
                                if (!sentMessageOnce[0]) {
                                    p.sendMessage("§cYou are holding invalid block material!");
                                    sentMessageOnce[0] = true;
                                }
                                bl.setType(Material.AIR);
                            }
                        }
                    }
                }.runTaskTimer(SomePlugin.getInstance(), 0L, 0L);
            }
        }
    }

    @EventHandler
    public static void onThrow2(ProjectileLaunchEvent ev) {
        Entity ent = ev.getEntity();
        if (ent instanceof Snowball) {
            if (((Snowball) ent).getShooter() instanceof Player) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (ent.isDead()) cancel();

                        Location loc = ent.getLocation();
                        Block bl = ent.getWorld().getHighestBlockAt(loc);

                        Player pl = (Player) ((Snowball) ent).getShooter();

                        BlockData bd = bl.getBlockData();

                        FallingBlock fb = pl.getWorld().spawnFallingBlock(bl.getLocation(), bd);
                        // fb.setGravity(false);
                        fb.setDropItem(false);

                        Vector facing = new Vector(0, 2, 0);
                        fb.setVelocity(facing);
                        // pl.sendMessage(fb.toString());
                    }
                }.runTaskTimer(SomePlugin.getInstance(), 2, 0);
            }
        }
    }
}
