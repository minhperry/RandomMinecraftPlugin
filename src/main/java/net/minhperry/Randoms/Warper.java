package net.minhperry.Randoms;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Warper implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 1) {
                String dimension = args[0];
                World targetWorld = null;
                double x = player.getLocation().getX();
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();
                float yaw = player.getLocation().getYaw();
                float pitch = player.getLocation().getPitch();

                if (dimension.equalsIgnoreCase("overworld")) {
                    targetWorld = Bukkit.getWorld("world");
                } else if (dimension.equalsIgnoreCase("nether")) {
                    targetWorld = Bukkit.getWorld("world_nether");
                    x = x / 8;
                    z = z / 8;
                } else if (dimension.equalsIgnoreCase("end")) {
                    targetWorld = Bukkit.getWorld("world_the_end");
                }

                if (targetWorld != null) {
                    Location targetLocation = new Location(targetWorld, x, y, z, yaw, pitch);
                    player.teleport(targetLocation);
                } else {
                    player.sendMessage("Invalid dimension. Use 'overworld', 'nether', or 'end'.");
                }
            } else {
                player.sendMessage("Usage: /warp <dimension>");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList("overworld", "nether", "end"), completions);
        }
        return completions;
    }

}
