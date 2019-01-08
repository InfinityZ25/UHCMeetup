package me.infinity.minigame.meetup.EventsManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FallDamageProtector implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(!e.getCause().equals(EntityDamageEvent.DamageCause.FALL))return;
        e.setCancelled(true);
    }
}
