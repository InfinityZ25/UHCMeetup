package me.infinity.minigame.meetup.Database.Types;


import me.infinity.minigame.meetup.Database.Core.MySQL;
import me.infinity.minigame.meetup.Database.PlayerDataInterface;
import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Utils.Player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VMySQL implements PlayerDataInterface {

    private Meetup plugin;

    public VMySQL(Meetup plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadPlayer(Player p, Boolean bol) throws SQLException, IOException {
        //loadPlayerInventory(p);
        if (Meetup.getMode.state.equals(Meetup.getMode.SOLO)) {
            loadPlayerSoloStats(p, bol);
        } else if (Meetup.getMode.state.equals(Meetup.getMode.TEAM)) {
            loadPlayerTeamStats(p);
        }
    }

    @Override
    public void savePlayer(Player p) {
        //savePlayerInventory(p);
        if (Meetup.getMode.state.equals(Meetup.getMode.SOLO)) {
            savePlayerSoloStats(p);
        } else if (Meetup.getMode.state.equals(Meetup.getMode.TEAM)) {
            savePlayerTeamStats(p);
        }
    }

    @Override
    public void updateStats(Player p) throws SQLException{
        if (!isPlayerInSoloStats(p)) {
            MySQL.update("INSERT INTO Meetup_solo_stats (UUID, Wins, Kills, Deaths, PlayedTime, MaxKills, GappsEaten) VALUES ('" + p.getUniqueId().toString() + "', '0', '0', '0', '0', '0', '0');");
        } else {
            GamePlayer gamePlayer = plugin.getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());

            ResultSet player = MySQL.query("SELECT * FROM Meetup_solo_stats WHERE UUID='" + p.getUniqueId().toString() + "'");
            if (player.next()) {
                gamePlayer.setWins(player.getInt("Wins"));
                gamePlayer.setKills(player.getInt("Kills"));
                gamePlayer.setDeaths(player.getInt("Deaths"));
                gamePlayer.setPlayedTime(player.getInt("PlayedTime"));
                gamePlayer.setMaxKills(player.getInt("MaxKills"));
                gamePlayer.setGappsEaten(player.getInt("GappsEaten"));
            }
        }
    }

    private void savePlayerSoloStats(Player p) {
        if (!isPlayerInSoloStats(p)) {
            MySQL.update("INSERT INTO Meetup_solo_stats (UUID, Wins, Kills, Deaths, PlayedTime, MaxKills, GappsEaten) VALUES ('" + p.getUniqueId().toString() + "', '0', '0', '0', '0', '0', '0');");
        } else {
            GamePlayer gamePlayer = plugin.getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());
            MySQL.update("UPDATE Meetup_solo_stats SET Wins='" + gamePlayer.getWins() + "', Kills='" + gamePlayer.getKills() + "', Deaths='" + gamePlayer.getDeaths() +  "', PlayedTime='" + gamePlayer.getPlayedTime() +  "', MaxKills='" + gamePlayer.getMaxKills()+  "', GappsEaten='" + gamePlayer.getGappsEaten() + "' WHERE UUID='" + p.getUniqueId() + "'");
        }
    }

    private void savePlayerTeamStats(Player p) {
        if (!isPlayerInTeamStats(p)) {
            MySQL.update("INSERT INTO Meetup_team_stats (UUID, Wins, Kills, Deaths) VALUES ('" + p.getUniqueId().toString() + "', '0', '0', '0');");
        } else {
            GamePlayer gamePlayer = plugin.getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());
            MySQL.update("UPDATE Meetup_team_stats SET Wins='" + gamePlayer.getWins() + "', Kills='" + gamePlayer.getKills() + "', Deaths='" + gamePlayer.getDeaths() + "' WHERE UUID='" + p.getUniqueId() + "'");
        }
    }
    @Override
    public void savePlayerInventory(Player p) {
        if (!isInPlayerHotbar(p)) {
            MySQL.update("INSERT INTO Meetup_bar (UUID, Sword, Rod, Bow, Gapples, Gheads, Water_1, Water_2, Lava_1, Lava_2, Food, Cobblestone, Wood, Axe, Pickaxe, Arrow, Cobweb, SpeedPot, FirePot, Anvil, Enchanting) VALUES ('" + p.getUniqueId().toString() + "', '0', '1', '2', '3', '5', '4', '25', '6', '7', '34', '8', '16', '9', '18', '27', '35', '17', '26', '10', '19');");
        } else {
            GamePlayer gamePlayer = plugin.getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());
            int[] hotbar = gamePlayer.getHotbar();
            MySQL.update("UPDATE Meetup_bar SET Sword='" + hotbar[0] + "', Rod='" + hotbar[1] + "', Bow='" + hotbar[2] + "', Gapples='" + hotbar[3] + "', Gheads='" + hotbar[4] + "', Water_1='" + hotbar[5] + "', Water_2='" + hotbar[6]
                    + "', Lava_1='" + hotbar[7] + "', Lava_2='" + hotbar[8] + "', Food='" + hotbar[9] + "', Cobblestone='" + hotbar[10] + "', Wood='" + hotbar[11] + "', Axe='" + hotbar[12] + "', Pickaxe='" + hotbar[13]
                    + "', Arrow='" + hotbar[14] + "', Cobweb='" + hotbar[15] +  "', SpeedPot='" + hotbar[16] + "', FirePot='" + hotbar[17] + "', Anvil='" + hotbar[18] + "', Enchanting='" + hotbar[19] +"' WHERE UUID='" + p.getUniqueId() + "'");
        }
    }


    private void loadPlayerSoloStats(Player p, Boolean bol) throws SQLException {
        try{
            if (!isPlayerInSoloStats(p)) {
                MySQL.update("INSERT INTO Meetup_solo_stats (UUID, Wins, Kills, Deaths, PlayedTime, MaxKills, GappsEaten) VALUES ('" + p.getUniqueId().toString() + "', '0', '0', '0', '0', '0', '0');");
                plugin.getPlayerManager().getUuidGamePlayerMap().put(p.getUniqueId(), new GamePlayer(p, p.getUniqueId(), bol, 0, 0, 0, 0, 0));
            } else {
                GamePlayer gamePlayer = new GamePlayer(p, p.getUniqueId(), bol, 0, 0, 0, 0, 0);

                ResultSet player = MySQL.query("SELECT * FROM Meetup_solo_stats WHERE UUID='" + p.getUniqueId().toString() + "'");
                if (player.next()) {
                    gamePlayer.setWins(player.getInt("Wins"));
                    gamePlayer.setKills(player.getInt("Kills"));
                    gamePlayer.setDeaths(player.getInt("Deaths"));
                    gamePlayer.setPlayedTime(player.getInt("PlayedTime"));
                    gamePlayer.setMaxKills(player.getInt("MaxKills"));
                    gamePlayer.setGappsEaten(player.getInt("GappsEaten"));
                }

                plugin.getPlayerManager().getUuidGamePlayerMap().put(p.getUniqueId(), gamePlayer);
                loadPlayerInventory(p);
            }
        }catch(Exception io){
            io.printStackTrace();
            Bukkit.broadcastMessage(ChatColor.RED + "An exception has occurred, please let us now this via twitter!");
            Bukkit.shutdown();
        }

    }

    private void loadPoints(Player p){
        GamePlayer gamePlayer = plugin.getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());
        if(!isPoints(p)){
            gamePlayer.setPoints(0);
            MySQL.update("INSERT INTO Meetup_team_stats (UUID, Wins, Kills, Deaths) VALUES ('" + p.getUniqueId().toString() + "', '0', '0', '0', '0');");

            return;
        }

    }

    @Override
    public void loadPlayerInventory(Player p) throws SQLException{
        try {
            if (!isInPlayerHotbar(p)) {
                MySQL.update("INSERT INTO Meetup_bar (UUID, Sword, Rod, Bow, Gapples, Gheads, Water_1, Water_2, Lava_1, Lava_2, Food, Cobblestone, Wood, Axe, Pickaxe, Arrow, Cobweb, SpeedPot, FirePot, Anvil, Enchanting) VALUES ('" + p.getUniqueId().toString() + "', '0', '1', '2', '3', '5', '4', '25', '6', '7', '34', '8', '16', '9', '18', '27', '35', '17', '26', '10', '19');");
                plugin.getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId()).setHotbar(new int[]{0, 1, 2, 3, 5, 4, 25, 6, 7, 34, 8, 16, 9, 18, 27, 35, 17, 26, 10, 19});
            } else {
                GamePlayer gamePlayer = plugin.getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());

                ResultSet player = MySQL.query("SELECT * FROM Meetup_bar WHERE UUID='" + p.getUniqueId().toString() + "'");
                if (player.next()) {
                    gamePlayer.setHotbar(new int[]{player.getInt("Sword"), player.getInt("Rod"), player.getInt("Bow"), player.getInt("Gapples"), player.getInt("Gheads"), player.getInt("Water_1")
                            , player.getInt("Water_2"), player.getInt("Lava_1"), player.getInt("Lava_2"), player.getInt("Food"), player.getInt("Cobblestone")
                            , player.getInt("Wood"), player.getInt("Axe"), player.getInt("Pickaxe"), player.getInt("Arrow"), player.getInt("Cobweb")
                            , player.getInt("SpeedPot"), player.getInt("FirePot"), player.getInt("Anvil"), player.getInt("Enchanting")});
                }
            }
        }catch(Exception io){
            io.printStackTrace();
            Bukkit.broadcastMessage(ChatColor.RED + "An exception has occurred, please let us now this via twitter!");
            Bukkit.shutdown();
        }
    }

    private void loadPlayerTeamStats(Player p) throws SQLException {
        try{
            if (!isPlayerInTeamStats(p)) {
                MySQL.update("INSERT INTO Meetup_team_stats (UUID, Wins, Kills, Deaths) VALUES ('" + p.getUniqueId().toString() + "', '0', '0', '0', '0');");
                plugin.getPlayerManager().getUuidGamePlayerMap().put(p.getUniqueId(), new GamePlayer(p, p.getUniqueId(), false, 0, 0, 0, 0,0));
            } else {
                GamePlayer gamePlayer = new GamePlayer(p, p.getUniqueId(), false, 0, 0, 0, 0, 0);

                ResultSet player = MySQL.query("SELECT * FROM Meetup_team_stats WHERE UUID='" + p.getUniqueId().toString() + "'");
                if (player.next()) gamePlayer.setWins(player.getInt("Wins"));
                if (player.next()) gamePlayer.setKills(player.getInt("Kills"));
                if (player.next()) gamePlayer.setDeaths(player.getInt("Deaths"));

                plugin.getPlayerManager().getUuidGamePlayerMap().put(p.getUniqueId(), gamePlayer);
            }}catch(Exception io){
            io.printStackTrace();
            Bukkit.broadcastMessage(ChatColor.RED + "An exception has occurred, please let us now this via twitter!");
            Bukkit.shutdown();
        }
    }



    private boolean isInPlayerHotbar(Player p) {
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM Meetup_bar WHERE UUID='" + p.getUniqueId().toString() + "'");
            return rs.next() && rs.getString("UUID") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isPoints(Player p) {
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM playerpoints WHERE playername='" + p.getUniqueId().toString() + "'");
            return rs.next() && rs.getString("playername") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private boolean isPlayerInTeamStats(Player p) {
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM Meetup_solo_stats WHERE UUID='" + p.getUniqueId().toString() + "'");
            return rs.next() && rs.getString("UUID") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isPlayerInSoloStats(Player p) {
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM Meetup_solo_stats WHERE UUID='" + p.getUniqueId().toString() + "'");
            return rs.next() && rs.getString("UUID") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}