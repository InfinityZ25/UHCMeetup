package me.infinity.minigame.meetup.Tasks;

import me.infinity.minigame.meetup.Meetup;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class EndTask extends BukkitRunnable {

    private int close_time;
    private int time;
    private int firework_time;
    private Meetup instance;
    private Location loc;
    private Player p;

    //Settings.UseFireworksFor-Seconds

    public EndTask(Meetup instance, Location loc){
        this.instance = instance;
        close_time = this.instance.getConfig().getInt("Settings.CloseServerAfter-Seconds");
        firework_time = this.instance.getConfig().getInt("Settings.UseFireworksFor-Seconds");
        time = 0;
        this.loc = loc;
    }

    public EndTask(Meetup instance, Player p){
        this.instance = instance;
        close_time = this.instance.getConfig().getInt("Settings.CloseServerAfter-Seconds");
        firework_time = this.instance.getConfig().getInt("Settings.UseFireworksFor-Seconds");
        time = 0;
        this.p = p;
    }

    @Override
    public void run() {
        time++;
        if(close_time == time){
            for(Player all : Bukkit.getOnlinePlayers()){
                all.kickPlayer("Thanks for playing!");
            }
            instance.onDisable();
            Bukkit.shutdown();
            this.cancel();
            return;
        }
        if(time > firework_time)return;
        if(p == null){
            createFirework(loc);
        }
        else{
            createFirework(p.getLocation());
        }

    }

    private void createFirework(Location loc){
        Firework f = (Firework)loc.getWorld().spawn(loc, (Class)Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.STAR).with(FireworkEffect.Type.CREEPER).with(FireworkEffect.Type.BURST).with(FireworkEffect.Type.BALL).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.AQUA).withColor(Color.YELLOW).withColor(Color.LIME).withColor(Color.TEAL).withColor(Color.RED).withColor(Color.WHITE).build());
        fm.setPower(0);
        f.setFireworkMeta(fm);
    }
}
