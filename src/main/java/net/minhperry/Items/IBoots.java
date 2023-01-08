package net.minhperry.Items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

public interface IBoots {
    ItemStack createBoots();

    void setMaterial(Material material);

    void setAbility(Consumer<Player> ability);

    void setSpeed(double speed);
}
