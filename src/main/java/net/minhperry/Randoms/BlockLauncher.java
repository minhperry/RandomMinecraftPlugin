package net.minhperry.Randoms;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BlockLauncher implements Listener {
    @EventHandler
    public static void onRightClickHorseArmor(PlayerInteractEvent event) {
        Player pl = event.getPlayer();

        ItemStack mainhand = pl.getInventory().getItemInMainHand();
        ItemStack offhand = pl.getInventory().getItemInOffHand();

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (mainhand.getType().equals(Material.LEATHER_HORSE_ARMOR)) {
                if (offhand.getType().isBlock()) {
                    BlockData bd = offhand.getType().createBlockData();
                    // pl.sendMessage(bd.toString());

                    FallingBlock fb = pl.getWorld().spawnFallingBlock(pl.getLocation(), bd);
                    // fb.setGravity(false);
                    fb.setDropItem(false);

                    Vector facing = pl.getLocation().getDirection().setY(pl.getLocation().getDirection().getY() * 1.1);
                    fb.setVelocity(facing.multiply(2));
                } else {
                    pl.sendMessage("§cCannot launch this! " + offhand.getType().toString() + " is not a valid block!");
                }
            }
        }

    }
}
