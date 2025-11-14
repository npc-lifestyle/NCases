package wtf.n1zamu.manager;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.configuration.ConfigurationSection;
import wtf.n1zamu.NCases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramManager {
    private Map<String, Hologram> crateHologramMap;

    public HologramManager() {
        load();
    }

    public void load() {
        this.crateHologramMap = new HashMap<>();
        ConfigurationSection hologramSection = NCases.getInstance().getConfig().getConfigurationSection("holograms");
        if (hologramSection == null) {
            return;
        }
        NCases.getInstance().getCrateManager().getCrates().forEach(crate -> {
            if (!hologramSection.contains(crate.getTitle())) {
                return;
            }
            List<String> hologramLines = hologramSection.getStringList(crate.getTitle());
            String holoName = crate.getTitle() + "_hologram";
            if (DHAPI.getHologram(holoName) != null) {
                DHAPI.removeHologram(holoName);
            }
            Hologram hologram = DHAPI.createHologram(holoName, crate.getLocation().clone().add(0.5, 1.5, 0.5), hologramLines);
            crateHologramMap.put(crate.getTitle(), hologram);
        });
    }

    public Map<String, Hologram> getCrateHologramMap() {
        return crateHologramMap;
    }
}
