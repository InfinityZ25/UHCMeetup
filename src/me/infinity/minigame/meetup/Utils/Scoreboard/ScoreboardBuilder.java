package me.infinity.minigame.meetup.Utils.Scoreboard;

import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Utils.ConfigFile;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.List;

public class ScoreboardBuilder {
    public static Integer startingTime = 60;


    public static Scoreboard scoreboardBuilder(String path){
        //Get the config file for recursive purposes
        ConfigFile sbConfig = Meetup.getInstance().scoreboardFile;
        //Create the scoreboard itself
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        //Create the sidebar objective
        Objective sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName(ChatColor.translateAlternateColorCodes('&', sbConfig.getString(path + ".title")));
        //Test if tab health should be registered
        if(sbConfig.getBoolean(path + ".use-tab-health")){
            //Create the objective and display it on list
            Objective tabHealth = scoreboard.registerNewObjective("tab-health", "health");
            tabHealth.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }
        //Test if bellow name health should be registered
        if(sbConfig.getBoolean(path + ".use-name-health")){
            //Create the objective and display it on list
            Objective bellowName = scoreboard.registerNewObjective("name-health", "health");
            bellowName.setDisplayName(ChatColor.DARK_RED + "❤");
            bellowName.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
        //Take care of all the sidebar lines
        List<String> list = Meetup.getInstance().scoreboardFile.getStringList(path + ".lines");
        //Get how many lines are there to organize it and give right numbers
        int size = list.size();
        for(String c : list){

            String s = ChatColor.translateAlternateColorCodes('&', c);

            if(s.contains("<online>")){
                Team t = scoreboard.registerNewTeam("OnlinePlayers");
                s= s.replace("<online>", "");
                t.setPrefix(s.substring(0, s.length()/2));
                t.addEntry(s.substring(s.length()/2, s.length()));
                t.setSuffix(Bukkit.getOnlinePlayers().size() + "");
                sidebar.getScore(s.substring(s.length()/2, s.length())).setScore(size);
            }
            else if(s.contains("<animated_dots>")) {
                Team t = scoreboard.registerNewTeam("DotAnimation");
                s = s.replace("<animated_dots>", "");
                t.setPrefix(s.substring(0, s.length() / 2));
                t.addEntry(s.substring(s.length() / 2, s.length()));
                t.setSuffix("");
                sidebar.getScore(s.substring(s.length() / 2, s.length())).setScore(size);
            }
            else if(s.contains("<secondsToStart>")){
                Team t = scoreboard.registerNewTeam("SecondsLeft");
                s= s.replace("<secondsToStart>", "");
                t.setPrefix(s.substring(0, s.length()/2));
                t.addEntry(s.substring(s.length()/2, s.length()));
                t.setSuffix("30s");
                sidebar.getScore(s.substring(s.length()/2, s.length())).setScore(size);
            }
            else if(s.contains("<playersAlive>")){
                Team t = scoreboard.registerNewTeam("playersAlive");
                s= s.replace("<playersAlive>", "");
                t.setPrefix(s.substring(0, s.length()/2));
                t.addEntry(s.substring(s.length()/2, s.length()));
                t.setSuffix(Meetup.getInstance().getPlayerManager().playersAlive() + "/" + 24);
                sidebar.getScore(s.substring(s.length()/2, s.length())).setScore(size);
            }
            else if(s.contains("<gameTimer>")){
                Team t = scoreboard.registerNewTeam("gameTimer");
                s= s.replace("<gameTimer>", "");
                t.setPrefix(s.substring(0, s.length()/2));
                t.addEntry(s.substring(s.length()/2, s.length()));
                t.setSuffix(Meetup.getInstance().getScoreboardManager().getTime(0));
                sidebar.getScore(s.substring(s.length()/2, s.length())).setScore(size);
            }
            else if(s.contains("<playerKills>")){
                Team t = scoreboard.registerNewTeam("TeamSoloK");
                s= s.replace("<playerKills>", "");
                t.setPrefix(s.substring(0, s.length()/2));
                t.addEntry(s.substring(s.length()/2, s.length()));
                t.setSuffix("0");
                sidebar.getScore(s.substring(s.length()/2, s.length())).setScore(size);
            }
            else if(s.contains("<scenario>")){
                Team t = scoreboard.registerNewTeam("scenarioTeam");
                s= s.replace("<scenario>", "");
                t.setPrefix(s.substring(0, s.length()/2));
                t.addEntry(s.substring(s.length()/2, s.length()));
                t.setSuffix(Meetup.getInstance().getScenarioManager().getMostVoted().getName());
                sidebar.getScore(s.substring(s.length()/2, s.length())).setScore(size);
            }
            else {
                Team t = scoreboard.registerNewTeam("TeamLine" + size);
                if(s.length() >= 16) {
                    t.setPrefix(s.substring(0, s.length()/3));
                    t.addEntry(s.substring(s.length()/3, (s.length()/3 ) * 2));
                    t.setSuffix(s.substring((s.length()/3 ) * 2, s.length()));
                    sidebar.getScore(s.substring(s.length()/3, (s.length()/3 ) * 2)).setScore(size);
                }
                else {
                    t.addEntry(s);
                    sidebar.getScore(s).setScore(size);
                }
            }

            size--;

        }


        return scoreboard;
    }
}
