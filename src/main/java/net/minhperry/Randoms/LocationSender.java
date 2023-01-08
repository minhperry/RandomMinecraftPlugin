package net.minhperry.Randoms;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LocationSender implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sendloc")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location location = player.getLocation();
                String dimension = location.getWorld().getName();
                String tpDim;
                switch (dimension) {
                    case "world":
                    default:
                        dimension = "Overworld";
                        tpDim = "overworld";
                        break;
                    case "world_the_end":
                        dimension = "The End";
                        tpDim = "the_end";
                        break;
                    case "world_nether":
                        dimension = "The Nether";
                        tpDim = "the_nether";
                        break;
                }

                String message = ChatColor.GREEN + player.getName() + " is currently at " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " in " + dimension;
                TextComponent textComponent = new TextComponent(message);
                // /execute in DIMENSION run tp PLAYER x y z
                String tpCmd = "/execute in " + tpDim + " run tp " + player.getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, tpCmd));
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to teleport to " + player.getName()).create()));
                for (Player recipient : Bukkit.getOnlinePlayers()) {
                    recipient.spigot().sendMessage(ChatMessageType.CHAT, textComponent);
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
                return true;
            }
        }
        return false;

    }
}
