package wtf.n1zamu.utility;


import org.bukkit.Bukkit;

public class NMSUtil {
    private static final String cachedVersion;

    static {
        String string = Bukkit.getServer().getClass().getPackage().getName();
        cachedVersion = string.substring(string.lastIndexOf(46) + 1);
    }

    public static boolean isAtLeastVersion(int n, int n2, int n3) {
        String[] stringArray = cachedVersion.split("_");
        int n4 = Integer.parseInt(stringArray[0].toLowerCase().replace("v", ""));
        int n5 = Integer.parseInt(stringArray[1]);
        int n6 = Integer.parseInt(stringArray[2].toLowerCase().replace("r", ""));
        if (n4 > n) {
            return true;
        }
        if (n4 < n) {
            return false;
        }
        if (n5 > n2) {
            return true;
        }
        if (n5 < n2) {
            return false;
        }
        return n6 >= n3;
    }

    public static boolean isBelowVersion(int n, int n2, int n3) {
        return !NMSUtil.isAtLeastVersion(n, n2, n3);
    }

}
