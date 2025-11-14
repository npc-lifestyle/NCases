package wtf.n1zamu.hook.impl;

import wtf.n1zamu.hook.PluginHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook extends PluginHook {
    private static Economy economy;

    @Override
    public String getName() {
        return "Vault";
    }

    @Override
    protected boolean setup() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;

        economy = rsp.getProvider();
        return true;
    }

    public static Economy getEconomy() {
        return economy;
    }
}
