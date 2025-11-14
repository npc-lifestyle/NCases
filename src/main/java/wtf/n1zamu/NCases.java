package wtf.n1zamu;

import wtf.n1zamu.crate.CrateManager;
import wtf.n1zamu.database.CrateDatabase;
import wtf.n1zamu.executor.CaseCommandExecutor;
import wtf.n1zamu.gui.InventoryGUIManager;
import wtf.n1zamu.hook.PluginHook;
import wtf.n1zamu.hook.impl.PlayerPointsHook;
import wtf.n1zamu.hook.impl.VaultHook;
import wtf.n1zamu.listener.CaseListener;
import wtf.n1zamu.listener.GUIListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.n1zamu.utility.animation.Animation;
import wtf.n1zamu.utility.hologram.HologramManager;
import wtf.n1zamu.utility.message.MessageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NCases extends JavaPlugin {

    private static NCases INSTANCE;
    private CrateManager crateManager;
    private CrateDatabase crateDatabase;
    private MessageManager messageManager;
    private InventoryGUIManager inventoryGUIManager;
    private HologramManager hologramManager;
    private final List<Animation> animations = new ArrayList<>();

    @Override
    public void onEnable() {
        INSTANCE = this;

        if (!this.setupHooks()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.saveDefaultConfig();

        this.crateDatabase = new CrateDatabase();
        this.crateManager = new CrateManager();
        this.messageManager = new MessageManager();
        this.hologramManager = new HologramManager();
        this.hologramManager.load();
        this.inventoryGUIManager = new InventoryGUIManager();
        this.inventoryGUIManager.load();

        Arrays.asList(this.getCommand("ncase"), this.getCommand("cases"))
                .forEach(command -> {
                    if (command == null) {
                        return;
                    }
                    command.setExecutor(new CaseCommandExecutor());
                    command.setTabCompleter(new CaseCommandExecutor());
                });

        this.getServer().getPluginManager().registerEvents(new GUIListener(), this);
        this.getServer().getPluginManager().registerEvents(new CaseListener(), this);
    }

    private boolean setupHooks() {
        for (PluginHook pluginHook : Arrays.asList(
                new PlayerPointsHook(),
                new VaultHook()
        )) {
            if (!pluginHook.pass()) {
                Bukkit.getLogger().warning(String.format("failed to load %s.", pluginHook.getName()));
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDisable() {
        animations.forEach(Animation::stop);
        if (this.crateDatabase == null) {
            return;
        }
        Bukkit.getScheduler().cancelTasks(this);
        this.crateDatabase.closeConnection();
    }


    public static NCases getInstance() {
        return INSTANCE;
    }

    public CrateManager getCrateManager() {
        return crateManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public List<Animation> getAnimations() {
        return animations;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

    public InventoryGUIManager getInventoryGUIManager() {
        return inventoryGUIManager;
    }

    public CrateDatabase getCrateDatabase() {
        return crateDatabase;
    }
}
