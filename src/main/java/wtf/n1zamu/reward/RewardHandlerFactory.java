package wtf.n1zamu.reward;

import wtf.n1zamu.crate.CrateItem;
import wtf.n1zamu.crate.CrateType;
import wtf.n1zamu.reward.handle.RewardHandler;
import org.bukkit.entity.Player;
import wtf.n1zamu.reward.handle.impl.*;

import java.util.EnumMap;
import java.util.Map;

public class RewardHandlerFactory {

    private static final Map<CrateType, RewardHandler> handlers = new EnumMap<>(CrateType.class);

    static {
        handlers.put(CrateType.CMD, new CmdRewardHandler());
        handlers.put(CrateType.POINTS, new PointsRewardHandler());
        handlers.put(CrateType.VAULT, new VaultRewardHandler());
    }

    public static void reward(CrateType type, CrateItem prize, Player player) {
        RewardHandler handler = handlers.get(type);
        if (handler != null) handler.reward(player, prize);
    }
}
