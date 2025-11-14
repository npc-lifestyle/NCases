package wtf.n1zamu.listener;

import wtf.n1zamu.NCases;
import wtf.n1zamu.crate.Crate;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.*;

public class CaseListener implements Listener {
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getHand() == null) {
            return;
        }

        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null) {
            return;
        }
        Optional<Crate> possibleCaseBlock = NCases.getInstance().getCrateManager().getCrateByLocation(clickedBlock.getLocation());

        if (!possibleCaseBlock.isPresent()) {
            return;
        }
        event.setCancelled(true);

        Crate crate = possibleCaseBlock.get();
        if (crate.getState()) {
            NCases.getInstance().getMessageManager().sendMessage(event.getPlayer(), "case_reserved");
            return;
        }
        event.getPlayer().openInventory(NCases.getInstance().getInventoryGUIManager().show(crate.getTitle() + ".openGui", crate, event.getPlayer()));
    }
}

