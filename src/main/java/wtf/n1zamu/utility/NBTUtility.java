package wtf.n1zamu.utility;

import wtf.n1zamu.NCases;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class NBTUtility {
    public static void addItemNBT(org.bukkit.inventory.ItemStack item, String key, String value) {
        ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
        meta.getPersistentDataContainer().set(new NamespacedKey(NCases.getInstance(), key), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }

    public static boolean hasItemNBT(ItemStack item, String key) {
        return item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(NCases.getInstance(), key), PersistentDataType.STRING);

    }
}
