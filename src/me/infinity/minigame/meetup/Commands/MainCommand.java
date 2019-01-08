package me.infinity.minigame.meetup.Commands;

import com.mojang.authlib.GameProfile;
import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Utils.Kit.Kit;
import me.infinity.minigame.meetup.Utils.Player.GamePlayer;
import me.infinity.minigame.meetup.Utils.Scoreboard.ScoreboardManager;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.SQLException;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("meetup")){
            switch(Integer.parseInt(args[0])){
                case 1:{
                    Player p = Bukkit.getPlayer(args[1]);
                    GamePlayer gamePlayer = Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());
                    gamePlayer.addKills(2);
                    Meetup.getInstance().getInter().savePlayer(p);
                    Meetup.getInstance().log("Executed", true);
                    break;
                }
                case 2:{
                    Player p = Bukkit.getPlayer(args[1]);
                    GamePlayer gamePlayer = Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());
                    Meetup.getInstance().log(p.getName() + "'s stats: " + gamePlayer.getWins() + gamePlayer.getKills() + gamePlayer.getDeaths(), true);

                    break;
                }
                case 3:{
                    sender.sendMessage(ChatColor.GREEN + "Loading your inventory...");
                    load((Player)sender);
                    Meetup.getInstance().getKitManager().giveDefaultEditKit((Player)sender);
                    sender.sendMessage(ChatColor.GREEN + "Inventory succesfully loaded!");
                    break;
                }

                case 4:{
                    sender.sendMessage(ChatColor.GREEN + "This method reads your inventory and saves it to mysql!");
                    Meetup.getInstance().getKitManager().detectInventory((Player)sender);
                    sender.sendMessage(ChatColor.GREEN + "Kit has been saved!");

                    break;
                }
                case 5:{
                    sender.sendMessage("This method tests the get kit method.");
                    Meetup.getInstance().getKitManager().setKit((Player) sender, Meetup.getInstance().getKitManager().getKit(6, 12), false, false, false);
                    sender.sendMessage("Kit given");
                    break;
                }
                case 6:{
                    sender.sendMessage("This method tests the get kit method in a for method");
                    Integer i = Integer.parseInt(args[1]);
                    for(int e = 0; e <= i; e++){
                        Meetup.getInstance().getKitManager().setKit((Player) sender, Meetup.getInstance().getKitManager().getKit(6, 12), false, false, false);
                    }
                    sender.sendMessage("Kit given");
                    break;
                }
                case 7:{
                    Integer i = Integer.parseInt(args[1]);
                    sender.sendMessage("Getting kit " + i);
                    Kit k = Meetup.getInstance().getKitManager().getKits().get(i);
                    Meetup.getInstance().getKitManager().setKit((Player)sender, k, false, false, false);
                    break;
                }
                case 8:{
                    Player p = (Player)sender;
                    Integer i = Integer.parseInt(args[1]);
                    switch(i){
                        case 1:
                            sender.sendMessage("Setting the edit kit scoreboard!");
                            p.setScoreboard(Meetup.getInstance().getScoreboardManager().getEditScoreboard());
                            break;
                        case 2:
                            sender.sendMessage("Setting the edit waiting scoreboard!");
                            p.setScoreboard(Meetup.getInstance().getScoreboardManager().getLobbyScoreboard());
                            break;
                    }
                    break;
                }
                case 9:{
                    Meetup.getInstance().getScoreboardManager().startCountdown();
                    break;
                }
                case 10:{
                    Player p = (Player)sender;

                    MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
                    WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
                    EntityPlayer entity = new EntityPlayer(server, world, new GameProfile(p.getUniqueId(), ""), new PlayerInteractManager(world));

                    Location loc = p.getLocation();
                    entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                    entity.setCustomNameVisible(false);

                    PacketPlayOutEntityStatus killAnimationPacket = new PacketPlayOutEntityStatus(entity, (byte)Integer.parseInt(args[1]));
                    PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(entity);
                    PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy();


                    for(Player all : Bukkit.getOnlinePlayers()){
                        ((CraftPlayer) all).getHandle().playerConnection.sendPacket(spawnPacket);
                        // ((CraftPlayer) all).getHandle().playerConnection.sendPacket(killAnimationPacket);

                    }

                    break;
                }
                case 11:{

                    Player p = (Player)sender;
                    for(Player all : Bukkit.getOnlinePlayers()){
                        p.showPlayer(all);
                    }
                    break;
                }
                case 12:{
                    Player p = (Player)sender;

                    EntityPlayer entity = ((CraftPlayer) p).getHandle();

                    PacketPlayOutEntityVelocity packetPlayOutEntityVelocity = new PacketPlayOutEntityVelocity(entity.getId(), Short.parseShort(args[1]), Short.parseShort(args[2]), Short.parseShort(args[3]));
                    for(Player all : Bukkit.getOnlinePlayers()){
                        ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityVelocity);
                    }
                    break;
                }
                case 14:{
                    Player p = (Player)sender;

                    MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
                    WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();

                    EntityPlayer entity = new EntityPlayer(server, world, new GameProfile(p.getUniqueId(), p.getName()) , new PlayerInteractManager(world));

                    Location loc = p.getLocation();
                    BlockPosition blockPos = new BlockPosition(loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ());

                    PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(entity);
                    PacketPlayOutBed packetPlayOutBed = new PacketPlayOutBed(entity, blockPos);
                    //PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam("hideNametag", (byte)1);


                    for(Player all : Bukkit.getOnlinePlayers()){
                        ((CraftPlayer) all).getHandle().playerConnection.sendPacket(spawnPacket);
                        ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutBed);
                        all.getScoreboard().getTeam("hideNametag").addPlayer(p);
                        // ((CraftPlayer) all).getHandle().playerConnection.sendPacket(killAnimationPacket);

                    }
                    break;
                }
                case 15:{
                    sender.sendMessage(Meetup.getInstance().getScenarioManager().getMostVoted().getName());

                    break;
                }
                case 16:{
                    Meetup.getInstance().getScenarioManager().scenarios.forEach(a ->{
                        Bukkit.broadcastMessage(a.getName());
                    });
                }
            }


        }

        return false;
    }

    public void load(Player p) {
        try {
            Meetup.getInstance().getInter().loadPlayerInventory(p);
        }catch (SQLException io){
            io.printStackTrace();
        }
    }
}
