package me.infinity.minigame.meetup.EventsManager;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Utils.Player.GamePlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class CoreEvents implements Listener {

    Map<UUID, GamePlayer> map;
    MinecraftServer server;

    public CoreEvents(){
        map = Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap();
        server = ((CraftServer) Bukkit.getServer()).getServer();
    }

    public void removePlayers(Player receiver) {
        Collection<? extends Player> playersBukkit = Bukkit.getOnlinePlayers();
        EntityPlayer[] playersNMS = new EntityPlayer[playersBukkit.size()];
        int current = 0;
        for (Player player : playersBukkit) {
            playersNMS[current] = ((CraftPlayer) player).getHandle();
            current++;
        }
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, playersNMS);
        ((CraftPlayer) receiver).getHandle().playerConnection.sendPacket(packet);
    }

    public EntityPlayer createPlayers(String name, String listName, String texture, String signature) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldserver = server.getWorldServer(0);
        PlayerInteractManager playerinteractmanager = new PlayerInteractManager(worldserver);
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        profile.getProperties().put("textures", new Property("textures", texture, signature));
        EntityPlayer player = new EntityPlayer(server, worldserver, profile, playerinteractmanager);
        player.listName = new ChatComponentText(listName);
        return player;
    }

    public void addPlayers(Player receiver, EntityPlayer... createdPlayers) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, createdPlayers);
        ((CraftPlayer) receiver).getHandle().playerConnection.sendPacket(packet);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        e.setJoinMessage("");
        Player p = e.getPlayer();
        p.setMaximumNoDamageTicks(19);
        p.setDisplayName("Pan");
        new BukkitRunnable(){
            @Override
            public void run(){
                EntityPlayer s = ((CraftPlayer) p).getHandle();
                s.listName = new ChatComponentText("Juancito");
                PacketPlayOutPlayerInfo packet2 = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, s);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet2);

            }
        }.runTaskLater(Meetup.getInstance(), 40L);
    }

    @EventHandler
    public void onInv(InventoryClickEvent e){
        if(e.getClickedInventory() == null)return;
        if(!e.getClickedInventory().getName().contains("'s stats"))return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onInv(InventoryInteractEvent e){
        if(e.getInventory() == null)return;
        if(!e.getInventory().getName().contains("'s stats"))return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){

        e.setQuitMessage("");

        GamePlayer gp = map.get(e.getPlayer().getUniqueId());

        map.remove(gp);
    }

    @EventHandler
    public void onKick(PlayerKickEvent e){

        e.setLeaveMessage("");

        GamePlayer gp = map.get(e.getPlayer().getUniqueId());

        map.remove(gp);
    }

    @EventHandler
    public void onArrow(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Arrow))return;
        if(!(((Arrow) e.getDamager()).getShooter() instanceof  Player))return;
        if(!(e.getEntity() instanceof  Player)) return;

        Player p = (Player) e.getEntity();

        if(p.getHealth() - e.getFinalDamage() <= 0.0D)return;

        Player shooter = ((Player) ((Arrow) e.getDamager()).getShooter());

        if(shooter == p)return;

        shooter.sendMessage(ChatColor.translateAlternateColorCodes('&',"&f" + p.getName() + " &7is now at &c" + (((int)(p.getHealth() - e.getFinalDamage())) / 2.0D) + "&4❤"));

    }


    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e){
        if(!e.toWeatherState())return;
        e.setCancelled(true);
    }
    @EventHandler
    public void onThunder(ThunderChangeEvent e){
        if(!e.toThunderState())return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onDeathEvent(PlayerDeathEvent e){
        Player p = e.getEntity();

        if(e.getEntity().getKiller() == null){
            e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&7" + e.getDeathMessage().replace(e.getEntity().getName(), "&f&l" + e.getEntity().getName() + "&r&7")));
        }else{
            e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&7" + e.getDeathMessage().replace(e.getEntity().getName(), "&f&l" + e.getEntity().getName() + "&r&7").replace(e.getEntity().getKiller().getName(), "&f&l" + e.getEntity().getKiller().getName() + "&r&7")));
        }


        p.setHealth(20.0D);
        WorldServer world = ((CraftWorld) p.getLocation().getWorld()).getHandle();

        EntityPlayer entity = new EntityPlayer(server, world, new GameProfile(p.getUniqueId(), p.getName()), new PlayerInteractManager(world));
        Location loc = p.getLocation();
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ()+0.5, loc.getYaw(), loc.getPitch());


        PacketPlayOutEntityStatus killAnimationPacket = new PacketPlayOutEntityStatus(entity, (byte)3);
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(entity);

        for(Player all : Bukkit.getOnlinePlayers()){
            if(all == p)continue;
            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(spawnPacket);
            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(killAnimationPacket);
            all.hidePlayer(p);
        }


    }

}
