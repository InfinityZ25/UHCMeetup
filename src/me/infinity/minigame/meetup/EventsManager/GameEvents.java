package me.infinity.minigame.meetup.EventsManager;

import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Utils.MessageSenderUtil;
import me.infinity.minigame.meetup.Utils.Player.GamePlayer;
import me.infinity.minigame.meetup.Utils.Scoreboard.ScoreboardBuilder;
import net.md_5.bungee.api.ChatColor;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Currency;
import java.util.HashSet;
import java.util.Random;

public class GameEvents extends MessageSenderUtil implements Listener {

    private  HashSet<Block> blocksMeshable = new HashSet<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(e.getBlock().getType() == Material.LEAVES||e.getBlock().getType() == Material.OBSIDIAN ||e.getBlock().getType() == Material.LEAVES_2||e.getBlock().getType() == Material.BROWN_MUSHROOM||e.getBlock().getType() == Material.RED_MUSHROOM||e.getBlock().getType() == Material.LONG_GRASS||e.getBlock().getType() == Material.YELLOW_FLOWER||e.getBlock().getType() == Material.RED_ROSE||e.getBlock().getType() == Material.DOUBLE_PLANT||e.getBlock().getType() == Material.CACTUS||e.getBlock().getType() == Material.DEAD_BUSH) return;
        if(blocksMeshable.contains(e.getBlock())){
            blocksMeshable.remove(e.getBlock());
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if(!blocksMeshable.contains(e.getBlock())){
            blocksMeshable.add(e.getBlock());
            return;
        }
        e.setCancelled(true);
    }
    /*
        @EventHandler
        public void onBlockFromToEvent(BlockFromToEvent e){
            if(e.getToBlock().getType() != Material.LAVA) return;
            if(e.getToBlock().getType() != Material.STATIONARY_LAVA) return;
            if(e.getToBlock().getType() != Material.WATER) return;
            if(e.getToBlock().getType() != Material.STATIONARY_WATER) return;

            blocksMeshable.add(e.getToBlock());
        }*/
    @EventHandler
    public void onLogin(PlayerLoginEvent e) {

        switch (Meetup.getStates.state){
            case STARTING:{
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&',Meetup.getInstance().getLangFile().getString("cant-join-starting")));
                break;
            }
            default:
                if (e.getPlayer().hasPermission("reserved.slot"))  return;
                e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, ChatColor.translateAlternateColorCodes('&',Meetup.getInstance().getLangFile().getString("cant-join-spec")));
        }



    }

    @EventHandler
    public void onInv(InventoryInteractEvent e){
        if(e.getInventory() == null)return;
        if(!e.getInventory().getTitle().contains("'s stats"))return;
        e.setCancelled(true);
    }


    private void givePoints(Player p, int i){

        int points = getPoints(p, i);
        PlayerPoints.getAPI().give(p.getUniqueId(), points);
        sendMessage(p, ChatColor.translateAlternateColorCodes('&',getLang("points-recieved").replace("<Points>", points + "")));
    }
    private int getPoints(Player p, int i){
        if(p.hasPermission("noobsters.4"))return i *4;
        if(p.hasPermission("noobsters.3"))return i *3;
        if(p.hasPermission("noobsters.2"))return i *2;
        else return i;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        e.getDrops().add(Meetup.getInstance().goldenHead.clone());
        e.getDrops().add(new ItemStack(Material.EXP_BOTTLE, new Random().nextInt(8) +8));

        GamePlayer gp = Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());
        Meetup.getInstance().getPlayerManager().playersAlive.remove(gp);
        Meetup.getInstance().getScoreboardManager().updatePlayersAlive();

        e.setDeathMessage("");

        if(p.getKiller() != null){
            Bukkit.broadcastMessage((ChatColor.translateAlternateColorCodes('&',("&f&l" + p.getName() + "&r&7 was slain by " + "&f&l" + p.getKiller().getName()))));
            Player killer = p.getKiller();
            GamePlayer GK = Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(killer.getUniqueId());
            GK.addLKills(1);
            givePoints(killer, 1);


            if(GK.getLKills() > GK.getMaxKills()){
                GK.setMaxKills(GK.getLKills());
            }
            if(killer.isOnline()){
                killer.getScoreboard().getTeam("TeamSoloK").setSuffix(GK.getLKills() + "");
            }
        }
        else{
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l<Player>&r&7 has been disqualified from the game (Disconnected)".replace("<Player>", p.getName())));

        }

        if(p.isOnline()) {
            gp.setSpectator(true);
            new BukkitRunnable(){
                @Override
                public void run(){
                    setSpectator(p);
                }
            }.runTaskLater(Meetup.getInstance(), 1L);
        }

        //if(Meetup.getInstance().getPlayerManager().playersAlive.size() <= 1)return;
        gp.addDeaths(1);

        if(gp.getLKills() > gp.getMaxKills()){
            gp.setMaxKills(gp.getLKills());
        }
        int time = (int)((System.currentTimeMillis() - gp.getStartingTime()) / 1000);
        gp.addPlayedTime(time);
        gp.addKills(gp.getLKills());
        Meetup.getInstance().getInter().savePlayer(p);


    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        if(!Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().containsKey(e.getPlayer().getUniqueId()))return;
        if(Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(e.getPlayer().getUniqueId()).isSpectator())return;
        if(Meetup.getInstance().getPlayerManager().playersAlive.size() <= 1)return;
        if(e.getPlayer().isDead())return;

        e.getPlayer().damage(200);

    }

    @EventHandler
    public void onKick(PlayerKickEvent e){
        if(!Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().containsKey(e.getPlayer().getUniqueId()))return;
        if(Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(e.getPlayer().getUniqueId()).isSpectator())return;
        if(Meetup.getInstance().getPlayerManager().playersAlive.size() <= 1)return;
        if(e.getPlayer().isDead())return;

        e.getPlayer().damage(200);


    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        p.setScoreboard(ScoreboardBuilder.scoreboardBuilder("Scoreboards.ingame-scoreboard"));

        try {
            Meetup.getInstance().getInter().loadPlayer(p, true);
            p.teleport(new Location(Meetup.getInstance().game, 0 ,Meetup.getInstance().game.getHighestBlockYAt(0,0) ,0));
            setSpectator(p);
        } catch (Exception ie) {
            ie.printStackTrace();
        }


    }
    private void cleanPlayer(Player p){
        p.setFoodLevel(20);
        p.setHealth(20.0D);

        p.getInventory().setArmorContents(null);
        p.getInventory().clear();

        p.setTotalExperience(0);
        p.setLevel(0);
        p.setExp(0F);
        p.setGameMode(GameMode.CREATIVE);
        for(PotionEffect ef : p.getActivePotionEffects()){
            p.removePotionEffect(ef.getType());
        }
    }

    private void setSpectator(Player p){
        for(Player all : Bukkit.getOnlinePlayers()){
            all.hidePlayer(p);
        }
        cleanPlayer(p);
        Bukkit.getOnlinePlayers().forEach(player ->{
            if(Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(player.getUniqueId()).isSpectator()){
                p.hidePlayer(player);
            }
        });


        //Give spectator items
        p.getInventory().setItem(4, itemCreator(Material.COMPASS, 1, (short)0, "&f&lTeleport", createLore("&fRight click to teleport!")));
        p.getInventory().setItem(8, itemCreator(Material.BED, 1, (short)0, "&f&lLobby", createLore("&fRight click to return to lobby")));

    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e){
        switch(e.getItem().getType()){
            case GOLDEN_APPLE:{
                Player p = e.getPlayer();
                GamePlayer gp = Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());
                Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId()).addGappsEaten(1);
                p.removePotionEffect(PotionEffectType.REGENERATION);
                if(e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()){
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*10, 1));
                }
                else{
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*5, 1));

                }
                break;
            }
            case POTION:{
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.getPlayer().getInventory().remove(Material.GLASS_BOTTLE);
                    }
                }.runTaskLater(Meetup.getInstance(), 1);
                break;
            }
        }
    }


}
