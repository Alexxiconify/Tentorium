package net.sylviameows.tentorium.utilities.gui;

import org.bukkit.inventory.Inventory;

import java.util.Map;

public abstract class AbstractButtonUpdater {
    protected final Inventory inventory;
    protected final Map<Integer, AbstractButton> buttons;

    public AbstractButtonUpdater(Inventory inventory, Map<Integer, AbstractButton> buttons) {
        this.inventory = inventory;
        this.buttons = buttons;
    }

    public abstract void updateItem();
}
