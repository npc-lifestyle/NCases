package wtf.n1zamu.hook;

import org.bukkit.Bukkit;

public abstract class PluginHook {
    public abstract String getName();

    protected abstract boolean setup();

    private boolean exists() {
        return Bukkit.getServer().getPluginManager().getPlugin(this.getName()) != null;
    }

    public boolean pass() {
        return exists() && setup();
    }
}
