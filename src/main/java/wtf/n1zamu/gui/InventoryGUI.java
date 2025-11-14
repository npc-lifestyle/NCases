package wtf.n1zamu.gui;

import org.bukkit.ChatColor;
import wtf.n1zamu.NCases;
import wtf.n1zamu.gui.holder.CustomInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import wtf.n1zamu.gui.config.ConfigurationItem;

import java.util.HashMap;
import java.util.Map;

public abstract class InventoryGUI {
    private final String id;
    private String title;
    protected final Map<String, ConfigurationItem> items;

    public InventoryGUI(String id) {
        this.id = id;
        this.items = new HashMap<>();

        ConfigurationSection section = NCases.getInstance().getInventoryGUIManager().getConfig().getConfigurationSection("menu." + this.id);

        if (section == null) {
            Bukkit.getLogger().warning("couldn't found section for path: " + this.id);
            return;
        }

        this.title = section.getString("title");

        ConfigurationSection itemsSection = section.getConfigurationSection("items");

        if (itemsSection == null) {
            Bukkit.getLogger().warning("couldn't found section for path: " + section.getCurrentPath() + ".items");
            return;
        }


        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection s = itemsSection.getConfigurationSection(key);

            if (s == null) {
                Bukkit.getLogger().warning("couldn't found section for path: " + itemsSection.getCurrentPath() + ".key");
                continue;
            }

            ConfigurationItem item = new ConfigurationItem(s);


            this.items.put(key, item);
        }
    }

    public abstract Inventory show(CustomInventoryHolder holder);

    public abstract Inventory show(CustomInventoryHolder holder, Player player);

    public abstract void onClick(InventoryClickEvent event);

    public String getTitle() {
        return ChatColor.translateAlternateColorCodes('&', title);
    }

    public String getId() {
        return id;
    }
}
