package net.minhperry.Skyblock;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class GlacialScythe implements Listener {
    private static final Material GLACIAL_SCYTHE_MATERIAL = Material.DIAMOND_HOE;
    private static final int PROJECTILE_DAMAGE = 5;
    private static final int PROJECTILE_RADIUS = 5;
    private static final int PROJECTILE_LIFESPAN = 5;
    private static final String name = ChatColor.GOLD + "Glacial Scythe";
    private final JavaPlugin plugin;
    private final NamespacedKey key;
    private final ItemStack item;

    public GlacialScythe(JavaPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "glacial_scythe");
        this.item = createItem();
        registerRecipe();
    }

    private ItemStack createItem() {
        ItemStack item = new ItemStack(GLACIAL_SCYTHE_MATERIAL);
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
        Recipe recipe = new ShapedRecipe(item)
                .shape("DAD", "DBD", "IHI")
                .setIngredient('I', Material.BLUE_ICE)
                .setIngredient('A', Material.ENCHANTED_GOLDEN_APPLE)
                .setIngredient('B', Material.BEACON)
                .setIngredient('H', Material.DIAMOND_HORSE_ARMOR)
                .setIngredient('D', Material.DIAMOND_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != GLACIAL_SCYTHE_MATERIAL) {
            return;
        }

        ItemStack itemHeld = event.getItem();
        if (itemHeld == null || !itemHeld.hasItemMeta()) {
            return;
        }
        ItemMeta meta = itemHeld.getItemMeta();
        if (!meta.hasDisplayName() || !meta.getDisplayName().equals(name) || !meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        // armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.getEquipment().setHelmet(new ItemStack(Material.BLUE_ICE));
        armorStand.getEquipment().setItemInMainHand(new ItemStack(Material.SEA_LANTERN));
        armorStand.getEquipment().setItemInOffHand(new ItemStack(Material.TNT));

        Vector direction = player.getEyeLocation().getDirection().multiply(3);
        armorStand.setVelocity(direction);

        armorStand.setCustomName("GlacialProjectile");
        armorStand.setCustomNameVisible(false);

        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0F, 1.4F);

        player.getServer().getScheduler().scheduleSyncDelayedTask(
                plugin,
                () -> {
                    if (armorStand.isValid()) {
                        armorStand.getWorld().getEntities().stream()
                                .filter(e -> e instanceof LivingEntity && !(e instanceof Player))
                                .filter(e -> e.getLocation().distance(armorStand.getLocation()) <= PROJECTILE_RADIUS)
                                .forEach(e -> ((LivingEntity) e).damage(PROJECTILE_DAMAGE));
                        player.playSound(armorStand.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.5F, 1.5F);
                        armorStand.remove();
                    }
                },
                PROJECTILE_LIFESPAN * 20
        );
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (!(projectile instanceof ArmorStand)) {
            return;
        }

        ArmorStand armorStand = (ArmorStand) projectile;

        if (!armorStand.hasMetadata("GlacialProjectile")) {
            return;
        }

        applyDamage(armorStand.getLocation());
        event.getEntity().getWorld().playSound(armorStand.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.5F, 1.5F);
        armorStand.remove();
    }

    private void applyDamage(Location location) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, PROJECTILE_RADIUS, PROJECTILE_RADIUS, PROJECTILE_RADIUS)) {
            if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.damage(PROJECTILE_DAMAGE);
            }
        }
    }

    @EventHandler
    public void onTiltingDirt(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != GLACIAL_SCYTHE_MATERIAL) {
            return;
        }

        ItemStack itemHeld = event.getItem();
        if (itemHeld == null || !itemHeld.hasItemMeta()) {
            return;
        }
        ItemMeta meta = itemHeld.getItemMeta();
        if (!meta.hasDisplayName() || !meta.getDisplayName().equals(name) || !meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block usedOn = event.getClickedBlock();

        if (isDirt(usedOn)) {
            event.setCancelled(true);
        }
    }

    private boolean isDirt(Block block) {
        if (block == null) return false;
        Material[] allDirt = {
                Material.DIRT,
                Material.GRASS_BLOCK,
                Material.COARSE_DIRT,
                Material.PODZOL,
                Material.ROOTED_DIRT,
                Material.DIRT_PATH,
                Material.MYCELIUM
        };
        if (Arrays.asList(allDirt).contains(block.getType())) {
            return true;
        } else return false;
    }
}