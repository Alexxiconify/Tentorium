package net.sylviameows.tentorium.utilities.gui;

import org.bukkit.inventory.ItemStack;

public abstract class AbstractButton {
    private AbstractButtonUpdater updater;

    public void setUpdater(AbstractButtonUpdater updater) {
        this.updater = updater;
    }

    public AbstractButtonUpdater getUpdater() {
        return updater;
    }

    public abstract ItemStack getItem();
}
