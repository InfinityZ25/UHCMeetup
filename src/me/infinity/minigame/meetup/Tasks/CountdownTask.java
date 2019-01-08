package me.infinity.minigame.meetup.Tasks;

import me.infinity.minigame.meetup.EventsManager.GameEvents;
import me.infinity.minigame.meetup.EventsManager.SpectatorEvents;
import me.infinity.minigame.meetup.EventsManager.StartingEvents;
import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Utils.Player.GamePlayer;
import me.infinity.minigame.meetup.Utils.Scenario.Scenario;
import me.infinity.minigame.meetup.Utils.Scoreboard.ScoreboardBuilder;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

public class CountdownTask extends BukkitRunnable {

    public CountdownTask (Meetup instance){
        this.instance = instance;
        message = instance.getLangFile().getString("starting-in").replace("&", ChatColor.COLOR_CHAR + "");
        sitted = new HashMap<>();
        time = 31;
        Bukkit.getOnlinePlayers().forEach(all ->{
            all.playSound(all.getLocation(), Sound.CLICK,10,1 );
            instance.getLobbyEvents().cleanPlayer(all);
            instance.getLobbyEvents().giveLobbyItems(all, false);
        });
    }
    private Meetup instance;
    private int time;
    private String message;
    private StartingEvents startingEvents;
    private Map<Player, Integer> sitted;
    @Override
    public void run() {
        time--;
        instance.getScoreboardManager().getLobbyScoreboard().getTeam("SecondsLeft").setSuffix(time + "s");

        if(time == 20){
            Bukkit.broadcastMessage(message.replace("<xseconds>", "20").replace("<sec_or_secs>", "seconds"));
            Bukkit.getOnlinePlayers().forEach(all ->{
                all.playSound(all.getLocation(), Sound.CLICK,10,1 );
            });

        }
        else if(time == 12){
            Meetup.getStates.state = Meetup.getStates.STARTING;
        }
        else if(time == 10){
            startingEvents = new StartingEvents();

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', instance.getLangFile().getString("scenarios.most-voted-scenario")).replace("<Scenario>", instance.getScenarioManager().getMostVoted().getName()));
            instance.registerListener(startingEvents);
            instance.getScenarioManager().t.cancel();
            //instance.getScenarioManager().t.cancel();
            Bukkit.broadcastMessage(message.replace("<xseconds>", "10").replace("<sec_or_secs>", "seconds"));
            //Teleport and set TO STARTING
            Meetup.getStates.state = Meetup.getStates.STARTING;

            instance.getLobbyEvents().removeEditors();
            instance.unregisterListener(instance.getLobbyEvents());
            instance.registerListener(new GameEvents());
            instance.registerListener(new SpectatorEvents());
            boolean rodless = instance.getScenarioManager().getMostVoted().getName().equalsIgnoreCase("Rodless");
            boolean bowless = instance.getScenarioManager().getMostVoted().getName().equalsIgnoreCase("Bowless");

            Bukkit.getOnlinePlayers().forEach(all ->{

                Location loc  = instance.scatterLocations.get(0);
                all.teleport(loc);

                sit(all);
                all.setAllowFlight(true);

                all.playSound(all.getLocation(), Sound.LEVEL_UP,10,1 );

                instance.getKitManager().setKit(all, instance.getKitManager().getKits().get(0), false, rodless, bowless);
                instance.scatterLocations.remove(0);
                instance.getKitManager().getKits().remove(0);

            });
            instance.scatterLocations.clear();
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', instance.getLangFile().getString("players-scattered")));

        }
        else if (time <= 5){
            if(time == 0){
                //Start the game
                Meetup.getStates.state = Meetup.getStates.INGAME;
                long time = System.currentTimeMillis();
                instance.unregisterListener(startingEvents);
                Scenario mostVoted = instance.getScenarioManager().getMostVoted();
                if(mostVoted != null) {
                    instance.registerListener(mostVoted.getListener());
                }
                Bukkit.getOnlinePlayers().forEach(all ->{
                    GamePlayer gp = instance.getPlayerManager().getUuidGamePlayerMap().get(all.getUniqueId());
                    instance.getPlayerManager().playersAlive.add(gp);
                    unsit(all);
                    gp.setStartingTime(time);
                    all.playSound(all.getLocation(), Sound.CLICK,10,1 );
                });

                for(Player all : Bukkit.getOnlinePlayers()){
                    all.setAllowFlight(false);
                    all.setFlying(false);
                    Scoreboard sb = ScoreboardBuilder.scoreboardBuilder("Scoreboards.ingame-scoreboard");
                    all.setScoreboard(sb);
                }
                Bukkit.getOnlinePlayers().forEach( a->{
                    Bukkit.getOnlinePlayers().forEach( b ->{
                        a.getScoreboard().getObjective("tab-health").getScore(b).setScore((int)b.getHealth());
                        a.getScoreboard().getObjective("name-health").getScore(b).setScore((int)b.getHealth());
                    });
                });

                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', instance.getLangFile().getString("game-begun")));
                new GameplayTask(instance).runTaskTimer(instance, 20L, 20L);
                this.cancel();
            }
            else{
                Bukkit.getOnlinePlayers().forEach(all ->{
                    all.playSound(all.getLocation(), Sound.NOTE_PLING,10,1 );
                });
                Bukkit.broadcastMessage((message.replace("<xseconds>", time + "").replace("<sec_or_secs>", "seconds")));
                if(time < -1){
                    Bukkit.broadcastMessage("A crash has occured, please report this via twitter");
                    Bukkit.shutdown();
                }
            }
        }

    }


    private  void sit(Player p) {
        Location l = p.getLocation();
        EntityHorse horse = new EntityHorse(((CraftWorld) l.getWorld()).getHandle());

        EntityBat pig = new EntityBat(((CraftWorld) l.getWorld()).getHandle());

        pig.setLocation(l.getX(), l.getY() + 0.5, l.getZ(), 0, 0);
        pig.setInvisible(true);
        pig.setHealth(6);

        horse.setLocation(l.getX(), l.getY() + 0.5, l.getZ(), 0, 0);
        horse.setInvisible(true);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(pig);


        sitted.put(p, pig.getId());

        PacketPlayOutAttachEntity sit = new PacketPlayOutAttachEntity(0, ((CraftPlayer) p).getHandle(), pig);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(sit);
    }

    private  void unsit(Player p) {
        if (sitted.get(p) != null) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(sitted.get(p));
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            sitted.remove(p);

        }
    }
}
