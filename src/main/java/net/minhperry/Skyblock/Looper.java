package net.minhperry.Skyblock;

import net.minhperry.SomePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Looper {
    private int delay;

    public Looper(int delay) {
        this.delay = delay;
    }

    public void executeCommand(CommandSender sender, int times, String command) {
        boolean isPlayer = false;
        if (sender instanceof Player) isPlayer = true;
        for (int i = 0; i < times; i++) {
            if (isPlayer) ((Player) sender).performCommand(command);
            else Bukkit.getServer().dispatchCommand(sender, command);
        }
    }

    public void register() {
        SomePlugin.getInstance().getCommand("loop").setExecutor(new LoopCommand());
    }

    private class LoopCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length < 2) {
                sender.sendMessage("Usage: /loop <times> <command>");
                return true;
            }

            int times;
            try {
                times = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Error: <times> must be an integer.");
                return true;
            }

            StringBuilder commandBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                commandBuilder.append(args[i]).append(" ");
            }
            String cmd = commandBuilder.toString().trim();

            executeCommand(sender, times, cmd);
            return true;
        }
    }
}
