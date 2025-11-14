package wtf.n1zamu.executor;

import org.bukkit.entity.Player;

import wtf.n1zamu.NCases;
import wtf.n1zamu.crate.Crate;

import wtf.n1zamu.executor.subcommand.impl.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.checkerframework.checker.nullness.qual.NonNull;


import java.util.*;
import java.util.stream.Collectors;

public class CaseCommandExecutor implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String s, @NonNull String[] strings) {
        if (!commandSender.hasPermission("nCases.admin")) {
            NCases.getInstance().getMessageManager().sendMessage((Player) commandSender, "not_permission");
            return false;
        }

        if (strings.length < 1) {
            commandSender.sendMessage(new String[]{
                    "// NCases v1.0",
                    ">> /ncase - помощь",

                    ">> /ncase set <player> <case> <amount> - сетнуть кол-во кейсов",
                    ">> /ncase take <player> <case> <amount> - забрать кол-во кейсов",
                    ">> /ncase look <player> - посмотреть все кейсы игрока",
                    ">> /ncase give <player> <case> <amount> - добавить кейсы игроку",
                    ">> /ncase reload - перезагрузка конфигураций"
            });
            return true;
        }
        String commandType = strings[0];
        Arrays.asList(new ReloadSubCommand(), new LookSubCommand(), new GiveSubCommand(), new SetSubCommand(), new TakeSubCommand()).forEach(subCommand -> {
            if (!subCommand.getName().equalsIgnoreCase(commandType)) {
                return;
            }
            subCommand.execute(commandSender, strings);
        });
        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String s, @NonNull String[] strings) {
        if (strings.length == 1) {
            return Arrays.asList(
                    "set",
                    "take",
                    "look",
                    "give",
                    "reload"
            );
        }
        if (strings.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }
        if (strings.length == 3) {
            String string0 = strings[0];
            if (string0.equalsIgnoreCase("take") || string0.equalsIgnoreCase("set") || string0.equalsIgnoreCase("give")) {
                return NCases.getInstance().getCrateManager().getCrates().stream().map(Crate::getTitle).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}
