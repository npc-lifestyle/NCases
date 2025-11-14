package wtf.n1zamu.crate;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import rip.jnic.nativeobfuscator.Native;
import wtf.n1zamu.utility.NMSUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CrateItem {
    private final String title;
    private final Material material;
    private final double chance;
    private final List<String> command;
    private final String headTexture;
    private final int modelData;

    public CrateItem(String title, Material material, double chance, List<String> command, String headTexture, int modelData) {
        this.title = title;
        this.material = material;
        this.chance = chance;
        this.command = command;
        this.headTexture = headTexture;
        this.modelData = modelData;
    }

    public CrateItem(String title, Material material, double chance, String headTexture, int modelData) {
        this.title = title;
        this.material = material;
        this.chance = chance;
        this.command = Collections.singletonList("<none>");
        this.headTexture = headTexture;
        this.modelData = modelData;
    }

    public String getTitle() {
        return title;
    }

    public Material getMaterial() {
        return material;
    }

    public double getChance() {
        return this.chance;
    }

    public List<String> getCommand() {
        return command;
    }

    public ItemStack fromCrateItem() {
        ItemStack itemStack = new ItemStack(material);
        if (material != Material.PLAYER_HEAD) {
            ItemMeta meta = itemStack.getItemMeta();
            if (modelData != -1) {
                meta.setCustomModelData(modelData);
            }
            itemStack.setItemMeta(meta);
            return itemStack;
        }
        if (headTexture != null) {
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            if (modelData != -1) {
                skullMeta.setCustomModelData(modelData);
            }
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", headTexture));
            try {
                if (NMSUtil.isBelowVersion(1, 15, 2)) {
                    Method mtd = skullMeta.getClass().getDeclaredMethod("profile", GameProfile.class);
                    mtd.setAccessible(true);
                    mtd.invoke(skullMeta, profile);
                } else {
                    Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                    mtd.setAccessible(true);
                    mtd.invoke(skullMeta, profile);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();
            }
            itemStack.setItemMeta(skullMeta);
        } else {
            ItemMeta meta = itemStack.getItemMeta();
            if (modelData != -1) {
                meta.setCustomModelData(modelData);
            }
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    @Native
    public static CrateItem deserialize(ConfigurationSection section) {
        if (!section.contains("title") || !section.contains("material") || !section.contains("chance")) {
            return null;
        }
        Material mat = Material.getMaterial((section.getString("material")).toUpperCase());
        String headId = mat != null && section.contains("texture") ? section.getString("texture") : null;
        int modelData = section.contains("modelData") ? section.getInt("modelData") : -1;
        if (section.contains("command")) {
            return new CrateItem(
                    section.getString("title"),
                    mat,
                    section.getDouble("chance"),
                    section.getStringList("command"),
                    headId,
                    modelData
            );
        } else {
            return new CrateItem(
                    section.getString("title"),
                    mat,
                    section.getDouble("chance"),
                    headId,
                    modelData
            );
        }
    }

    @Override
    public String toString() {
        return "Предмет: " +
                "Имя: " + title +
                ", Материал: " + material +
                ", Шанс: " + chance +
                ", Команды: " + command;
    }
}
