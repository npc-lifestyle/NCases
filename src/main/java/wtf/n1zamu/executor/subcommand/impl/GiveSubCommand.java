package wtf.n1zamu.executor.subcommand.impl;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import wtf.n1zamu.NCases;
import wtf.n1zamu.executor.subcommand.SubCommand;

import java.util.HashMap;
import java.util.Map;

public class GiveSubCommand implements SubCommand {
    @Override
    public String getName() {
        return "give";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            return;
        }
        String playerName = args[1];

        String caseName = args[2];
        if (!NCases.getInstance().getCrateManager().getCrateByTitle(caseName).isPresent()) {
            sender.sendMessage(">> Такого кейса не существует!");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Укажите <amount> в виде числа!");
            return;
        }
        int cases = NCases.getInstance().getCrateDatabase().getKeys(playerName, caseName);
        NCases.getInstance().getCrateDatabase().updateKeyAmount(playerName, caseName, cases + amount);
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null || !player.isOnline()) {
            return;
        }
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%player%", playerName);
        placeholders.put("%amount%", String.valueOf(amount));
        placeholders.put("%case%", caseName);
        NCases.getInstance().getMessageManager().sendMessage(player, "givedCases", placeholders);
    }
}
