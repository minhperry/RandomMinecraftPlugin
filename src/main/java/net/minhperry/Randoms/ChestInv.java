package net.minhperry.Randoms;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minhperry.Skyblock.SpringBoots;

public class ChestInv implements CommandExecutor, Listener {

    private final Map<UUID, Inventory> openChests = new HashMap<>();

    ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (!command.getName().equalsIgnoreCase("ch")) {
            return false;
        }

        if (args.length < 2) {
            player.sendMessage("You must specify a chest name and the number of slots!");
            return true;
        }

        String chestName = args[0];
        int numSlots;
        try {
            numSlots = Integer.parseInt(args[1]);
            if (numSlots <= 0) throw new NumberFormatException();
            if (numSlots % 9 != 0 || (9 < numSlots && numSlots > 54)) throw new IllegalArgumentException();
        } catch (NumberFormatException e) {
            player.sendMessage("The number of slots must be a positive integer!");
            return true;
        } catch (IllegalArgumentException e) {
            player.sendMessage("The number of slots must be divisble by 9! Setting to 27 default. ");
            numSlots = 27;
        }

        Inventory chest = Bukkit.createInventory(player, numSlots, chestName);

        int borderSize = numSlots / 9;
        ItemMeta meta = filler.getItemMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        filler.setItemMeta(meta);

        for (int i = 0; i < numSlots; i++) {
            if (i < borderSize || i % 9 < borderSize || i % 9 >= 9 - borderSize || i >= numSlots - borderSize) {
                chest.setItem(i, filler);
            }
        }

        SpringBoots jumpBoots = new SpringBoots();
        jumpBoots.setMaterial(Material.IRON_BOOTS);
        jumpBoots.setAbility();
        jumpBoots.setSpeed(0.2);
        ItemStack boots = jumpBoots.createBoots();
        chest.setItem(0, boots);

        openChests.put(player.getUniqueId(), chest);

        player.openInventory(chest);

        return true;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        openChests.remove(player.getUniqueId());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!openChests.containsValue(event.getInventory())) {
            return;
        }

        ItemStack item = event.getCurrentItem();
        if (item != null && item.getType() == Material.BARRIER && item.getItemMeta().isUnbreakable()) {
            event.setCancelled(true);
        }
    }

}
