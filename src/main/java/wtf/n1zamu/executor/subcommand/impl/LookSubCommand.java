package wtf.n1zamu.executor.subcommand.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import wtf.n1zamu.NCases;
import wtf.n1zamu.executor.subcommand.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class LookSubCommand implements SubCommand {
    @Override
    public String getName() {
        return "look";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return;
        }
        String playerName = args[1];

        Map<String, Integer> allKeys = NCases.getInstance().getCrateDatabase().getAllKeys(playerName);
        if (allKeys.isEmpty()) {
            return;
        }
        allKeys.forEach((key, value) -> {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%player%", playerName);
            placeholders.put("%amount%", String.valueOf(value));
            placeholders.put("%case%", key);
            NCases.getInstance().getMessageManager().sendMessage((Player) sender, "lookCase", placeholders);
        });
    }
}
