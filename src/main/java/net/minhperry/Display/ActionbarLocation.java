package net.minhperry.Display;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minhperry.SomePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class ActionbarLocation extends BukkitRunnable {

    private Player player;

    public ActionbarLocation(Player player) {
        this.player = player;
    }

    public static void startUpdatingPositions(List<Player> players) {
        for (Player player : players) {
            new ActionbarLocation(player).runTaskTimer(SomePlugin.getInstance(), 0, 1);
        }
    }

    @Override
    public void run() {
        int x = (int) player.getLocation().getX();
        int y = (int) player.getLocation().getY();
        int z = (int) player.getLocation().getZ();
        String message = ChatColor.GREEN + "Position: " + x + " / " + y + " / " + z;
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ActionbarLocation.startUpdatingPositions(Collections.singletonList(player));
    }
}

