package wtf.n1zamu.utility.animation.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.utils.items.HologramItem;
import org.apache.commons.lang.RandomStringUtils;

import wtf.n1zamu.NCases;
import wtf.n1zamu.crate.Crate;
import wtf.n1zamu.crate.CrateItem;

import wtf.n1zamu.reward.RewardHandlerFactory;
import wtf.n1zamu.utility.animation.AnimationInfo;
import wtf.n1zamu.utility.animation.Animation;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class RotatingCircleAnimation implements Animation {
    private Crate donateCase;
    private Player player;
    private Location chest;
    private Location soundLocation;
    private List<Hologram> hologramList;
    private int time;
    private int endTime;
    private int offset;
    private int tick;
    private boolean cancelRotation;
    private Hologram winHologram;
    private Location winLocation;
    private List<CrateItem> parsedItems;
    private CrateItem prize;
    private AnimationInfo animation;
    private BukkitTask spinTask;

    public RotatingCircleAnimation(Crate donateCase, Location chestLocation, Player player) {
        this.animation = donateCase.getAnimationInfo();
        this.donateCase = donateCase;
        this.player = player;
        this.chest = chestLocation.clone().add(0.5, 0.0, 0.5);
        this.soundLocation = this.chest.clone().add(0.5, 0.0, 0.5);
        this.hologramList = new ArrayList<>();
        this.time = 0;
        this.endTime = -1;
        this.offset = 0;
        this.tick = 0;
        this.cancelRotation = false;
        this.winLocation = this.chest.clone().add(0.0, animation.getRadius() + 1.0, 0.0);
        this.parsedItems = donateCase.getItems();
        this.parsedItems = animation.getItemsOnRound() <= this.parsedItems.size() ?
                this.parsedItems : this.parsedItems.stream().limit(animation.getItemsOnRound()).collect(Collectors.toList());
        this.prize = donateCase.getCaseItemRandomizator().getRandomValue();
        NCases.getInstance().getAnimations().add(this);
    }

    public void run() {
        if (NCases.getInstance().getConfig().getBoolean("hideHolograms")) {
            NCases.getInstance().getHologramManager().getCrateHologramMap().get(donateCase.getTitle()).disable();
        }
        this.spinTask = Bukkit.getScheduler().runTaskTimer(NCases.getInstance(), this::lambdaTask, 1L, 1L);
    }

    @Override
    public void stop() {
        if (spinTask != null && !spinTask.isCancelled()) {
            spinTask.cancel();
        }
        if (Bukkit.getPluginManager().getPlugin("NCases") == null || !Bukkit.getPluginManager().isPluginEnabled("NCases")) {
            clearHolograms();
            return;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(NCases.getInstance(), () -> {
            clearHolograms();
            donateCase.setState(false);
            NCases.getInstance()
                    .getHologramManager()
                    .getCrateHologramMap()
                    .get(donateCase.getTitle())
                    .enable();

            NCases.getInstance().getAnimations().remove(this);
        }, 60L);
    }

    private void clearHolograms() {
        this.hologramList.forEach(Hologram::delete);
        this.hologramList.clear();
    }

    private void lambdaTask() {
        if (this.tick == 5) {
            if (!this.cancelRotation) {
                this.soundLocation.getWorld().playSound(this.soundLocation, this.animation.getRotateSound(), 1.0F, 1.0F);
            }

            this.tick = 0;
        }

        if (this.endTime == -2) {
            ++this.endTime;
        }

        if (!this.cancelRotation && this.hologramList.size() < this.parsedItems.size() && Math.toRadians(this.offset) >= Math.PI * 2 / (double) this.parsedItems.size() * (double) this.hologramList.size()) {
            String name = ChatColor.translateAlternateColorCodes('&', this.parsedItems.get(this.hologramList.size()).getTitle());
            Hologram as2 = DHAPI.createHologram(
                    RandomStringUtils.randomAlphanumeric(10),
                    this.chest.clone().add(0.0, 0.5, 0.0),
                    Arrays.asList(
                            name,
                            "#ICON:" + HologramItem.fromItemStack(this.parsedItems.get(this.hologramList.size()).fromCrateItem()).getContent()
                    )
            );
            if (this.parsedItems.get(this.hologramList.size()).equals(this.prize)) {
                this.winHologram = as2;
            }

            this.hologramList.add(as2);
            return;
        }

        if (!this.cancelRotation) {
            int asNum = 0;
            double cO = Math.toRadians((double) this.offset % 360.0);

            for (double d2 = 0.0; d2 < Math.PI * 2 && asNum < this.hologramList.size(); d2 += Math.PI * 2 / (double) this.parsedItems.size()) {
                double a2 = d2 + cO;
                double cos2 = Math.cos(a2);
                double sin2 = Math.sin(a2);
                Hologram as3 = this.hologramList.get(asNum++);
                Location teleport = this.chest.clone();
                if (this.donateCase.getDirection().equalsIgnoreCase("x")) {
                    teleport.add(0.0, sin2 * this.animation.getRadius() + 1.0, cos2 * this.animation.getRadius());
                } else {
                    teleport.add(cos2 * this.animation.getRadius(), sin2 * this.animation.getRadius() + 1.0, 0.0);
                }

                moveHologram(as3, teleport);
            }

            double m = 1.0;
            if (this.hologramList.size() == this.parsedItems.size()) {
                if (this.endTime == -1) {
                    this.endTime = this.time + this.animation.getRotateTime() * 20;
                }

                if (this.time >= this.endTime) {
                    if (this.winHologram.getLocation().clone().distance(this.winLocation) <= 0.05) {
                        this.cancelRotation = true;
                        moveHologram(this.winHologram, this.winLocation);
                        this.soundLocation.getWorld().playSound(this.soundLocation, this.animation.getEndSound(), 1.0F, 1.0F);

                        RewardHandlerFactory.reward(donateCase.getType(), prize, player);

                        stop();
                        return;
                    }

                    this.endTime = this.time + 1;
                }

                int remaining = this.endTime - this.time;
                m = 0.2 + (double) remaining / 20.0 / 10.0;
            }

            this.offset = (int) ((double) this.offset + this.animation.getItemSpeed() * m);
            ++this.time;
        }

        ++this.tick;
    }

    private void moveHologram(Hologram hologram, Location location) {
        Location hologramLocation = hologram.getLocation();
        hologramLocation.setWorld(location.getWorld());
        hologramLocation.setX(location.getX());
        hologramLocation.setY(location.getY());
        hologramLocation.setZ(location.getZ());
        hologram.setLocation(hologramLocation);
        hologram.realignLines();
    }
}
