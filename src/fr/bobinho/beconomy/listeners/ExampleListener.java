package fr.bobinho.beconomy.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Listener of example
 */
public final class ExampleListener implements Listener {

    /**
     * Listen when a player click on a loot table inventory
     *
     * @param e the inventory click event
     */
    @EventHandler
    public void onPlayerClickInInventory(InventoryClickEvent e) {

        e.setCancelled(true);
    }

}
