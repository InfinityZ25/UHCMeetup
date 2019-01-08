package me.infinity.minigame.meetup.EventsManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class StartingEvents implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        e.setCancelled(true);
    }
}
