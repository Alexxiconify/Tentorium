package net.sylviameows.tentorium.utilities.gui;

import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class GUIHandler {
    private static final Map<Inventory, AbstractGUI> validGUIs = new HashMap<>();

    public static void registerGUI(Inventory inv, AbstractGUI gui){
        if (inv.getViewers().isEmpty()){
            validGUIs.put(inv, gui);
        }
    }

    public static void unregisterGUI(Inventory inventory){
        if (inventory.getViewers().size() <= 1){
            validGUIs.remove(inventory);
        }
    }

    public static AbstractGUI getRegisteredGUI(Inventory inventory){
        return validGUIs.get(inventory);
    }
}
