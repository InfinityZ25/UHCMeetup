package me.infinity.minigame.meetup.Utils.Scenario.Listeners;

import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import me.infinity.minigame.meetup.Meetup;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntity;
import net.minecraft.server.v1_8_R3.TileEntityChest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Timebomb extends ScenarioTemplate{

    @EventHandler
    private void onPlayerDeathEvent(PlayerDeathEvent e){
        Player p = e.getEntity();
        createChest(p, e, true);
    }

    public Block[] createChest(Player p, PlayerDeathEvent event, Boolean useTimebomb){
        Location loc = p.getLocation().clone();


        //Get the block downwards
        Block b = loc.getBlock().getRelative(BlockFace.DOWN);
        b.setType(Material.CHEST);
        //Get the block to the north of the mentioned before
        b.getRelative(BlockFace.NORTH).setType(Material.CHEST);
        //Set blocks relatively up to air to avoid it from bugging
        loc.getBlock().setType(Material.AIR);
        loc.getBlock().getRelative(BlockFace.NORTH).setType(Material.AIR);
        //Transform the block state to a chest
        Chest chest = (Chest) b.getState();
        //get the block involved
        Block[] s = new Block[]{b, b.getRelative(BlockFace.NORTH)};
        //Get the drops and place them on chest
        for (ItemStack item : event.getDrops()) {
            if (item != null) {
                if (item.getType() == Material.AIR) {
                    continue;
                }
                chest.getInventory().addItem(item);
            }
        }
        event.getDrops().clear();
        //Create a hologram
        if(useTimebomb) {
            Hologram hologram = HologramAPI.createHologram(chest.getLocation().clone().add(0.5, 2.0, 0.0), "");
            hologram.spawn();

            new BukkitRunnable() {
                private int time = 31;

                public void run() {
                    --time;
                    if(time >=15) {
                        hologram.setText("§a" + time + "s");
                    }
                    else if (time >= 4 && time <= 14) {
                        hologram.setText("§e" + time + "s");
                    }
                    else if (time == 3) {
                        hologram.setText("§6" + time + "s");
                    }
                    else if (time == 2) {
                        hologram.setText("§c" + time + "s");
                    }
                    else if (time == 1) {
                        hologram.setText("§4" + time + "s");
                    }
                    else if (time == 0) {
                        loc.getBlock().setType(Material.AIR);
                        loc.getWorld().strikeLightning(loc);
                        loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 4.0F, false, true);
                        hologram.despawn();
                        b.setType(Material.AIR);
                        b.getRelative(BlockFace.NORTH).setType(Material.AIR);

                        this.cancel();
                    }

                }
            }.runTaskTimer(Meetup.getInstance(), 0L, 20L);

        }
        return  s;
    }

    public void setName(Block block, String name)
    {
        if (block.getType() != Material.CHEST)
        {
            // Not a chest
            return;
        }

        // Get the NMS World
        net.minecraft.server.v1_8_R3.World nmsWorld = ((CraftWorld) block.getLocation().getWorld()).getHandle();
        // Get the tile entity
        TileEntity te = nmsWorld.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        // Make sure it's a TileEntityChest before using it
        if (!(te instanceof TileEntityChest))
        {
            // Not a chest :o!
            return;
        }
        ((TileEntityChest) te).a(name);
    }
}
