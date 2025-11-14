package wtf.n1zamu.utility.animation;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;

public class AnimationInfo {
    private Sound rotateSound;
    private Sound endSound;
    private double radius;
    private int rotateTime;
    private double itemSpeed;
    private double particleSpeed;
    private int itemsOnRound;

    public AnimationInfo(
            Sound rotateSound, Sound endSound, double radius, int rotateTime, double itemSpeed, double particleSpeed, int itemsOnRound
    ) {
        this.rotateSound = rotateSound;
        this.endSound = endSound;
        this.radius = radius;
        this.rotateTime = rotateTime;
        this.itemSpeed = itemSpeed;
        this.particleSpeed = particleSpeed;
        this.itemsOnRound = itemsOnRound;
    }

    public Sound getRotateSound() {
        return this.rotateSound;
    }

    public Sound getEndSound() {
        return this.endSound;
    }

    public double getRadius() {
        return this.radius;
    }

    public int getRotateTime() {
        return this.rotateTime;
    }

    public double getItemSpeed() {
        return this.itemSpeed;
    }

    public double getParticleSpeed() {
        return this.particleSpeed;
    }

    public int getItemsOnRound() {
        return this.itemsOnRound;
    }

    public static AnimationInfo deserialize(ConfigurationSection section) {
        return new AnimationInfo(
                Sound.valueOf(section.getString("sound")),
                Sound.valueOf(section.getString("endSound")),
                section.getDouble("radius"),
                section.getInt("rotateTime"),
                section.getDouble("itemsSpeed"),
                section.getDouble("particleSpeed"),
                section.getInt("itemsOnRound")
        );
    }

    @Override
    public String toString() {
        return "AnimationInfo{" +
                "rotateSound=" + rotateSound +
                ", endSound=" + endSound +
                ", radius=" + radius +
                ", rotateTime=" + rotateTime +
                ", itemSpeed=" + itemSpeed +
                ", particleSpeed=" + particleSpeed +
                ", itemsOnRound=" + itemsOnRound +
                '}';
    }
}
