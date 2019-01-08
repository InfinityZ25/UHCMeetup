package me.infinity.minigame.meetup.Utils.Scenario.Listeners;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Bowless extends ScenarioTemplate{

    @EventHandler
    public void onCraftItem(CraftItemEvent e){
        if(e.getRecipe().getResult() == null)return;
        if(e.getRecipe().getResult().getType() != Material.BOW)return;
        e.setResult(Event.Result.DENY);
        e.setCancelled(true);
        sendMessage(e.getWhoClicked(), super.getLang("scenarios-bowless"));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getPlayer().getItemInHand() == null)return;
        if(e.getItem() == null)return;
        if(e.getItem().getType() != Material.BOW)return;
        e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
        e.setCancelled(true);
        sendMessage(e.getPlayer(), super.getLang("scenarios-bowless"));
    }

}
