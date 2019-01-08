package me.infinity.minigame.meetup.Utils.Scenario.Listeners;

import me.infinity.minigame.meetup.Meetup;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Absorptionless extends ScenarioTemplate {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e){
        if(e.getItem() == null) return;
        if(e.getItem().getType() != Material.GOLDEN_APPLE) return;

        Player p = e.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                p.removePotionEffect(PotionEffectType.ABSORPTION);
            }
        }.runTaskLater(Meetup.getInstance(), 1l);

    }
}
