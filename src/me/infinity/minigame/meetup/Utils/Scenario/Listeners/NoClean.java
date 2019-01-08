package me.infinity.minigame.meetup.Utils.Scenario.Listeners;

import me.infinity.minigame.meetup.Meetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoClean extends ScenarioTemplate {

    private Map<Player, BukkitTask> noCleanMap = new HashMap<>();

    public NoClean(){
        Bukkit.broadcastMessage("test");
    }

    @EventHandler
    public void onDeathEvent(PlayerDeathEvent e){
        if(e.getEntity().getKiller() == null)return;
        Player killer = e.getEntity().getKiller();

        giveNoClean(killer);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        new BukkitRunnable(){
            @Override
            public void run(){
                noCleanMap.keySet().forEach( a ->{
                    Team t = e.getPlayer().getScoreboard().registerNewTeam(a.getName());
                    t.setPrefix(ChatColor.GREEN + "");
                    t.addPlayer(a);
                });

            }
        }.runTaskLater(Meetup.getInstance(), 2l);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof  Player))return;
        Player p = (Player) e.getEntity();
        if(!noCleanMap.containsKey(p)){
            if((e.getDamager() instanceof Player)){
                Player damager = (Player) e.getDamager();
                if(!noCleanMap.containsKey(damager))return;
                e.setCancelled(true);
                sendMessage(damager, getLang("scenarios.no-clean-lost"));
                removeNoClean(damager);
                return;
            }
            if((e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player)){
                Player damager = (Player) (((Projectile) e.getDamager()).getShooter());
                if(!noCleanMap.containsKey(damager))return;
                e.setCancelled(true);
                sendMessage(damager, getLang("scenarios.no-clean-lost"));
                removeNoClean(damager);
            }
            return;
        }

        e.setCancelled(true);

        if(e.getDamager() instanceof Player){
            Player damager = (Player) e.getDamager();
            sendMessage(damager, getLang("scenarios.no-clean-nodmg"));
            return;
        }
        if(e.getDamager() instanceof  Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player){
            Player damager = (Player) (((Projectile) e.getDamager()).getShooter());
            sendMessage(damager, getLang("scenarios.no-clean-nodmg"));
        }

    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player))return;
        Player p = (Player) e.getEntity();
        if(!noCleanMap.containsKey(p))return;
        if(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        e.setCancelled(true);

    }


    private void giveNoClean(Player p){
        Bukkit.getOnlinePlayers().parallelStream().forEach(all ->{
            Team t = all.getScoreboard().registerNewTeam(p.getName());
            t.setPrefix(ChatColor.RED + "");
            t.addPlayer(p);
        });
        sendMessage(p, getLang("scenarios.no-clean-gotten"));

        BukkitTask task = new BukkitRunnable(){
            int se = 21;
            @Override
            public void run(){
                if(!p.isOnline()){
                    removeNoClean(p);
                    this.cancel();
                }
                se--;
                Bukkit.getOnlinePlayers().parallelStream().forEach(all->{
                    all.getScoreboard().getTeam(p.getName()).setSuffix(" (" + se + "s)");
                });
                if(se <= 0){
                    sendMessage(p, getLang("scenarios.no-clean-lost"));
                    removeNoClean(p);
                }

            }
        }.runTaskTimerAsynchronously(Meetup.getInstance(), 0L, 20L);
        noCleanMap.put(p, task);

    }

    private void removeNoClean(Player p){
        Bukkit.getOnlinePlayers().parallelStream().forEach(all ->{
            all.getScoreboard().getTeam(p.getName()).removePlayer(p);
            all.getScoreboard().getTeam(p.getName()).unregister();

        });
        noCleanMap.get(p).cancel();
        noCleanMap.remove(p);

    }



}
