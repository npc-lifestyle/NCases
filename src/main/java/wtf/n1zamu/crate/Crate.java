package wtf.n1zamu.crate;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import wtf.n1zamu.utility.RandomizationUtility;
import org.bukkit.Location;
import wtf.n1zamu.utility.animation.AnimationInfo;

import java.util.*;

public class Crate {

    private final String title;
    private final CrateType type;
    private Location location;
    private List<CrateItem> items;
    private String direction;
    private boolean state;
    private AnimationInfo animationInfo;
    private RandomizationUtility<CrateItem> randomization;


    public Crate(String title, CrateType type, Location location, List<CrateItem> items, String direction, AnimationInfo animationInfo) {
        this.title = title;
        this.type = type;
        this.location = location;
        this.items = items;
        this.direction = direction;
        this.state = false;
        this.animationInfo = animationInfo;
        this.randomization = new RandomizationUtility<>();
        this.items.forEach(i -> this.randomization.addValue(i, i.getChance()));
    }

    public String getTitle() {
        return title;
    }

    public Location getLocation() {
        return location;
    }

    public String getDirection() {
        return this.direction;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public static Crate getBySection(ConfigurationSection section) {
        String title = section.getString("title");
        CrateType type = CrateType.getCrateTypeByName(section.getString("type"));
        String animationDirection = section.getString("animation_direction");
        Location location = null;
        List<CrateItem> itemList = new ArrayList<>();
        AnimationInfo animInfo = null;
        for (String key : section.getKeys(false)) {
            if (!section.isConfigurationSection(key)) {
                continue;
            }
            if (key.equalsIgnoreCase("location")) {
                ConfigurationSection locationSection = section.getConfigurationSection(key);
                if (locationSection == null) {
                    continue;
                }
                World world = Bukkit.getWorld(locationSection.getString("world"));
                int x = locationSection.getInt("x");
                int y = locationSection.getInt("y");
                int z = locationSection.getInt("z");
                location = new Location(world, x, y, z);
            }
            if (key.equalsIgnoreCase("animation_info")) {
                animInfo = AnimationInfo.deserialize(section.getConfigurationSection(key));
            }
            CrateItem item = CrateItem.deserialize(section.getConfigurationSection(key));
            if (item != null) {
                itemList.add(item);
            }
        }
        return new Crate(title, type, location, itemList, animationDirection, animInfo);
    }

    public CrateType getType() {
        return this.type;
    }

    public List<CrateItem> getItems() {
        return items;
    }

    public RandomizationUtility<CrateItem> getCaseItemRandomizator() {
        return this.randomization;
    }

    public AnimationInfo getAnimationInfo() {
        return animationInfo;
    }

    @Override
    public String toString() {
        return "Кейс: \n" +
                "Название: '" + title + '\n' +
                "Тип: " + type + '\n' +
                "Локация: " + location + '\n' +
                "Предметы: " + items;
    }
}
