package me.infinity.minigame.meetup.Tasks;

import javafx.concurrent.Task;
import me.infinity.minigame.meetup.Meetup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BorderTask extends BukkitRunnable {

    private int time;
    private Meetup instance;
    private Boolean ontop;

    public BorderTask (Meetup instance){
        time = -1;
        ontop = false;
        this.instance = instance;
        String message = getLang("borders.initial-message");
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendMessage(player, message);
        });
    }

    @Override
    public void run() {
        time++;
        switch(time){
            case 60:{
                String message = getLang("borders.secs-left").replace("<size>", "50").replace("<seconds>", "" + 60);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 90:{
                String message = getLang("borders.secs-left").replace("<size>", "50").replace("<seconds>", "" + 30);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 110:{
                String message = getLang("borders.secs-left").replace("<size>", "50").replace("<seconds>", "" + 10);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 115:{
                String message = getLang("borders.secs-left").replace("<size>", "50").replace("<seconds>", "" + 5);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 116:{
                String message = getLang("borders.secs-left").replace("<size>", "50").replace("<seconds>", "" + 4);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 117:{
                String message = getLang("borders.secs-left").replace("<size>", "50").replace("<seconds>", "" + 3);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 118:{
                String message = getLang("borders.secs-left").replace("<size>", "50").replace("<seconds>", "" + 2);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 119:{
                String message = getLang("borders.secs-left").replace("<size>", "50").replace("<seconds>", "" + 1);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 120:{
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        randomTp(50);
                        placeBorder(Bukkit.getWorld("game"), 50);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb game setcorners 50 -50 -50 50");
                        instance.game.getWorldBorder().setSize(102);
                    }
                }.runTask(instance);
                String message = getLang("borders.border-shrunk").replace("<size>", "50");

                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.ZOMBIE_INFECT, 10, 1);
                });



                break;
            }
            case 180:{
                String message = getLang("borders.secs-left").replace("<size>", "25").replace("<seconds>", "" + 60);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 210:{
                String message = getLang("borders.secs-left").replace("<size>", "25").replace("<seconds>", "" + 30);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 230:{
                String message = getLang("borders.secs-left").replace("<size>", "25").replace("<seconds>", "" + 10);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 235:{
                String message = getLang("borders.secs-left").replace("<size>", "25").replace("<seconds>", "" + 5);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 236:{
                String message = getLang("borders.secs-left").replace("<size>", "25").replace("<seconds>", "" + 4);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 237:{
                String message = getLang("borders.secs-left").replace("<size>", "25").replace("<seconds>", "" + 3);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 238:{
                String message = getLang("borders.secs-left").replace("<size>", "25").replace("<seconds>", "" + 2);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 239:{
                String message = getLang("borders.secs-left").replace("<size>", "25").replace("<seconds>", "" + 1);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
                });
                break;
            }
            case 240:{
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        randomTp(25);
                        placeBorder(Bukkit.getWorld("game"), 25);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb game setcorners 25 -25 -25 25");
                        instance.game.getWorldBorder().setSize(52);
                    }
                }.runTask(instance);
                String message = getLang("borders.border-shrunk").replace("<size>", "25");
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendMessage(player, message);
                    player.playSound(player.getLocation(), Sound.ZOMBIE_INFECT, 10, 1);
                });
                this.cancel();

                break;
            }

        }

    }

    private void randomTp(int size){
        Bukkit.getOnlinePlayers().forEach(all ->{
            if(isOutsideOfBorder(all.getLocation(), size)) {
                Location loc = new Location(Bukkit.getWorld("game"), 0, 0, 0);
                loc.setX(loc.getX() + Math.random() * size * 2.0 - size);
                loc.setZ(loc.getZ() + Math.random() * size * 2.0 - size);
                loc = loc.getWorld().getHighestBlockAt(loc).getLocation();
                all.teleport(loc.add(0.0, 1.5, 0.0));
            }
        });

    }

    private boolean isOutsideOfBorder(Location loc, Integer size){
        double x = loc.getX();
        double z = loc.getZ();
        return (x > size) || (-x > size) || (z > size) || (-z > size);
    }

    private String getLang(String path){
        return ChatColor.translateAlternateColorCodes('&', Meetup.getInstance().getLangFile().getString(path));
    }

    private void sendMessage(Player sender , String message){
        if(message.isEmpty())return;
        sender.sendMessage(message);
    }

    private Integer fillArea(final Location l1, final Location l2) {
        final Location min = new Location(l1.getWorld(), 0.0, 0.0, 0.0);
        final Location max = new Location(l1.getWorld(), 0.0, 0.0, 0.0);
        if (l1.getX() >= l2.getX()) {
            max.setX((double)l1.getBlockX());
            min.setX((double)l2.getBlockX());
        }
        else {
            min.setX((double)l1.getBlockX());
            max.setX((double)l2.getBlockX());
        }
        if (l1.getY() >= l2.getY()) {
            max.setY((double)l1.getBlockY());
            min.setY((double)l2.getBlockY());
        }
        else {
            min.setY((double)l1.getBlockY());
            max.setY((double)l2.getBlockY());
        }
        if (l1.getZ() >= l2.getZ()) {
            max.setZ((double)l1.getBlockZ());
            min.setZ((double)l2.getBlockZ());
        }
        else {
            min.setZ((double)l1.getBlockZ());
            max.setZ((double)l2.getBlockZ());
        }
        int delay = 0;
        for (Double x = min.getX(); x <= max.getX(); ++x) {
            final Double x2 = x;
            for (Double z = min.getZ(); z <= max.getZ(); ++z) {
                final Double z2 = z;
                final BukkitScheduler scheduler = instance.getServer().getScheduler();
                scheduler.scheduleSyncDelayedTask(instance, (Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final int maxY = min.getWorld().getHighestBlockAt((int)Math.round(x2), (int)Math.round(z2)).getY() + 4;
                        for (Double y = min.getY(); y <= max.getY(); ++y) {
                            if (ontop && y == min.getY()) {
                                y = Double.parseDouble(l1.getWorld().getHighestBlockYAt((int)Math.round(x2), (int)Math.round(z2)) + "");
                            }
                            final Location loc = new Location(l1.getWorld(), (double)x2, (double)y, (double)z2);
                            if (y > maxY) {
                                break;
                            }
                            if ((loc.getBlock().getType().equals(Material.AIR)) || !ontop) {
                                loc.getBlock().setType(Material.BEDROCK);
                            }
                        }
                    }
                }, (long)(delay / 70));
                ++delay;
            }
            ++delay;
        }
        BukkitScheduler scheduler2 = instance.getServer().getScheduler();
        scheduler2.scheduleSyncDelayedTask(instance, new Runnable() {
            @Override
            public void run() {
                System.out.println("Finished placing border");
            }
        }, (delay / 70L) + 1L);



        return delay / 70;
    }


    private  void placeBorder(World world, Integer size) {
        final List<Double> timings = new ArrayList<Double>();
        final Location north1 = new Location(world, (double)size, 0.0, (double)(-size + 1));
        final Location north2 = new Location(world, (double)size, 256.0, (double)(size - 1));
        timings.add(Double.parseDouble(fillArea(north1, north2) + ""));
        final Location east1 = new Location(world, (double)(-size), 0.0, (double)size);
        final Location east2 = new Location(world, (double)size, 256.0, (double)size);
        timings.add(Double.parseDouble(fillArea(east1, east2) + ""));
        final Location south1 = new Location(world, (double)(-size), 0.0, (double)(-size + 1));
        final Location south2 = new Location(world, (double)(-size), 256.0, (double)(size - 1));
        timings.add(Double.parseDouble(fillArea(south1, south2) + ""));
        final Location west1 = new Location(world, (double)(-size), 0.0, (double)(-size));
        final Location west2 = new Location(world, (double)size, 256.0, (double)(-size));
        timings.add(Double.parseDouble(fillArea(west1, west2) + ""));
        Collections.sort(timings);
        System.out.println("Creating border. Est. Time: " + Math.ceil(timings.get(0) * 2.0 / 20.0) + " seconds");

    }
}
