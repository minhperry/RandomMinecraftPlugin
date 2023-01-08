package net.minhperry.Skyblock;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Menu implements Listener {
    private static final int MENU_SIZE = 27;
    private static final int CRAFTING_TABLE_INDEX = 10;
    private static final int ENDERCHEST_INDEX = 12;
    private static final int OVERWORLD_WARP_INDEX = 14;
    private static final int NETHER_WARP_INDEX = 15;
    private static final int END_WARP_INDEX = 16;
    private static final Material FILL_MATERIAL = Material.GRAY_STAINED_GLASS_PANE;

    private final ItemStack menuItem;
    private final ItemStack craftingTableItem;
    private final ItemStack enderChestItem;
    private final ItemStack overworldWarpItem;
    private final ItemStack netherWarpItem;
    private final ItemStack endWarpItem;

    public Menu() {
        this.menuItem = createMenuItem();
        this.craftingTableItem = new ItemStack(Material.CRAFTING_TABLE);
        this.enderChestItem = new ItemStack(Material.ENDER_CHEST);
        this.overworldWarpItem = createWarpItem(Material.GRASS_BLOCK, ChatColor.WHITE + "Overworld");
        this.netherWarpItem = createWarpItem(Material.NETHERRACK, ChatColor.WHITE + "Nether");
        this.endWarpItem = createWarpItem(Material.END_STONE, ChatColor.WHITE + "End");
    }

    public ItemStack getMenuItem() {
        return menuItem;
    }

    public Inventory createMenuInventory(int size) {
        Inventory inventory = Bukkit.createInventory(null, size, ChatColor.BLUE + "Menu");
        for (int i = 0; i < size; i++) {
            if (i == CRAFTING_TABLE_INDEX) {
                inventory.setItem(i, craftingTableItem);
            } else if (i == ENDERCHEST_INDEX) {
                inventory.setItem(i, enderChestItem);
            } else if (i == OVERWORLD_WARP_INDEX) {
                inventory.setItem(i, overworldWarpItem);
            } else if (i == NETHER_WARP_INDEX) {
                inventory.setItem(i, netherWarpItem);
            } else if (i == END_WARP_INDEX) {
                inventory.setItem(i, endWarpItem);
            } else {
                inventory.setItem(i, new ItemStack(FILL_MATERIAL, 1));
            }
        }
        return inventory;
    }

    private ItemStack createMenuItem() {
        ItemStack menuItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = menuItem.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Menu");
        menuItem.setItemMeta(meta);
        return menuItem;
    }

    private ItemStack createWarpItem(Material material, String name) {
        ItemStack warpItem = new ItemStack(material);
        ItemMeta meta = warpItem.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + name);
        warpItem.setItemMeta(meta);
        return warpItem;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            ItemStack item = event.getItem();
            if (item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.getDisplayName().equals(ChatColor.WHITE + "Menu")) {
                    event.setCancelled(true);
                    Inventory menuInventory = createMenuInventory(MENU_SIZE);
                    event.getPlayer().openInventory(menuInventory);
                }
            } else {
                PlayerInventory playerInventory = event.getPlayer().getInventory();
                int lastHotbarIndex = 8;
                ItemStack lastHotbarItem = playerInventory.getItem(lastHotbarIndex);
                if (lastHotbarItem == null || lastHotbarItem.getType() == Material.AIR) {
                    playerInventory.setItem(lastHotbarIndex, menuItem);
                }
            }
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();

        if (inventory != null && event.getView().getTitle().equals(ChatColor.BLUE + "Menu")) {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {
                event.setCancelled(true);
                if (clickedItem.isSimilar(craftingTableItem)) {
                    event.getWhoClicked().openWorkbench(player.getLocation(), true);
                } else if (clickedItem.isSimilar(enderChestItem)) {
                    event.getWhoClicked().openInventory(player.getEnderChest());
                } else if (clickedItem.isSimilar(overworldWarpItem)) {
                    ((Player) event.getWhoClicked()).performCommand("warp overworld");
                } else if (clickedItem.isSimilar(netherWarpItem)) {
                    ((Player) event.getWhoClicked()).performCommand("warp nether");
                } else if (clickedItem.isSimilar(endWarpItem)) {
                    ((Player) event.getWhoClicked()).performCommand("warp end");
                }
            }
        }
    }
}