package me.infinity.minigame.meetup.Tasks;

import me.infinity.minigame.meetup.EventsManager.FallDamageProtector;
import me.infinity.minigame.meetup.Meetup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameplayTask extends BukkitRunnable {

    private Meetup instance;
    private Integer time;
    private FallDamageProtector fallDamageProtector;

    public GameplayTask(Meetup instance){
        this.instance = instance;
        time = 0;

        new DataWatcher(this.instance, this).runTaskTimerAsynchronously(this.instance, 20 * 5L, 10L);
        new BorderTask(this.instance).runTaskTimerAsynchronously(this.instance, 0L, 20L);
        fallDamageProtector = new FallDamageProtector();
        instance.registerListener(fallDamageProtector);
    }
    @Override
    public void run() {
        time++;
        for(Player all : Bukkit.getOnlinePlayers()){
            all.getScoreboard().getTeam("gameTimer").setSuffix(getTime(time));
        }

        if(time == 10){
            instance.unregisterListener(fallDamageProtector);
        }
        //if(time != 60 || time != 90|| time != 110 || time != 180 || time != 210 || time != 230)return;

    }

    public String getTime(Integer i){
        int mins = i % 3600 / 60;
        int second = i % 60;

        return String.format("%02d:%02d", mins, second);
    }


}
