package net.minhperry.Skyblock;

import net.minhperry.Items.IBoots;
import net.minhperry.SomePlugin;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpringBoots {
    private Material material;
    private Consumer<Player> ability;
    private double speed;

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setAbility() {
        this.ability = player -> {
            player.setFlySpeed(0.1f);
            new BukkitRunnable() {
                long startTime = System.currentTimeMillis();

                @Override
                public void run() {
                    if (player.isOnGround() || !player.isSneaking()) {
                        long elapsedTime = System.currentTimeMillis() - startTime;
                        double boost = Math.min(elapsedTime / 1000.0, 1.0);
                        player.setVelocity(player.getLocation().getDirection().multiply(boost));
                        player.setFlySpeed(0.05f);
                        this.cancel();
                    }
                }
            }.runTaskTimer(SomePlugin.getInstance(), 0, 1);
        };
    }


    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public ItemStack createBoots() {
        ItemStack boots = new ItemStack(material);
        ItemMeta meta = boots.getItemMeta();
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "custom speed boost", speed, AttributeModifier.Operation.ADD_NUMBER));
        meta.setDisplayName("Spring Boots");

        List<String> lore = new ArrayList<>();
        lore.add("Grants the ability to charge jumps by holding shift.");
        lore.add("Releasing shift will launch you into the air.");
        meta.setLore(lore);

        boots.setItemMeta(meta);
        return boots;
    }
}
