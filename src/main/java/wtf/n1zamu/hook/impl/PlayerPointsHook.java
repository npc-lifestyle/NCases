package wtf.n1zamu.hook.impl;

import wtf.n1zamu.hook.PluginHook;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;

public class PlayerPointsHook extends PluginHook {
    private static PlayerPointsAPI ppAPI;

    @Override
    public String getName() {
        return "PlayerPoints";
    }

    @Override
    protected boolean setup() {
        ppAPI = PlayerPoints.getInstance().getAPI();
        return ppAPI != null;
    }

    public static PlayerPointsAPI getAPI() {
        return ppAPI;
    }
}
