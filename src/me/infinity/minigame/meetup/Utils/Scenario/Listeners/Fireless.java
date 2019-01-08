package me.infinity.minigame.meetup.Utils.Scenario.Listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class Fireless extends ScenarioTemplate{

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getEntity().getType() != EntityType.PLAYER)return;
        if((e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || e.getCause() == EntityDamageEvent.DamageCause.LAVA)){
            e.setCancelled(true);
        }
    }
}
