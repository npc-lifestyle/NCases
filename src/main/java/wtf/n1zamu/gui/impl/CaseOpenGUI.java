package wtf.n1zamu.gui.impl;

import wtf.n1zamu.NCases;
import wtf.n1zamu.crate.Crate;
import wtf.n1zamu.gui.InventoryGUI;
import wtf.n1zamu.gui.holder.CustomInventoryHolder;
import wtf.n1zamu.utility.NBTUtility;
import wtf.n1zamu.utility.animation.impl.RotatingCircleAnimation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CaseOpenGUI extends InventoryGUI {

    public CaseOpenGUI(Crate crate) {
        super(crate.getTitle() + ".openGui");
    }

    @Override
    public Inventory show(CustomInventoryHolder holder) {
        return null;
    }

    @Override
    public Inventory show(CustomInventoryHolder holder, Player player) {
        Inventory inventory = Bukkit.createInventory(holder, 54, this.getTitle());
        Crate crate = (Crate) (holder).getData().get("case");
        Map<String, String> placeHolders = new HashMap<>();
        int keyCount = NCases.getInstance().getCrateDatabase().getKeys(player.getName(), crate.getTitle());
        placeHolders.put("keys", String.valueOf(keyCount));
        ItemStack openItem = this.items.get("openCaseItem").build(placeHolders);
        NBTUtility.addItemNBT(openItem, "open", "");
        this.items.get("openCaseItem").getSlot().forEach(slot -> inventory.setItem(slot, openItem));

        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItemStack = event.getCurrentItem();
        if (!clickedItemStack.hasItemMeta()) {
            return;
        }
        if (!NBTUtility.hasItemNBT(clickedItemStack, "open")) {
            return;
        }
        CustomInventoryHolder customInventoryHolder = ((CustomInventoryHolder) event.getInventory().getHolder());
        Crate crate = (Crate) customInventoryHolder.getData().get("case");
        if (crate.getState()) {
            NCases.getInstance().getMessageManager().sendMessage(player, "case_reserved");
            return;
        }

        int keyCount = NCases.getInstance().getCrateDatabase().getKeys(player.getName(), crate.getTitle());

        if (keyCount == 0) {
            NCases.getInstance().getMessageManager().sendMessage(player, "not_keys");
            return;
        }
        player.closeInventory();
        NCases.getInstance().getCrateDatabase().updateKeyAmount(player.getName(), crate.getTitle(), keyCount - 1);

        crate.setState(true);
        Map<String, String> placeholder = new HashMap<>();
        placeholder.put("%case%", crate.getTitle());
        NCases.getInstance().getMessageManager().sendMessage(player, "case_open", placeholder);
        RotatingCircleAnimation circleAnimationV2 = new RotatingCircleAnimation(crate, crate.getLocation(), player);
        circleAnimationV2.run();
    }
}
