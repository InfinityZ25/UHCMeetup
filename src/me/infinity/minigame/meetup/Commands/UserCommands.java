package me.infinity.minigame.meetup.Commands;

import me.infinity.minigame.meetup.Database.Core.MySQL;
import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Tasks.CountdownTask;
import me.infinity.minigame.meetup.Utils.Kit.Kit;
import me.infinity.minigame.meetup.Utils.MessageSenderUtil;
import me.infinity.minigame.meetup.Utils.Player.GamePlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserCommands extends MessageSenderUtil implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        switch(cmd.getName().toLowerCase()){
            case "kit":{
                if(!(sender instanceof Player)){
                    sendMessage(sender, getLang("console-cannot-use"));
                    return  true;
                }
                if(args.length == 0){
                    sendMessage(sender, getLang("kit-command-help"));
                    return true;
                }

                Player p = (Player)sender;

                switch(args[0].toLowerCase()){
                    case"edit":{
                        if(Meetup.getStates.state != Meetup.getStates.LOBBY)return true;
                        editKit(p);
                        break;
                    }
                    case"save":{
                        if(Meetup.getStates.state != Meetup.getStates.LOBBY && Meetup.getStates.state != Meetup.getStates.COUNTDOWN)return true;
                        saveKit(p);
                        if(!Meetup.getInstance().started){
                            Meetup.getInstance().getLobbyEvents().giveLobbyItems(p, true);
                        }
                        else{
                            Meetup.getInstance().getLobbyEvents().giveLobbyItems(p, false);
                        }
                        break;
                    }
                    default:{
                        sendMessage(sender, getLang("kit-command-help"));
                        break;
                    }
                }
                break;
            }
            case "editkit":{
                if(!(sender instanceof Player)){
                    sendMessage(sender, getLang("console-cannot-use"));


                    return  true;
                }
                if(Meetup.getStates.state != Meetup.getStates.LOBBY)return true;
                Player p = (Player)sender;
                editKit(p);
                break;
            }
            case "savekit":{
                if(!(sender instanceof Player)){
                    sendMessage(sender, getLang("console-cannot-use"));
                    return  true;
                }
                if(Meetup.getStates.state != Meetup.getStates.LOBBY && Meetup.getStates.state != Meetup.getStates.COUNTDOWN)return true;
                Player p = (Player)sender;
                saveKit(p);

                if(!Meetup.getInstance().started){
                    Meetup.getInstance().getLobbyEvents().giveLobbyItems(p, true);
                }
                else{
                    Meetup.getInstance().getLobbyEvents().giveLobbyItems(p, false);
                }
                break;
            }
            case "stats":{
                if(!(sender instanceof Player)){
                    sendMessage(sender, getLang("console-cannot-use"));
                    return  true;
                }
                Player p = (Player)sender;

                if(args.length == 0){
                    GamePlayer gamePlayer = Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());
                    sendMessage(p, getLang("stats-getting-own").replace("<Player>", p.getName()));
                    try {
                        Meetup.getInstance().getInter().updateStats(p);
                        Inventory inv = statsInventory(p.getName(), gamePlayer.getWins(), gamePlayer.getKills(), gamePlayer.getDeaths(), gamePlayer.getPlayedTime(), gamePlayer.getMaxKills(), gamePlayer.getGappsEaten());
                        p.openInventory(inv);
                        sendMessage(p, getLang("stats-loaded-own").replace("<Player>", p.getName()));
                    }catch(SQLException I){
                        I.printStackTrace();
                    }
                    return true;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null){
                    OfflinePlayer of = Bukkit.getOfflinePlayer(args[0]);
                    if(isPlayerInSoloStats(of.getUniqueId())){
                        sendMessage(p, getLang("stats-getting-elses").replace("<Player>", of.getName()));
                        try{
                            ResultSet player = MySQL.query("SELECT * FROM Meetup_solo_stats WHERE UUID='" + of.getUniqueId().toString() + "'");
                            if (player.next()){
                                Inventory inv = statsInventory(of.getName(), player.getInt("Wins"), player.getInt("Kills"), player.getInt("Deaths"), player.getInt("PlayedTime"), player.getInt("MaxKills"), player.getInt("GappsEaten"));
                                p.openInventory(inv);
                                sendMessage(p, getLang("stats-loaded-elses").replace("<Player>", of.getName()));
                            }
                        }catch(SQLException ie){
                            ie.printStackTrace();
                        }
                        return true;
                    }
                    sendMessage(p, getLang("player-null"));
                    return true;
                }
                if(Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().containsKey(p.getUniqueId())){
                    GamePlayer targetGameplayer = Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(target.getUniqueId());
                    sendMessage(p, getLang("stats-getting-elses").replace("<Player>", target.getName()));
                    try {
                        Meetup.getInstance().getInter().updateStats(target);
                        Inventory inv = statsInventory(target.getName(), targetGameplayer.getWins(), targetGameplayer.getKills(), targetGameplayer.getDeaths(), targetGameplayer.getPlayedTime(), targetGameplayer.getMaxKills(), targetGameplayer.getGappsEaten());
                        p.openInventory(inv);
                        sendMessage(p, getLang("stats-loaded-elses").replace("<Player>", target.getName()));
                    }catch(SQLException I){
                        I.printStackTrace();
                    }
                    return true;
                }

                break;
            }
            case "vote":{
                if(!(sender instanceof Player)){
                    sendMessage(sender, getLang("console-cannot-use"));
                    return  true;
                }

                break;
            }
            case "start":{
                if(!sender.hasPermission("force.start")){
                    sender.sendMessage(getLang("no-permissions"));
                    return true;
                }
                if(!Meetup.getInstance().checkForceStart()){
                    sender.sendMessage(getLang("force-start-denied"));
                    return true;
                }
                if(Meetup.getInstance().started){
                    sender.sendMessage(getLang("force-already-starting"));
                   return true;
                }
                Meetup.getInstance().started = true;
                Meetup.getStates.state = Meetup.getStates.COUNTDOWN;
                Bukkit.broadcastMessage(getLang("forced-start").replace("<Player>", sender.getName()));
                Meetup.getInstance().getScoreboardManager().startCountdown();
                break;
            }
            case "reroll":{
                if(!(sender instanceof Player)){
                    sendMessage(sender, getLang("console-cannot-use"));
                    return  true;
                }
                if(!sender.hasPermission("reroll.perm")){
                    sender.sendMessage(getLang("no-permissions"));
                    return true;
                }
                if(Meetup.getStates.state != Meetup.getStates.STARTING){
                    sendMessage(sender, getLang("cannot-reroll"));
                    return true;
                }
                if(reroll.contains(((Player) sender).getUniqueId())){
                    sendMessage(sender, getLang("cannot-reroll-anymore"));
                    return true;
                }
                Player p = ((Player) sender).getPlayer();
                boolean rodless = Meetup.getInstance().getScenarioManager().getMostVoted().getName().equalsIgnoreCase("Rodless");
                boolean bowless = Meetup.getInstance().getScenarioManager().getMostVoted().getName().equalsIgnoreCase("Bowless");
                for(int i = 100; i != 0; i--){

                    Kit k = Meetup.getInstance().getKitManager().getKit(Meetup.getInstance().kitFile.getInt("Armor.minimum-complexity"), Meetup.getInstance().kitFile.getInt("Armor.maximum-complexity"));
                    if(k == null)continue;

                    Meetup.getInstance().getKitManager().setKit(p, k, false, rodless, bowless);
                    reroll.add(p.getUniqueId());
                    break;
                }
                sendMessage(p, getLang("rerolled"));

            }
        }
        return false;
    }
    private static List<UUID>reroll = new ArrayList<>();


    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private boolean isPlayerInSoloStats(UUID uuid) {
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM Meetup_solo_stats WHERE UUID='" + uuid.toString() + "'");
            return rs.next() && rs.getString("UUID") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean editKit(Player p){
        Meetup instance = Meetup.getInstance();
        if(instance.getLobbyEvents().getEditingKit().contains(p.getUniqueId())){
            sendMessage(p, getLang("already-editing-kit"));
            return true;
        }
        sendMessage(p, getLang("loading-kit-edit-database"));
        load(p);
        instance.getKitManager().giveDefaultEditKit(p);
        p.setScoreboard(instance.getScoreboardManager().getEditScoreboard());
        instance.getLobbyEvents().getEditingKit().add(p.getUniqueId());
        sendMessage(p, getLang("loaded-kit-edit-database"));

        return true;
    }
    private boolean saveKit(Player p){
        Meetup instance = Meetup.getInstance();
        if(!instance.getLobbyEvents().getEditingKit().contains(p.getUniqueId())){
            sendMessage(p, getLang("not-editing-kit"));
            return true;
        }
        sendMessage(p, getLang("saving-kit"));
        instance.getKitManager().detectInventory(p);
        sendMessage(p, getLang("kit-saved"));
        instance.getLobbyEvents().getEditingKit().remove(p.getUniqueId());
        p.setScoreboard(instance.getScoreboardManager().getLobbyScoreboard());
        return  true;
    }
    public boolean saveKit(Player p, Boolean log){
        Meetup instance = Meetup.getInstance();
        if(!instance.getLobbyEvents().getEditingKit().contains(p.getUniqueId())){
            return true;
        }
        instance.getKitManager().detectInventory(p);
        instance.getLobbyEvents().getEditingKit().remove(p.getUniqueId());

        return  true;
    }
    private void load(Player p) {
        try {
            Meetup.getInstance().getInter().loadPlayerInventory(p);
        }catch (SQLException io){
            io.printStackTrace();
        }
    }

    private String playedTime(int i){
        if(i < 60){
            return 60 + " seconds";
        }
        if(i < 3600){
            return ((int)i%60) + " minutes";
        }
        return round((i/3600.0), 2) + "h";
    }

    private Inventory statsInventory(String playerName, Integer win, Integer kills, Integer deaths, Integer playedTime, Integer maxKills, Integer gappsConsumed){
        Inventory inv = Bukkit.createInventory(null, 9*2, ChatColor.translateAlternateColorCodes('&', "&4" + playerName + "'s stats"));

        ItemStack winItem = itemCreator(Material.NETHER_STAR, 1, (short)0, "&7Wins:&f " + win, null);
        ItemStack killItem = itemCreator(Material.DIAMOND_SWORD, 1, (short)0, "&7Kills:&f " + kills, null);
        ItemStack deathItem = itemCreator(Material.REDSTONE, 1, (short)0, "&7Deaths:&f " + deaths, null);
        ItemStack kdr = itemCreator(Material.IRON_SWORD, 1, (short)0, "&7KDR:&f " + round(((Double.parseDouble(kills + ".0") / Double.parseDouble(deaths + ".0"))), 2), null);
        ItemStack playedTimeItem = itemCreator(Material.WATCH, 1, (short)0, "&7Played Time:&f " + playedTime(playedTime), null);
        ItemStack killRecord = itemCreator(Material.GOLD_SWORD, 1, (short)0, "&7Kill Record:&f "  + maxKills, null);
        ItemStack gappsEaten = itemCreator(Material.GOLDEN_APPLE, 1, (short)0, "&7Golden Apples consumed:&f " + gappsConsumed, null);

        inv.setItem(2, playedTimeItem);
        inv.setItem(4, killItem);
        inv.setItem(6, deathItem);
        inv.setItem(10, winItem);
        inv.setItem(12, kdr);
        inv.setItem(14, killRecord);
        inv.setItem(16, gappsEaten);

        return inv;
    }


}