package net.minhperry.Skyblock;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;

public class Crafter implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("craft")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.openWorkbench(player.getLocation(), true);
                return true;
            }
        }
        return false;
    }
}
