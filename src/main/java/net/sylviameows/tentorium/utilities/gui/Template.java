package net.sylviameows.tentorium.utilities.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Template {

    private final String invName;
    private final char[] slotsChars;

    public Template(String invName, List<String> rows){
        this.invName = invName;

        StringBuilder builder = new StringBuilder();
        for (String row : rows) {
            builder.append(row.replace(" ", ""));
        }

        this.slotsChars = builder.toString().toCharArray();
    }

    public Inventory createInventory(){
        return Bukkit.createInventory(null, slotsChars.length, invName);
    }

    public void addButtons(AbstractGUI gui, char character, AbstractButton button){
        for (int slot : getSlotsByChar(character)){
            gui.getButtonMap().put(slot, button);
            gui.getInventory().setItem(slot, button.getItem());
        }
    }

    public void addButton(AbstractGUI gui, char character, AbstractButton button){
        for (int slot : getSlotsByChar(character)){
            if (gui.getButtonMap().containsKey(slot)){
                continue;
            }
            gui.getButtonMap().put(slot, button);
            gui.getInventory().setItem(slot, button.getItem());
            break;
        }
    }

    public SimpleButtonUpdater createSimpleUpdater(AbstractGUI gui, char slot){
        return new SimpleButtonUpdater(gui.getInventory(), gui.getButtonMap(), getSlotByChar(slot));
    }

    public MultipleButtonUpdater createMultipleUpdater(AbstractGUI gui, char... slots){
        Set<Integer> numSlots = new HashSet<>();
        for (char slot : slots){
            numSlots.addAll(getSlotsByChar(slot));
        }
        return new MultipleButtonUpdater(gui.getInventory(), gui.getButtonMap(), numSlots);
    }

    public int getSlotByChar(char slotChar){
        for (int i = 0; i < slotsChars.length; i ++){
            if (slotsChars[i]  == slotChar){
                return i;
            }
        }
        return -1;
    }

    public Set<Integer> getSlotsByChar(char slotChar){
        Set<Integer> slots = new HashSet<>();
        for (int slot = 0; slot < slotsChars.length; slot ++){
            if (slotsChars[slot]  == slotChar){
                slots.add(slot);
            }
        }
        return slots;
    }

}
