package wtf.n1zamu.reward.handle;

import wtf.n1zamu.crate.CrateItem;
import org.bukkit.entity.Player;

public interface RewardHandler {
    void reward(Player player, CrateItem prize);
}
