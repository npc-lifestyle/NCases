package wtf.n1zamu.executor.subcommand.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.n1zamu.NCases;
import wtf.n1zamu.executor.subcommand.SubCommand;
import wtf.n1zamu.utility.animation.Animation;

public class ReloadSubCommand implements SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return;
        }
        NCases.getInstance().getAnimations().forEach(Animation::stop);
        NCases.getInstance().reloadConfig();
        NCases.getInstance().getMessageManager().load();
        NCases.getInstance().getCrateManager().load();
        NCases.getInstance().getInventoryGUIManager().reloadConfig();
        NCases.getInstance().getHologramManager().load();
        NCases.getInstance().getMessageManager().sendMessage((Player) sender, "config_reloaded");
    }
}
