package net.minhperry.Skyblock;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Hyperion implements Listener {

    private final JavaPlugin plugin;
    private final NamespacedKey key;
    private final ItemStack item;

    private final String name = ChatColor.GOLD + "Hyperion";
    String longstring = "                                                                             _";

    public Hyperion(JavaPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "hyperion");
        this.item = createItem();
        registerRecipe();
    }

    private ItemStack createItem() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 7, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 0);
        item.setItemMeta(meta);
        return item;
    }

    private void registerRecipe() {
        Recipe recipe = new ShapedRecipe(key, item)
                .shape(" A ", " A ", " T ")
                .setIngredient('A', Material.ENCHANTED_GOLDEN_APPLE)
                .setIngredient('T', Material.TOTEM_OF_UNDYING);
        plugin.getServer().addRecipe(recipe);
    }

    @EventHandler
    public void useHyperion(PlayerInteractEvent event) throws InterruptedException {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName() || !meta.getDisplayName().equals(name) || !meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
            return;
        }

        Player player = event.getPlayer();
        Location location = event.getPlayer().getLocation();

        location.add(location.getDirection().multiply(5));
        location.setX(location.getBlockX() + 0.5);
        location.setY(location.getBlockY() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);

        for (Entity entity : player.getNearbyEntities(7, 7, 7)) {
            if (entity instanceof Damageable) {
                ((Damageable) entity).damage(20);
            }
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 4));

        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setHealth(Math.min(player.getHealth() + 5, player.getMaxHealth()));
            }
        }.runTaskLater(plugin, 20 * 5);

        RayTraceResult result = player.getWorld().rayTraceBlocks(player.getEyeLocation(), location.getDirection(), 5, FluidCollisionMode.NEVER);
        if (!(result == null || result.getHitBlock() == null)) {
            displayQuickSubtitle(player, ChatColor.RED + "There are blocks in the way!");
            return;
        }
        if (!isLocationSafeForTeleport(location.getBlock())) {
            displayQuickSubtitle(player, ChatColor.RED + "Destination is obstructed!");
            return;
        }

        player.setVelocity(new Vector(0, 0, 0));
        player.setFallDistance(0);
        player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    private void displayQuickSubtitle(Player player, String subtitle) {
        player.sendTitle(longstring, subtitle, 1, 10, 1);
    }

    private boolean isLocationSafeForTeleport(Block block) {
        if (!block.getType().isSolid() && !block.getRelative(BlockFace.UP).getType().isSolid())
            return true;
        else return false;
    }
}
