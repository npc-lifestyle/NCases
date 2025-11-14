package wtf.n1zamu.listener;

import wtf.n1zamu.NCases;
import wtf.n1zamu.gui.InventoryGUI;
import wtf.n1zamu.gui.holder.CustomInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class GUIListener implements Listener {
    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (!(inventory.getHolder() instanceof CustomInventoryHolder)) {
            return;
        }
        CustomInventoryHolder customInventoryHolder = (CustomInventoryHolder) inventory.getHolder();
        String inventoryId = customInventoryHolder.getId();

        ItemStack clickedItemStack = event.getCurrentItem();

        if (clickedItemStack == null) {
            return;
        }
        event.setCancelled(true);

        HashMap<String, InventoryGUI> guis = NCases.getInstance().getInventoryGUIManager().getGuis();

        if (!guis.containsKey(inventoryId)) {
            return;
        }
        guis.get(inventoryId).onClick(event);
    }
}
