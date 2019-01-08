package me.infinity.minigame.meetup.EventsManager;

import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Utils.Player.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.List;
import java.util.Random;

public class SpectatorEvents implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if(!Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(e.getPlayer().getUniqueId()).isSpectator())return;
        e.setCancelled(true);
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(!Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(e.getPlayer().getUniqueId()).isSpectator())return;
        e.setCancelled(true);
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(!Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(e.getPlayer().getUniqueId()).isSpectator())return;
        e.setCancelled(true);

        if(e.getPlayer().getItemInHand() == null || e.getPlayer().getItemInHand().getType() == Material.AIR)return;

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            switch(e.getItem().getType()){
                case BED:{
                    Meetup.getInstance().sendHub(e.getPlayer());
                    break;
                }
                case COMPASS:{
                    compassEvent(e.getPlayer());
                    break;

                }
            }

        }
    }

    private void compassEvent(Player player){
        List<GamePlayer> gm= Meetup.getInstance().getPlayerManager().playersAlive;
        player.teleport(gm.get(new Random().nextInt(gm.size())).getPlayer());
    }

    @EventHandler
    public void pickUpItem(PlayerPickupItemEvent e){
        if(!Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(e.getPlayer().getUniqueId()).isSpectator())return;
        e.setCancelled(true);
    }
    @EventHandler
    public void onBreak(PlayerDropItemEvent e){
        if(!Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(e.getPlayer().getUniqueId()).isSpectator())return;
        e.setCancelled(true);
    }
    @EventHandler
    public void onCreative(InventoryCreativeEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player)) return;
        Player p = (Player) e.getDamager();
        if(!Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId()).isSpectator())return;
        e.setCancelled(true);
    }
}
