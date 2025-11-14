package wtf.n1zamu.crate;

import org.bukkit.configuration.ConfigurationSection;
import wtf.n1zamu.NCases;
import org.bukkit.Location;

import java.util.*;

public class CrateManager {
    private List<Crate> crates;

    public CrateManager() {
        load();
    }

    public void load() {
        this.crates = new ArrayList<>();
        NCases.getInstance().getConfig().getConfigurationSection("cases").getKeys(false).forEach(key -> {
            ConfigurationSection section = NCases.getInstance().getConfig().getConfigurationSection("cases." + key);
            if (section == null) {
                return;
            }
            Crate crate = Crate.getBySection(section);
            this.crates.add(crate);
        });
    }

    public Optional<Crate> getCrateByTitle(String title) {
        return this.crates.stream().filter(crate -> crate.getTitle().equals(title)).findFirst();
    }

    public Optional<Crate> getCrateByLocation(Location location) {
        return this.crates.stream().filter(crate -> crate.getLocation() != null && crate.getLocation().equals(location)).findFirst();
    }

    public List<Crate> getCrates() {
        return crates;
    }
}
