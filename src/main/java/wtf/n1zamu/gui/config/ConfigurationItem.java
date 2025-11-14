package wtf.n1zamu.gui.config;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigurationItem {
    private String title;
    private Material material;
    private List<String> lore;
    private int modelData = -1;
    private List<Integer> slot;

    public ConfigurationItem(ConfigurationSection section) {
        if (section.contains("title"))
            this.title = ChatColor.translateAlternateColorCodes('&', section.getString("title"));
        if (section.contains("item"))
            this.material = Material.getMaterial(section.getString("item", "stone").toUpperCase());
        if (section.contains("lore"))
            this.lore = section.getStringList("lore")
                    .stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .collect(Collectors.toList());
        if (section.contains("slot"))
            this.slot = section.getIntegerList("slot");
        if (section.contains("modelData"))
            this.modelData = section.getInt("modelData");
    }

    public ItemStack build() {
        return this.build(new HashMap<>());
    }

    public ItemStack build(Map<String, String> placeholders) {
        ItemStack itemStack = new ItemStack(this.material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            String title = this.title;
            List<String> lore = this.lore;
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String placeholder = "%" + entry.getKey() + "%";
                String placeholderValue = entry.getValue();

                if (this.title != null)
                    title = title.replace(placeholder, placeholderValue);
                if (this.lore != null)
                    lore = lore.stream().map(line -> line.replace(placeholder, placeholderValue)).collect(Collectors.toList());
            }

            if (title != null) {
                itemMeta.setDisplayName(title);
            }
            if (lore != null) {
                itemMeta.setLore(lore);
            }
            if (modelData != -1) {
                itemMeta.setCustomModelData(modelData);
            }
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public List<Integer> getSlot() {
        return slot;
    }
}