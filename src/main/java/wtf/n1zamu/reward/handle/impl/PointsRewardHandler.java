package wtf.n1zamu.reward.handle.impl;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import wtf.n1zamu.crate.CrateItem;
import wtf.n1zamu.hook.impl.PlayerPointsHook;
import wtf.n1zamu.reward.handle.RewardHandler;
import org.bukkit.entity.Player;
import wtf.n1zamu.utility.HexColorUtility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PointsRewardHandler implements RewardHandler {
    @Override
    public void reward(Player player, CrateItem prize) {
        Pattern urlPattern = Pattern.compile("(https?://\\S+)", Pattern.CASE_INSENSITIVE);

        prize.getCommand().forEach(command -> {
            if (!command.startsWith("bc;")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                return;
            }
            String message = command.substring(3)
                    .replace("%player%", player.getName());
            String coloredMessage = HexColorUtility.translateHexColorCodes(message);

            Matcher matcher = urlPattern.matcher(coloredMessage);
            TextComponent fullMessage = new TextComponent();

            int lastEnd = 0;
            while (matcher.find()) {
                if (matcher.start() > lastEnd) {
                    String beforeLink = coloredMessage.substring(lastEnd, matcher.start());
                    for (BaseComponent comp : TextComponent.fromLegacyText(beforeLink)) {
                        fullMessage.addExtra(comp);
                    }
                }

                String url = matcher.group(1);
                String linkText = url.replace("https://", "");
                BaseComponent[] linkComponents = TextComponent.fromLegacyText(linkText);

                for (BaseComponent comp : linkComponents) {
                    comp.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                    fullMessage.addExtra(comp);
                }


                lastEnd = matcher.end();
            }

            if (lastEnd < coloredMessage.length()) {
                String tail = coloredMessage.substring(lastEnd);
                for (BaseComponent comp : TextComponent.fromLegacyText(tail)) {
                    fullMessage.addExtra(comp);
                }
            }

            Bukkit.spigot().broadcast(fullMessage);
        });
        PlayerPointsHook.getAPI().give(player.getUniqueId(), fromString(prize.getTitle()));
    }

    private int fromString(String input) {
        String strippedInput = ChatColor.stripColor(input);
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(strippedInput);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return 0;
    }
}
