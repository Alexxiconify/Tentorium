package net.sylviameows.tentorium.utilities.gui;

import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.Set;

public class MultipleButtonUpdater extends AbstractButtonUpdater {
    private final Set<Integer> slots;

    public MultipleButtonUpdater(Inventory inventory, Map<Integer, AbstractButton> buttons, Set<Integer> slots) {
        super(inventory, buttons);
        this.slots = slots;

        for (int i : slots) {
            AbstractButton button = buttons.get(i);
            if (button == null) {
                continue;
            }
            button.setUpdater(this);
        }
    }

    @Override
    public void updateItem() {
        for (int i : slots){
            AbstractButton button = buttons.get(i);
            if (button == null){
                continue;
            }
            inventory.setItem(i, button.getItem());
        }
    }
}
