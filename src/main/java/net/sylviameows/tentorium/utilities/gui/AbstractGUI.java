package net.sylviameows.tentorium.utilities.gui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGUI {

    private final Inventory inv;
    private final Map<Integer, AbstractButton> buttonMap = new HashMap<>();


    public final Inventory getInventory() {
        // We make sure to register the gui
        GUIHandler.registerGUI(inv, this);
        return inv;
    }

    public Map<Integer, AbstractButton> getButtonMap() {
        return buttonMap;
    }


    public AbstractGUI(Template template){
        this.inv = template.createInventory();
    }


    public void onOpen(InventoryOpenEvent event){

    }

    public void onClose(InventoryCloseEvent event){

    }

    public void onClick(InventoryClickEvent event){
        int slot = event.getSlot();

        AbstractButton button = buttonMap.get(slot);
        if (button == null) {
            return;
        }

        if (button instanceof ClickableButton) {
            ClickableButton clickableButton = (ClickableButton) button;
            clickableButton.onClick(event);

            event.getWhoClicked().playSound(
                    Sound.sound(Key.key("block.bamboo.place"), Sound.Source.MASTER, 1f, 1f)
            );

        }
    }
}
