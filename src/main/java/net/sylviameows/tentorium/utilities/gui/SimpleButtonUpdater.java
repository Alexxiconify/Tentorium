package net.sylviameows.tentorium.utilities.gui;

import org.bukkit.inventory.Inventory;

import java.util.Map;

public class SimpleButtonUpdater extends AbstractButtonUpdater {
    private final int slot;

    public SimpleButtonUpdater(Inventory inventory, Map<Integer, AbstractButton> buttons, int slot) {
        super(inventory,buttons);
        this.slot = slot;

        AbstractButton button = this.buttons.get(slot);
        button.setUpdater(this);
    }

    @Override
    public void updateItem() {
        AbstractButton button = buttons.get(slot);
        inventory.setItem(slot, button.getItem());
    }
}
