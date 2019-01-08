package me.infinity.minigame.meetup.Utils.Scenario.Listeners;

import me.infinity.minigame.meetup.Meetup;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Safeloot extends Timebomb{

    private Map<Block, Player> chestMap = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Block[] chest = createChest(e.getEntity(), e, false);

        new BukkitRunnable() {

            public void run() {
                chest[0].breakNaturally();
                chest[1].breakNaturally();
                chestMap.remove(chest[0]);
                chestMap.remove(chest[1]);
            }

        }.runTaskLater(Meetup.getInstance(), 30 * 20L);

        if(e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player){
            chestMap.put(chest[0], e.getEntity().getKiller());
            chestMap.put(chest[1], e.getEntity().getKiller());
            sendMessage(e.getEntity().getKiller(), getLang("scenarios.safe-loot-message"));
            setName(chest[0], e.getEntity().getName() + "'s Loot (P)");
            return;
        }
        setName(chest[0], e.getEntity().getName() + "'s Loot");


    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if (!chestMap.containsKey(e.getBlock()))return;
        if (chestMap.get(e.getBlock()) == e.getPlayer()) {
            e.setCancelled(false);
            return;
        }
        e.setCancelled(true);
        e.getPlayer().sendMessage("You can't interact with this chest!");
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK)return;
        if(e.getClickedBlock().getType() != Material.CHEST)return;
        if(!chestMap.containsKey(e.getClickedBlock()))return;
        if(chestMap.get(e.getClickedBlock()) == e.getPlayer())return;
        e.setCancelled(true);
       sendMessage(e.getPlayer(), getLang("scenarios.safeloot-cannnot-break-block"));
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(!(e.getEntity() instanceof  Player))return;
        Player p = (Player) e.getEntity();
        if(p.getOpenInventory() != null && p.getOpenInventory().getTitle().contains("'s Loot (P)")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if(!(e.getInventory().getHolder() instanceof DoubleChest))return;
        DoubleChest c = (DoubleChest)e.getInventory().getHolder();
        Chest left = (Chest)c.getLeftSide();
        Chest right = (Chest)c.getRightSide();
        if(chestMap.containsKey(left.getBlock())) {
            sendMessage(chestMap.get(left.getBlock()), getLang("scenarios.safe-loot-lost"));
        }
        else if(chestMap.containsKey(right.getBlock())){
            sendMessage(chestMap.get(right.getBlock()), getLang("scenarios.safe-loot-lost"));
        }

        left.getBlock().breakNaturally();
        right.getBlock().breakNaturally();


    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e){
        if(e.getEntity().getType() != EntityType.DROPPED_ITEM)return;
        Item i = (Item) e.getEntity();
        if(i.getItemStack().getType() != Material.CHEST)return;
        i.remove();


    }



}
