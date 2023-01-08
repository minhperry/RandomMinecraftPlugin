package net.minhperry.Display;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HealthDisplayer implements Listener {

    public String processMobName(LivingEntity entity) {
        String[] nameArr = entity.toString().split("(?=\\p{Lu})");
        String[] nameArrNew = Arrays.copyOfRange(nameArr, 1, nameArr.length);
        String name = String.join(" ", nameArrNew);
        return name;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entityy = event.getEntity();
        if (!(entityy instanceof LivingEntity))
            return;
        if (entityy instanceof ArmorStand)
            return;
        final DecimalFormat df = new DecimalFormat("0.00");
        LivingEntity entity = (LivingEntity) entityy;
        String name = processMobName(entity);
        double health = entity.getHealth();
        double maxHealth = entity.getMaxHealth();
        entity.setCustomName(ChatColor.RED + name + " "
                + ChatColor.GREEN + df.format(health)
                + ChatColor.WHITE + "/"
                + ChatColor.GREEN + df.format(maxHealth)
                + ChatColor.RED + "❤");
        entity.setCustomNameVisible(true);
    }

    @EventHandler
    public void onEntitySpawn(EntityDamageEvent event) {
        Entity entityy = event.getEntity();
        if (!(entityy instanceof LivingEntity))
            return;
        if (entityy instanceof ArmorStand)
            return;
        final DecimalFormat df = new DecimalFormat("0.00");
        LivingEntity entity = (LivingEntity) entityy;
        String name = processMobName(entity);
        double health = entity.getHealth();
        double maxHealth = entity.getMaxHealth();
        entity.setCustomName(ChatColor.RED + name + " "
                + ChatColor.GREEN + df.format(health)
                + ChatColor.WHITE + "/"
                + ChatColor.GREEN + df.format(maxHealth)
                + ChatColor.RED + "❤");
        entity.setCustomNameVisible(true);
    }

    @EventHandler
    public void onEntitySpawn(EntityRegainHealthEvent event) {
        Entity entityy = event.getEntity();
        if (!(entityy instanceof LivingEntity))
            return;
        if (entityy instanceof ArmorStand)
            return;
        final DecimalFormat df = new DecimalFormat("0.00");
        LivingEntity entity = (LivingEntity) entityy;
        String name = processMobName(entity);
        double health = entity.getHealth();
        double maxHealth = entity.getMaxHealth();
        entity.setCustomName(ChatColor.RED + name + " "
                + ChatColor.GREEN + df.format(health)
                + ChatColor.WHITE + "/"
                + ChatColor.GREEN + df.format(maxHealth)
                + ChatColor.RED + "❤");
        entity.setCustomNameVisible(true);
    }

    @EventHandler
    public void onEndermanTP(EntityTeleportEvent ev) {
        if (!(ev.getEntity() instanceof Enderman))
            return;
        else ev.setCancelled(true);
    }
}

