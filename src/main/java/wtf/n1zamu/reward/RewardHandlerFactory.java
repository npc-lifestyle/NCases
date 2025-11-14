package wtf.n1zamu.reward;

import wtf.n1zamu.crate.CrateItem;
import wtf.n1zamu.crate.CrateType;
import wtf.n1zamu.reward.handle.RewardHandler;
import org.bukkit.entity.Player;
import wtf.n1zamu.reward.handle.impl.*;

import java.util.HashMap;
import java.util.Map;

public class RewardHandlerFactory {
    public static void reward(CrateType type, CrateItem prize, Player player) {
        getByType(type).reward(player, prize);
    }

    private static RewardHandler getByType(CrateType type) {
        Map<CrateType, RewardHandler> handlerMap = new HashMap<>();
        handlerMap.put(CrateType.CMD, new CmdRewardHandler());
        handlerMap.put(CrateType.POINTS, new PointsRewardHandler());
        handlerMap.put(CrateType.VAULT, new VaultRewardHandler());
        return handlerMap.getOrDefault(type, null);
    }
}
