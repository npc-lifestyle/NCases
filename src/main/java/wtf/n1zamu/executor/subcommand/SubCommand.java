package wtf.n1zamu.executor.subcommand;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    String getName();

    void execute(CommandSender sender, String[] args);
}
