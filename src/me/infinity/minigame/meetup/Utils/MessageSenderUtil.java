package me.infinity.minigame.meetup.Utils;

import me.infinity.minigame.meetup.Meetup;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MessageSenderUtil {

    public String getLang(String path){
        return ChatColor.translateAlternateColorCodes('&', Meetup.getInstance().getLangFile().getString(path));
    }

    public void sendMessage(Player sender , String message){
        if(message.isEmpty())return;
        sender.sendMessage(message);
    }

    public void sendMessage(CommandSender sender , String message){
        if(message.isEmpty())return;
        sender.sendMessage(message);
    }
    public ItemStack itemCreator(Material m, Integer amount, Short durability, String name, List<String> lore){
        ItemStack Item = new ItemStack(m, amount, durability);
        ItemMeta meta = Item.getItemMeta();
        if(!name.isEmpty()){
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            if(lore != null && !lore.isEmpty()) {
                meta.setLore(lore);
            }
            Item.setItemMeta(meta);
        }
        return Item;
    }
    public List<String> createLore(String string){
        List<String> s = new ArrayList<>();
        for(String c : string.split(";;")){
            s.add(ChatColor.translateAlternateColorCodes('&', c));

        }
        return s;
    }

}
