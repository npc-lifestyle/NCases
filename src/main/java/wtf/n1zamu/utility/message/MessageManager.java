package wtf.n1zamu.utility.message;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import wtf.n1zamu.NCases;

import java.util.*;
import java.util.stream.Collectors;

public class MessageManager {
    private Map<String, List<String>> keyMessageMap;

    public MessageManager() {
        load();
    }

    public void load() {
        this.keyMessageMap = new HashMap<>();
        ConfigurationSection messageSection = NCases.getInstance().getConfig().getConfigurationSection("messages");
        if (messageSection == null) {
            return;
        }
        messageSection.getKeys(false).forEach(key -> keyMessageMap
                .put(key,
                        messageSection.getStringList(key)
                                .stream()
                                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                                .collect(Collectors.toList())));
    }

    public void sendMessage(Player player, String key) {
        List<String> message = keyMessageMap.getOrDefault(key, Collections.emptyList());
        message.forEach(player::sendMessage);
    }

    public void sendMessage(Player player, String key, Map<String, String> placeHolders) {
        List<String> message = keyMessageMap.getOrDefault(key, Collections.emptyList());
        if (message.isEmpty()) {
            return;
        }
        List<String> modifiedMessage = new ArrayList<>(message);

        for (Map.Entry<String, String> entry : placeHolders.entrySet()) {
            String key1 = entry.getKey();
            String value = entry.getValue();

            modifiedMessage = modifiedMessage.stream()
                    .map(line -> line.replace(key1, value))
                    .collect(Collectors.toList());
        }

        modifiedMessage.forEach(player::sendMessage);
    }
}
