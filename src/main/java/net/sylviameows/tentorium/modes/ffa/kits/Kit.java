package net.sylviameows.tentorium.modes.ffa.kits;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class Kit {
    protected HashMap<Integer, ItemStack> items = new HashMap<>();

    abstract public String id();
    abstract public Material icon(); // icon is currently unused, may be used in the future if kit gui gets generated procedurally

    public Component name() {
        return Component.text(id());
    }
    public Component description() {
        return Component.empty();
    }

    public void apply(Player player) {
        var inventory = player.getInventory();
        inventory.clear();

        items.forEach(inventory::setItem);
    }
}
