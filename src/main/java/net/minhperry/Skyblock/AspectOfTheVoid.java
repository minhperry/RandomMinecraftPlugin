package net.minhperry.Skyblock;

import net.minhperry.SomePlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class AspectOfTheVoid implements Listener {

    private final JavaPlugin plugin;
    private final NamespacedKey key;
    private final ItemStack item;

    private final String name = ChatColor.DARK_PURPLE + "Aspect of The Void";
    String longstring = "                                                                             _";

    public AspectOfTheVoid(JavaPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "aspect_of_the_void");
        this.item = createItem();
        registerRecipe();
    }

    private ItemStack createItem() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addEnchant(Enchantment.DIG_SPEED, 10, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 0);
        item.setItemMeta(meta);
        return item;
    }

    private void registerRecipe() {
        Recipe recipe = new ShapedRecipe(key, item)
                .shape(" E ", " S ", " H ")
                .setIngredient('E', Material.DRAGON_EGG)
                .setIngredient('S', Material.NETHER_STAR)
                .setIngredient('H', Material.NETHERITE_SHOVEL);
        plugin.getServer().addRecipe(recipe);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
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

        float preTPPitch = player.getLocation().getPitch();
        float preTPYaw = player.getLocation().getYaw();

        Location location = event.getPlayer().getLocation();

        if (!event.getPlayer().isSneaking()) {

            // AoTE
            location.add(location.getDirection().multiply(10));
            location.setX(location.getBlockX() + 0.5);
            location.setZ(location.getBlockZ() + 0.5);

            RayTraceResult result = player.getWorld().rayTraceBlocks(player.getEyeLocation(), location.getDirection(), 10, FluidCollisionMode.NEVER);
            if (!(result == null || result.getHitBlock() == null)) {
                displayQuickSubtitle(player, ChatColor.RED + "There are blocks in the way!");
                return;
            }

            if (!isLocationSafeForTeleport(location.getBlock())) {
                displayQuickSubtitle(player, ChatColor.RED + "Destination is obstructed!");
                return;
            }
            player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }

        // Etherwarp
        if (event.getPlayer().isSneaking()) {
            RayTraceResult result = player.getWorld().rayTraceBlocks(player.getEyeLocation(), location.getDirection(), 512, FluidCollisionMode.NEVER);
            if (result == null || result.getHitBlock() == null) {
                return;
            }
            Block block = result.getHitBlock();
            BlockData data = block.getBlockData();
            if (data instanceof Slab) {
                Slab slab = (Slab) data;
                if (slab.getType() == Slab.Type.TOP) {
                    block = block.getRelative(BlockFace.UP);
                }
            }
            location = block.getLocation();
            location.setX(location.getBlockX() + 0.5);
            location.setY(location.getBlockY() + 1.0);
            location.setZ(location.getBlockZ() + 0.5);
            location.setPitch(preTPPitch);
            location.setYaw(preTPYaw);
            int distance = (int) location.distance(event.getPlayer().getLocation());

            if (!isLocationSafeForTeleport(location.getBlock())) {
                displayQuickSubtitle(player, ChatColor.RED + "Destination is obstructed!");
                return;
            }
            if (distance > 50) {
                displayQuickSubtitle(player,
                        ChatColor.RED + "Too far! " + ChatColor.GREEN + distance + " blocks!");
                return;
            }
            player.playSound(location, Sound.ENTITY_ENDER_DRAGON_HURT, 1.0F, 1.2F);
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

    // @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            Location location = event.getPlayer().getLocation();
            RayTraceResult result = player.getWorld().rayTraceBlocks(player.getEyeLocation(), location.getDirection(), 512, FluidCollisionMode.NEVER);
            if (result == null || result.getHitBlock() == null) {
                return;
            }
            Block block = result.getHitBlock();

            Material oldMat = block.getType();

            Material newMat = Material.SEA_LANTERN;

            block.setType(newMat);

            new BukkitRunnable() {
                @Override
                public void run() {
                    block.setType(oldMat);
                }
            }.runTaskLater(SomePlugin.getInstance(), 10);
        }

    }


}