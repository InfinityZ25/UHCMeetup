package me.infinity.minigame.meetup.Utils.Scoreboard;

import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Tasks.CountdownTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager {
    private Scoreboard lobbyScoreboard;
    private Scoreboard editScoreboard;
    private BukkitRunnable scoreboardAnimation;
    private BukkitRunnable startingCountdown;
    public Integer gameTime;


    public ScoreboardManager(Meetup instance){
        //Scoreboards
        editScoreboard = ScoreboardBuilder.scoreboardBuilder("Scoreboards.edit-kit-scoreboard");
        lobbyScoreboard = ScoreboardBuilder.scoreboardBuilder("Scoreboards.waiting-scoreboard");
        gameTime = 0;

        //Animation
        scoreboardAnimation = new BukkitRunnable(){
            private int state = 0;
            private Team team = lobbyScoreboard.getTeam("DotAnimation");

            @Override
            public void run(){
                if(team!=null) {
                    switch (state) {
                        case 0: {
                            state = 1;
                            team.setSuffix(".");
                            break;
                        }
                        case 1: {
                            state = 2;
                            team.setSuffix("..");
                            break;
                        }
                        case 2: {
                            state = 0;
                            team.setSuffix("...");

                            break;
                        }
                    }
                }

            }
        };
        scoreboardAnimation.runTaskTimerAsynchronously(instance, 0L, 10L);
    }

    public Scoreboard getLobbyScoreboard() {
        return lobbyScoreboard;
    }

    public Scoreboard getEditScoreboard() {
        return editScoreboard;
    }

    public void updateOnlinePlayers(){
        lobbyScoreboard.getTeam("OnlinePlayers").setSuffix(Bukkit.getOnlinePlayers().size() + "");
    }

    private void startDotAnimation(Meetup instance){
        //Animation
        scoreboardAnimation = new BukkitRunnable(){
            private int state = 0;
            private Team team = lobbyScoreboard.getTeam("DotAnimation");

            @Override
            public void run(){
                if(team!=null) {
                    switch (state) {
                        case 0: {
                            state = 1;
                            team.setSuffix(".");
                            break;
                        }
                        case 1: {
                            state = 2;
                            team.setSuffix("..");
                            break;
                        }
                        case 2: {
                            state = 0;
                            team.setSuffix("...");

                            break;
                        }
                    }
                }

            }
        };
        scoreboardAnimation.runTaskTimerAsynchronously(instance, 0L, 10L);
    }

    public void startCountdown(){
        lobbyScoreboard = ScoreboardBuilder.scoreboardBuilder("Scoreboards.starting-scoreboard");
        scoreboardAnimation.cancel();
        for(Player all : Bukkit.getOnlinePlayers()){
            all.setScoreboard(lobbyScoreboard);
        }
        Meetup instance = Meetup.getInstance();
        startingCountdown = new CountdownTask(instance);
        startingCountdown.runTaskTimer(instance, 0L, 20L);


    }

    public void updatePlayersAlive(){
        for(Player p : Bukkit.getOnlinePlayers())
        p.getScoreboard().getTeam("playersAlive").setSuffix(Meetup.getInstance().getPlayerManager().playersAlive.size() + "/24");
    }

    public String getTime(Integer i){
        int mins = i % 3600 / 60;
        int second = i % 60;

        return String.format("%02d:%02d", mins, second);
    }
}
