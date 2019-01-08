package me.infinity.minigame.meetup.Commands;

import me.infinity.minigame.meetup.Meetup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        switch(cmd.getName().toLowerCase()){
            case "build":{
                if(!(sender instanceof Player)){
                    sender.sendMessage(getLang("console-cannot-use"));
                    return true;
                }
                if(!sender.hasPermission("meetup.buildmode")){
                    sender.sendMessage(getLang("no-permissions"));
                    return true;
                }

                Player p = (Player)sender;
                if(Meetup.getInstance().getLobbyEvents().getEditingKit().contains(p.getUniqueId())){
                    sender.sendMessage(getLang("cannot-do-while-editing"));
                    return true;
                }
                if(!Meetup.getInstance().getLobbyEvents().getBuildMode().contains(p.getUniqueId())){
                    Meetup.getInstance().getLobbyEvents().getBuildMode().add(p.getUniqueId());
                    sender.sendMessage(getLang("entered-buildmode"));
                    p.setGameMode(GameMode.CREATIVE);
                    return true;
                }

                Meetup.getInstance().getLobbyEvents().getBuildMode().remove(p.getUniqueId());
                sender.sendMessage(getLang("exit-buildmode"));
                p.setGameMode(GameMode.SURVIVAL);

                break;
            }
            case "closeinv":{
                if(!sender.hasPermission("console.perm")){
                    sender.sendMessage(getLang("no-permissions"));
                    return true;
                }
                Player p = Bukkit.getPlayer(args[0]);
                if(p==null)return true;
                p.getOpenInventory().close();
                break;
            }
        }
        return false;
    }

    private String getLang(String path){
        return ChatColor.translateAlternateColorCodes('&', Meetup.getInstance().getLangFile().getString(path));
    }
}
