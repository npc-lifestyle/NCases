package wtf.n1zamu.gui;

import rip.jnic.nativeobfuscator.Native;
import wtf.n1zamu.NCases;
import wtf.n1zamu.crate.Crate;
import wtf.n1zamu.gui.impl.CaseOpenGUI;
import wtf.n1zamu.gui.holder.CustomInventoryHolder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class InventoryGUIManager {
    private HashMap<String, InventoryGUI> guis;
    private FileConfiguration configuration;

    public InventoryGUIManager() {
        File configurationFile = new File(
                NCases.getInstance().getDataFolder(),
                "admin.yml"
        );

        if (!configurationFile.exists())
            NCases.getInstance().saveResource("admin.yml", false);

        this.configuration = YamlConfiguration.loadConfiguration(configurationFile);
    }

    public void load() {
        this.guis = new HashMap<>();
        NCases.getInstance().getCrateManager().getCrates().forEach(crate -> {
            InventoryGUI gui = new CaseOpenGUI(crate);
            this.guis.put(gui.getId(), gui);
        });
    }


    public Inventory show(String id) {
        return this.guis.get(id).show(new CustomInventoryHolder(id));
    }

    @Native
    public Inventory show(String id, Crate crate, Player player) {
        return this.guis.get(id).show(new CustomInventoryHolder(id, crate), player);
    }

    public void reloadConfig() {
        File configurationFile = new File(
                NCases.getInstance().getDataFolder(),
                "admin.yml"
        );
        this.configuration = YamlConfiguration.loadConfiguration(configurationFile);
        load();
    }

    public HashMap<String, InventoryGUI> getGuis() {
        return guis;
    }

    public FileConfiguration getConfig() {
        return configuration;
    }
}
