package net.minhperry;

import net.minhperry.Display.ActionbarLocation;
import net.minhperry.Display.HealthDisplayer;
import net.minhperry.Display.ScoreboardLocation;
import net.minhperry.Randoms.*;
import net.minhperry.Randoms.ArmorStandManager;
import net.minhperry.Skyblock.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class SomePlugin extends JavaPlugin {

    private static SomePlugin instance;

    private ArmorStandManager asm;

    public static SomePlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new PathWriter(), this);
        getServer().getPluginManager().registerEvents(new BlockLauncher(), this);

        ChestInv inv = new ChestInv();
        getCommand("ch").setExecutor(inv);
        getServer().getPluginManager().registerEvents(inv, this);

        getCommand("warp").setExecutor(new Warper());

        // asm = new ArmorStandManager();
        // asm.enable();

        List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
        for (Player player : players) {
            new ActionbarLocation(player).runTaskTimer(this, 0, 1);
        }

        getCommand("sendloc").setExecutor(new LocationSender());

        getCommand("term").setExecutor(new Terminator());
        getServer().getPluginManager().registerEvents(new Terminator(), this);

        new Looper(1).register();

        getServer().getPluginManager().registerEvents(new AspectOfTheVoid(this), this);

        getServer().getPluginManager().registerEvents(new GlacialScythe(this), this);

        getServer().getPluginManager().registerEvents(new HealthDisplayer(), this);

        getServer().getPluginManager().registerEvents(new Hyperion(this), this);

        getCommand("craft").setExecutor(new Crafter());

        getServer().getPluginManager().registerEvents(new Menu(), this);
    }

    @Override
    public void onDisable() {
        // asm.disable();
    }
}
