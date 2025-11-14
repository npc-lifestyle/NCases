package wtf.n1zamu.utility;

import org.bukkit.Bukkit;

public class NMSUtil {

    private static final int major;
    private static final int minor;
    private static final int revision;

    static {
        String versionPackage = Bukkit.getServer().getClass().getPackage().getName();
        String version = versionPackage.substring(versionPackage.lastIndexOf('.') + 1);

        String[] parts = version.split("_");

        int tmpMajor = 0;
        int tmpMinor = 0;
        int tmpRevision = 0;

        try {
            tmpMajor = Integer.parseInt(parts[0].replace("v", ""));
            tmpMinor = Integer.parseInt(parts[1]);
            tmpRevision = Integer.parseInt(parts[2].replace("R", ""));
        } catch (Exception e) {
            Bukkit.getLogger().warning("[NMSUtil] Invalid NMS version format: " + version);
        }

        major = tmpMajor;
        minor = tmpMinor;
        revision = tmpRevision;
    }

    public static boolean isAtLeastVersion(int maj, int min, int rev) {
        if (major != maj) return major > maj;
        if (minor != min) return minor > min;
        return revision >= rev;
    }

    public static boolean isBelowVersion(int maj, int min, int rev) {
        return !isAtLeastVersion(maj, min, rev);
    }
}
