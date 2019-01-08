package me.infinity.minigame.meetup.EventsManager;

import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Tasks.CountdownTask;
import me.infinity.minigame.meetup.Utils.LocationUtils;
import me.infinity.minigame.meetup.Utils.MessageSenderUtil;
import net.md_5.bungee.api.ChatColor;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LobbyEvents extends MessageSenderUtil implements Listener {
    private List<UUID> editingKit;
    private List<UUID> buildMode;
    private Meetup instance;
    private List<UUID> joinedTime;

    public LobbyEvents(Meetup meetup){
        editingKit = new ArrayList<>();
        buildMode = new ArrayList<>();
        joinedTime = new ArrayList<>();
        instance = meetup;
    }
    public List<UUID> getEditingKit() {
        return editingKit;
    }

    public List<UUID> getBuildMode() {
        return buildMode;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        cleanPlayer(p);

        p.teleport(LocationUtils.getLobby());
        p.setScoreboard(instance.getScoreboardManager().getLobbyScoreboard());
        instance.getScoreboardManager().updateOnlinePlayers();

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    instance.getInter().loadPlayer(p, false);
                } catch (Exception io) {
                    io.printStackTrace();
                }
            }
        }.runTask(instance);

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', instance.getLangFile().getString("player-joined").replace("<Player>", p.getName()).replace("<online>", Bukkit.getOnlinePlayers().size() + "")));

        if(instance.checkAutoStart() && !instance.started){
            //Auto start!
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', instance.getLangFile().getString("auto-start")));
            instance.started = true;
            Meetup.getStates.state = Meetup.getStates.COUNTDOWN;
            Meetup.getInstance().getScoreboardManager().startCountdown();
            giveLobbyItems(p, false);
        }
        if(instance.started){
            giveLobbyItems(p, false);
        }
        else{
            giveLobbyItems(p, true);
        }
        if(p.hasPermission("reserved.slot"))return;
        if(joinedTime.contains(p.getUniqueId()))return;
        joinedTime.add(p.getUniqueId());
    }

    public void removeEditors(){
        for(Player all : Bukkit.getOnlinePlayers()){
            all.getInventory().clear();
        }
        editingKit.clear();
        editingKit = null;
    }

    public void cleanPlayer(Player p){
        p.setFoodLevel(20);
        p.setHealth(20.0D);

        p.getInventory().setArmorContents(null);
        p.getOpenInventory().close();
        p.getInventory().clear();

        p.setTotalExperience(0);
        p.setLevel(0);
        p.setExp(0F);
        p.setGameMode(GameMode.SURVIVAL);
        for(PotionEffect ef : p.getActivePotionEffects()){
            p.removePotionEffect(ef.getType());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        new BukkitRunnable() {
            @Override
            public void run() {
                instance.getScoreboardManager().updateOnlinePlayers();
            }
        }.runTaskLater(instance, 1);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', instance.getLangFile().getString("player-left").replace("<Player>", e.getPlayer().getName()).replace("<online>", (Bukkit.getOnlinePlayers().size()-1) + "")));
        if(editingKit.contains(e.getPlayer().getUniqueId()))editingKit.remove(e.getPlayer().getUniqueId());
        if(joinedTime.contains(e.getPlayer().getUniqueId()))joinedTime.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e){
        new BukkitRunnable() {
            @Override
            public void run() {
                instance.getScoreboardManager().updateOnlinePlayers();
            }
        }.runTaskLater(instance, 1);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', instance.getLangFile().getString("player-left").replace("<Player>", e.getPlayer().getName()).replace("<online>", (Bukkit.getOnlinePlayers().size()-1) + "")));
        if(editingKit.contains(e.getPlayer().getUniqueId()))editingKit.remove(e.getPlayer().getUniqueId());
        if(joinedTime.contains(e.getPlayer().getUniqueId()))joinedTime.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onHunguer(FoodLevelChangeEvent e){
        e.setFoodLevel(20);
    }

    //All this listeners prevent players from griefing on inv edit mode!
    @EventHandler
    public void onInv(InventoryClickEvent e){
        if(editingKit == null)return;
        if(!editingKit.contains(e.getWhoClicked().getUniqueId()))return;
        if(e.getClickedInventory() == null)return;
        if(e.getClickedInventory().getType() == InventoryType.CRAFTING){
            e.setCancelled(true);
            return;
        }
        else if(e.getClickedInventory().getType() == InventoryType.PLAYER){
            if(e.getSlot() >= 36 && e.getSlot() <= 39){
                e.setCancelled(true);
                return;
            }
            switch (e.getAction()){
                case PLACE_ALL:
                case PICKUP_ALL:
                case SWAP_WITH_CURSOR:
                case HOTBAR_SWAP:
                case DROP_ALL_CURSOR:
                case DROP_ALL_SLOT:
                    break;
                default:
                    e.setCancelled(true);
            }
            return;
        }

    }

    private List<UUID> voted = new ArrayList<>();
    private void vote(ClickType clickType, String scenarioName, int slot, HumanEntity p){

        if(clickType  == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT){
            if(!p.hasPermission("reserved.slot")){
                p.sendMessage("No perms");
                return;
            }
            if(!p.hasPermission("scenario." + (slot+1))){
                sendMessage(p, getLang("scenarios.dont-own"));
                PlayerPointsAPI playerPointsAPI = PlayerPoints.getAPI();
                int i = playerPointsAPI.look(p.getUniqueId());

                if(i < instance.getConfig().getInt("Settings.Scenarios-Bonus-Pricing")){
                    sendMessage(p, getLang("scenarios.not-enough-points"));
                    Bukkit.getPlayer(p.getUniqueId()).playSound(p.getLocation(), Sound.VILLAGER_NO, 10, 1);
                    return;
                }
                //Give kit
                PlayerPoints.getAPI().take(p.getUniqueId(), instance.getConfig().getInt("Settings.Scenarios-Bonus-Pricing"));
                sendMessage(p, getLang("scenarios.gotten-bonus"));
                Bukkit.getPlayer(p.getUniqueId()).playSound(p.getLocation(), Sound.ANVIL_USE, 10, 1);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user "+ p.getName() + " permission set scenario." + (slot+1));
                return;
            }
            if(!voted.contains(p.getUniqueId())){
                voted.add(p.getUniqueId());
                instance.getScenarioManager().voteScenario(slot, instance.getScenarioManager().getScenario(scenarioName), instance.getConfig().getInt("Settings.Scenarios-Bonus-Amount"));
                sendMessage(p, getLang("scenarios.used-bonus"));
                Bukkit.getPlayer(p.getUniqueId()).playSound(p.getLocation(), Sound.LEVEL_UP, 10, 1);
                return;
            }
            sendMessage(p, getLang("scenarios.already-used-bonus"));
            Bukkit.getPlayer(p.getUniqueId()).playSound(p.getLocation(), Sound.VILLAGER_NO, 10, 1);



        }
        else if(clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT) {
            PlayerPointsAPI playerPointsAPI = PlayerPoints.getAPI();
            int i = playerPointsAPI.look(p.getUniqueId());
            if(i < instance.getConfig().getInt("Settings.Scenarios-Pricing")){
                sendMessage(p, getLang("scenarios.not-enough-points"));
                Bukkit.getPlayer(p.getUniqueId()).playSound(p.getLocation(), Sound.VILLAGER_NO, 10, 1);
                return;
            }
            Bukkit.getPlayer(p.getUniqueId()).playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 10, 1);

            playerPointsAPI.take(p.getUniqueId(), instance.getConfig().getInt("Settings.Scenarios-Pricing"));

            instance.getScenarioManager().voteScenario(slot, instance.getScenarioManager().getScenario(scenarioName), instance.getConfig().getInt("Settings.Scenarios-Amount"));
        }
    }
    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null)return;
        if (editingKit.contains(e.getWhoClicked().getUniqueId())) return;
        e.setCancelled(true);
        if(!e.getClickedInventory().getTitle().equalsIgnoreCase(instance.getScenarioManager().getVotinGui().getTitle()))return;

        switch(e.getSlot()){
            case 1:{
                new  BukkitRunnable(){
                    @Override
                    public void run(){
                        vote(e.getClick(), "NoClean", e.getSlot(), e.getWhoClicked());
                        instance.getScenarioManager().getVotinGui().getItem(e.getSlot()).setAmount(instance.getScenarioManager().getScenario("NoClean").getPoints());
                    }
                }.runTaskAsynchronously(instance);

                break;
            }
            case 2:{
                new  BukkitRunnable(){
                    @Override
                    public void run(){
                        vote(e.getClick(), "Timebomb", e.getSlot(), e.getWhoClicked());
                        instance.getScenarioManager().getVotinGui().getItem(e.getSlot()).setAmount(instance.getScenarioManager().getScenario("Timebomb").getPoints());
                    }
                }.runTaskAsynchronously(instance);
                break;
            }
            case 3:{
                new  BukkitRunnable(){
                    @Override
                    public void run(){
                        vote(e.getClick(), "Fireless", e.getSlot(), e.getWhoClicked());
                        instance.getScenarioManager().getVotinGui().getItem(e.getSlot()).setAmount(instance.getScenarioManager().getScenario("Fireless").getPoints());
                    }
                }.runTaskAsynchronously(instance);
                break;
            }
            case 4:{
                new  BukkitRunnable(){
                    @Override
                    public void run(){
                        vote(e.getClick(), "Bowless", e.getSlot(), e.getWhoClicked());
                        instance.getScenarioManager().getVotinGui().getItem(e.getSlot()).setAmount(instance.getScenarioManager().getScenario("Bowless").getPoints());
                    }
                }.runTaskAsynchronously(instance);
                break;
            }
            case 5:{
                new  BukkitRunnable(){
                    @Override
                    public void run(){
                        vote(e.getClick(), "Rodless", e.getSlot(), e.getWhoClicked());
                        instance.getScenarioManager().getVotinGui().getItem(e.getSlot()).setAmount(instance.getScenarioManager().getScenario("Rodless").getPoints());
                    }
                }.runTaskAsynchronously(instance);
                break;
            }
            case 6:{
                new  BukkitRunnable(){
                    @Override
                    public void run(){
                        vote(e.getClick(), "Absorptionless", e.getSlot(), e.getWhoClicked());
                        instance.getScenarioManager().getVotinGui().getItem(e.getSlot()).setAmount(instance.getScenarioManager().getScenario("Absorptionless").getPoints());
                    }
                }.runTaskAsynchronously(instance);
                break;
            }
            case 7:{
                new  BukkitRunnable(){
                    @Override
                    public void run(){
                        vote(e.getClick(), "Safeloot", e.getSlot(), e.getWhoClicked());
                        instance.getScenarioManager().getVotinGui().getItem(e.getSlot()).setAmount(instance.getScenarioManager().getScenario("Safeloot").getPoints());
                    }
                }.runTaskAsynchronously(instance);
                break;
            }
        }
    }
    @EventHandler
    public void invDrag(InventoryDragEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if(buildMode.contains(e.getPlayer().getUniqueId()))return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(buildMode.contains(e.getPlayer().getUniqueId()))return;
        e.setCancelled(true);

        if(e.getItem() == null)return;
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            switch(e.getItem().getType()){
                case BED:{
                    instance.sendHub(e.getPlayer());
                    break;
                }
                case PAPER:{
                    e.getPlayer().openInventory(instance.getScenarioManager().getVotinGui());
                    break;
                }case BOOK:{
                    if(editingKit == null)return;
                    if(editingKit.contains(e.getPlayer().getUniqueId()))return;
                    e.getPlayer().performCommand("editkit");
                    break;
                }

            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e){
        if(buildMode.contains(e.getPlayer().getUniqueId()))return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(buildMode.contains(e.getPlayer().getUniqueId()))return;
        e.setCancelled(true);
    }
    @EventHandler
    public void onBlockPlaceEvent(BlockBreakEvent e){
        if(buildMode.contains(e.getPlayer().getUniqueId()))return;
        e.setCancelled(true);
    }


    @EventHandler
    public void onLogin(PlayerLoginEvent e){
        if(Bukkit.getOnlinePlayers().size()+1 < instance.getConfig().getInt("Settings.MaxNonVip"))return;
        if(!e.getPlayer().hasPermission("reserved.slot")){
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.translateAlternateColorCodes('&',instance.getLangFile().getString("cant-join-full")));
            return;
        }

//Kick player
        for(int i = 1; i < 24; i++){
            try {
                UUID ud = joinedTime.get(joinedTime.size() - i);
                Player p = Bukkit.getPlayer(ud);
                p.kickPlayer(ChatColor.translateAlternateColorCodes('&',instance.getLangFile().getString("cant-join-full")));
                break;
            }catch(Exception io){
                io.printStackTrace();
            }
        }


    }
    public void giveLobbyItems(Player p, Boolean bol){
        Inventory inv = p.getInventory();

        ItemStack voteItem = itemCreator(Material.matchMaterial(getString("LobbyItems.Vote.Item")), 1, (short)0, getString("LobbyItems.Vote.Name"), createLore(getString("LobbyItems.Vote.Lore")));

        ItemStack invEditItem = itemCreator(Material.matchMaterial(getString("LobbyItems.KitEditor.Item")), 1, (short)0, getString("LobbyItems.KitEditor.Name"), createLore(getString("LobbyItems.KitEditor.Lore")));

        ItemStack returnLobbyItem = itemCreator(Material.matchMaterial(getString("LobbyItems.Bed.Item")), 1, (short)0, getString("LobbyItems.Bed.Name"), createLore(getString("LobbyItems.Bed.Lore")));

        inv.setItem(getInt("LobbyItems.Bed.Slot"), returnLobbyItem);
        inv.setItem(getInt("LobbyItems.Vote.Slot"), voteItem);
        if(bol){
            inv.setItem(getInt("LobbyItems.KitEditor.Slot"), invEditItem);
        }



    }
    private String getString(String str){
        return Meetup.getInstance().getConfig().getString(str);
    }
    private Integer getInt(String str){
        return Meetup.getInstance().getConfig().getInt(str);
    }

}
