package me.infinity.minigame.meetup.Tasks;

import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Utils.Player.GamePlayer;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class DataWatcher extends BukkitRunnable {

    public DataWatcher (Meetup instance, GameplayTask gameplayTask){
        this.instance = instance;
        this.gameplayTask = gameplayTask;
        this.win = false;
        this.alive = instance.getPlayerManager().playersAlive;
    }

    private GameplayTask gameplayTask;
    private Meetup instance;
    private Boolean win;
    private List<GamePlayer> alive;

    @Override
    public void run() {
        if(Bukkit.getOnlinePlayers().size() == 0)Bukkit.shutdown();
        if(!win) {
            if (alive.size() <= 1) {
                Meetup.getStates.state = Meetup.getStates.FINISHED;
                if (!alive.isEmpty()) {

                    GamePlayer gp = alive.get(0);
                    gp.addWins(1);
                    givePoints(gp.getPlayer(), 5);
                    
                    int time = (int)((System.currentTimeMillis() - gp.getStartingTime()) / 1000);
                    gp.addPlayedTime(time);
                    gp.addKills(gp.getLKills());
                    instance.getInter().savePlayer(gp.getPlayer());
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', instance.getLangFile().getString("player-won").replace("<Player>", gp.getPlayer().getName())));
                    new EndTask(instance, gp.getPlayer()).runTaskTimer(instance, 0L, 20L);
                } else {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', instance.getLangFile().getString("no-winner")));
                    Location loc = new Location(instance.game, 0, instance.game.getHighestBlockYAt(0,0)+1, 0);
                    new EndTask(instance, loc).runTaskTimer(instance, 0L, 20L);
                }
                this.win = true;
                gameplayTask.cancel();

            }

        }
    }

    private void sendMessage(Player sender , String message){
        if(message.isEmpty())return;
        sender.sendMessage(message);
    }

    private void givePoints(Player p, int i){

        PlayerPoints.getAPI().give(p.getUniqueId(), getPoints(p, i));
        sendMessage(p, net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&',getLang("points-recieved").replace("<Points>", i + "")));
    }
    private int getPoints(Player p, int i){
        if(p.hasPermission("noobsters.4"))return i *4;
        if(p.hasPermission("noobsters.3"))return i *3;
        if(p.hasPermission("noobsters.2"))return i *2;
        else return i;
    }

    private String getLang(String path){
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', Meetup.getInstance().getLangFile().getString(path));
    }


}
