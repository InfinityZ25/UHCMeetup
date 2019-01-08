package me.infinity.minigame.meetup.Utils.Scenario.Listeners;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Rodless extends ScenarioTemplate{

    @EventHandler
    public void onCraftItem(CraftItemEvent e){
        if(e.getRecipe().getResult() == null)return;
        if(e.getRecipe().getResult().getType() != Material.FISHING_ROD)return;
        e.setResult(Event.Result.DENY);
        e.setCancelled(true);
        sendMessage(e.getWhoClicked(), getLang("scenarios-rodless"));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getPlayer().getItemInHand() == null)return;
        if(e.getItem() == null)return;
        if(e.getItem().getType() != Material.FISHING_ROD)return;
        e.setCancelled(true);
        e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR, 1));
        sendMessage(e.getPlayer(), getLang("scenarios-rodless"));
    }

}
