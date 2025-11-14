package wtf.n1zamu.gui.holder;

import com.google.common.collect.ImmutableMap;
import rip.jnic.nativeobfuscator.Native;
import wtf.n1zamu.crate.Crate;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class CustomInventoryHolder implements InventoryHolder {

    private final String id;

    private final Map<String, Object> data;

    public CustomInventoryHolder(String id) {
        this.id = id;
        this.data = new HashMap<>();
    }

    public CustomInventoryHolder(String id, Map<String, Object> data) {
        this.id = id;
        this.data = data;
    }

    @Native
    public CustomInventoryHolder(String id, Crate crate) {
        this(id, ImmutableMap.<String, Object>builder().put("case", crate).build());
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}