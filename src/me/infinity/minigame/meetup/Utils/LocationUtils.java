package me.infinity.minigame.meetup.Utils;

import me.infinity.minigame.meetup.Meetup;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;

public class LocationUtils {
    private static Location findScatterLoc(World world, Integer size) {
        Location loc = new Location(world, 0, 0, 0);
        loc.setX(loc.getX() + Math.random() * size * 2.0 - size);
        loc.setZ(loc.getZ() + Math.random() * size * 2.0 - size);
        loc = loc.getWorld().getHighestBlockAt(loc).getLocation();
        if (loc.getWorld().getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getType() == Material.LAVA || loc.getWorld().getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getType() == Material.STATIONARY_LAVA || loc.getWorld().getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getType() == Material.STATIONARY_WATER || loc.getWorld().getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getType() == Material.WATER) {
            findScatterLoc(world, size);
        }
        return isntAir(loc);

    }

    public static Location findValidLocation(World world, Integer size, Integer distance) {
        Location loc = findScatterLoc(world, size);
        if(!isValid(loc, distance)){
            return findValidLocation(world, size, distance);
        }

        return loc;
    }

    private static Location isntAir(Location loc) {
        for(int i = 0; i < 100; i++){
            if(loc.getBlock().getType() != Material.AIR){
                //Bukkit.broadcastMessage("isn't air");
                loc = loc.getBlock().getRelative(BlockFace.UP).getLocation();
                break;
            }
            if (loc.getBlock().getType().equals(Material.AIR)) {
                //Bukkit.broadcastMessage("is air");
               // Bukkit.broadcastMessage(loc.getBlock().getType() + "");
                loc = loc.getBlock().getRelative(BlockFace.DOWN).getLocation();
            }
        }
        return loc;
    }


    private static boolean isValid(Location loc, Integer distance){
        if(Meetup.getInstance().scatterLocations.isEmpty()) return true;

        for(Location locs : Meetup.getInstance().scatterLocations){
            if(locs.distance(loc) < distance){
                return false;
            }
        }
        return true;
    }

    public static Location getLobby(){
        FileConfiguration cfg = Meetup.getInstance().getConfig();
        return new Location(Bukkit.getWorlds().get(0), cfg.getDouble("Spawn.Lobby.X"), cfg.getDouble("Spawn.Lobby.Y"), cfg.getDouble("Spawn.Lobby.Z"), cfg.getFloat("Spawn.Lobby.Yaw"), cfg.getFloat("Spawn.Lobby.Pitch"));
    }
}
