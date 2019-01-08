package me.infinity.minigame.meetup.Utils.Kit;

import me.infinity.minigame.meetup.Database.Core.MySQL;
import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Utils.ConfigFile;
import me.infinity.minigame.meetup.Utils.Player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KitManager {


    //This list must be calculate when the server starts, maximum of 24 kits.
    private List<Kit> kits = new ArrayList<>();
    public Kit defaultKit = new Kit(new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.BOW),6, 3, 10, true, true );

    public KitManager(){
        Meetup.getInstance().log(" Loading Kits...");
        for(int i = 100; i != 0; i--){
            if(kits.size() >= 25)break;

            Kit k = getKit(Meetup.getInstance().kitFile.getInt("Armor.minimum-complexity"), Meetup.getInstance().kitFile.getInt("Armor.maximum-complexity"));
            if(k == null)continue;
            kits.add(k);
        }
        Meetup.getInstance().log(" Kits have been successfully loaded!");
    }
    public List<Kit> getKits() {
        return kits;
    }


    private Kit getKitNull(int minimumComplexity, int maximumComplexity){
        {
            int complexity = 0;
            ConfigFile kitcfg = Meetup.getInstance().kitFile;
            //Helmet
            ItemStack helmet = null;
            {
                int amount_helmets = kitcfg.getConfigurationSection("Armor.Helmets").getKeys(false).size();
                int chosen_helmet_group = randomNumberA(maximumComplexity, complexity, 3, amount_helmets);

                complexity = complexity + chosen_helmet_group;
                int amount_helmet_group = kitcfg.getConfigurationSection("Armor.Helmets." + chosen_helmet_group).getKeys(false).size();
                if (amount_helmet_group > 1) {

                    int selected = new Random().nextInt(amount_helmet_group) + 1;

                    String path = "Armor.Helmets." + chosen_helmet_group + "." + fromNumberToLetter(selected);

                    ItemStack is = new ItemStack(Material.matchMaterial(kitcfg.getString(path + ".Item")));
                    //Test if the item has enchantments or not
                    if (!kitcfg.getString(path + ".Enchants").equalsIgnoreCase("none")) {
                        //Apply the enchantments
                        for (String s : kitcfg.getString(path + ".Enchants").split(";")) {
                            String[] enchantInfo = s.split("-");
                            is.addEnchantment(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]));
                        }
                    }
                    ItemMeta im = is.getItemMeta();
                    if (!kitcfg.getString(path + ".Item-Name").equalsIgnoreCase("none")) {
                        im.setDisplayName(kitcfg.getString(path + ".Item-Name").replace("&", ChatColor.COLOR_CHAR + ""));
                    }
                    is.setItemMeta(im);
                    helmet = is;

                } else {
                    String path = "Armor.Helmets." + chosen_helmet_group + ".a";
                    ItemStack is = new ItemStack(Material.matchMaterial(kitcfg.getString(path + ".Item")));
                    //Test if the item has enchantments or not
                    if (!kitcfg.getString(path + ".Enchants").equalsIgnoreCase("none")) {
                        //Apply the enchantments
                        for (String s : kitcfg.getString(path + ".Enchants").split(";")) {
                            String[] enchantInfo = s.split("-");
                            is.addEnchantment(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]));

                        }
                    }
                    ItemMeta im = is.getItemMeta();
                    if (!kitcfg.getString(path + ".Item-Name").equalsIgnoreCase("none")) {
                        im.setDisplayName(kitcfg.getString(path + ".Item-Name").replace("&", ChatColor.COLOR_CHAR + ""));
                    }
                    is.setItemMeta(im);
                    helmet = is;
                }
            }

            //Chestplate
            ItemStack chestplate = null;
            {
                int amount_chesplates = kitcfg.getConfigurationSection("Armor.Chestplates").getKeys(false).size();
                int chosen_chestplate_group = randomNumberA(maximumComplexity, complexity, 2, amount_chesplates);

                complexity = complexity + chosen_chestplate_group;
                int amount_chestplate_group = kitcfg.getConfigurationSection("Armor.Chestplates." + chosen_chestplate_group).getKeys(false).size();
                if (amount_chestplate_group > 1) {

                    int selected = new Random().nextInt(amount_chestplate_group) + 1;

                    String path = "Armor.Chestplates." + chosen_chestplate_group + "." + fromNumberToLetter(selected);

                    ItemStack is = new ItemStack(Material.matchMaterial(kitcfg.getString(path + ".Item")));
                    //Test if the item has enchantments or not
                    if (!kitcfg.getString(path + ".Enchants").equalsIgnoreCase("none")) {
                        //Apply the enchantments
                        for (String s : kitcfg.getString(path + ".Enchants").split(";")) {
                            String[] enchantInfo = s.split("-");
                            is.addEnchantment(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]));
                        }
                    }
                    ItemMeta im = is.getItemMeta();
                    if (!kitcfg.getString(path + ".Item-Name").equalsIgnoreCase("none")) {
                        im.setDisplayName(kitcfg.getString(path + ".Item-Name").replace("&", ChatColor.COLOR_CHAR + ""));
                    }
                    is.setItemMeta(im);
                    chestplate = is;

                } else {
                    String path = "Armor.Chestplates." + chosen_chestplate_group + ".a";
                    ItemStack is = new ItemStack(Material.matchMaterial(kitcfg.getString(path + ".Item")));
                    //Test if the item has enchantments or not
                    if (!kitcfg.getString(path + ".Enchants").equalsIgnoreCase("none")) {
                        //Apply the enchantments
                        for (String s : kitcfg.getString(path + ".Enchants").split(";")) {
                            String[] enchantInfo = s.split("-");
                            is.addEnchantment(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]));

                        }
                    }
                    ItemMeta im = is.getItemMeta();
                    if (!kitcfg.getString(path + ".Item-Name").equalsIgnoreCase("none")) {
                        im.setDisplayName(kitcfg.getString(path + ".Item-Name").replace("&", ChatColor.COLOR_CHAR + ""));
                    }
                    is.setItemMeta(im);
                    chestplate = is;
                }
            }

            //Leggings
            ItemStack leggings = null;
            {
                int amount_leggings = kitcfg.getConfigurationSection("Armor.Leggings").getKeys(false).size();
                int chosen_leggings_group = randomNumberA(maximumComplexity, complexity, 1, amount_leggings);

                complexity = complexity + chosen_leggings_group;
                int amount_legging_group = kitcfg.getConfigurationSection("Armor.Leggings." + chosen_leggings_group).getKeys(false).size();
                if (amount_legging_group > 1) {

                    int selected = new Random().nextInt(amount_legging_group) + 1;

                    String path = "Armor.Leggings." + chosen_leggings_group + "." + fromNumberToLetter(selected);

                    ItemStack is = new ItemStack(Material.matchMaterial(kitcfg.getString(path + ".Item")));
                    //Test if the item has enchantments or not
                    if (!kitcfg.getString(path + ".Enchants").equalsIgnoreCase("none")) {
                        //Apply the enchantments
                        for (String s : kitcfg.getString(path + ".Enchants").split(";")) {
                            String[] enchantInfo = s.split("-");
                            is.addEnchantment(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]));
                        }
                    }
                    ItemMeta im = is.getItemMeta();
                    if (!kitcfg.getString(path + ".Item-Name").equalsIgnoreCase("none")) {
                        im.setDisplayName(kitcfg.getString(path + ".Item-Name").replace("&", ChatColor.COLOR_CHAR + ""));
                    }
                    is.setItemMeta(im);
                    leggings = is;

                } else {
                    String path = "Armor.Leggings." + amount_legging_group + ".a";
                    ItemStack is = new ItemStack(Material.matchMaterial(kitcfg.getString(path + ".Item")));
                    //Test if the item has enchantments or not
                    if (!kitcfg.getString(path + ".Enchants").equalsIgnoreCase("none")) {
                        //Apply the enchantments
                        for (String s : kitcfg.getString(path + ".Enchants").split(";")) {
                            String[] enchantInfo = s.split("-");
                            is.addEnchantment(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]));

                        }
                    }
                    ItemMeta im = is.getItemMeta();
                    if (!kitcfg.getString(path + ".Item-Name").equalsIgnoreCase("none")) {
                        im.setDisplayName(kitcfg.getString(path + ".Item-Name").replace("&", ChatColor.COLOR_CHAR + ""));
                    }
                    is.setItemMeta(im);
                    leggings = is;
                }
            }

            //Boots
            ItemStack boots = null;
            {
                int amount_boots = kitcfg.getConfigurationSection("Armor.Boots").getKeys(false).size();
                int chosen_boots_group = randomNumberA(maximumComplexity, complexity, 0, amount_boots);
                complexity = complexity + chosen_boots_group;
                int amount_boots_group = kitcfg.getConfigurationSection("Armor.Boots." + chosen_boots_group).getKeys(false).size();
                if (amount_boots_group > 1) {

                    int selected = new Random().nextInt(amount_boots_group) + 1;

                    String path = "Armor.Boots." + amount_boots_group + "." + fromNumberToLetter(selected);

                    ItemStack is = new ItemStack(Material.matchMaterial(kitcfg.getString(path + ".Item")));
                    //Test if the item has enchantments or not
                    if (!kitcfg.getString(path + ".Enchants").equalsIgnoreCase("none")) {
                        //Apply the enchantments
                        for (String s : kitcfg.getString(path + ".Enchants").split(";")) {
                            String[] enchantInfo = s.split("-");
                            is.addEnchantment(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]));
                        }
                    }
                    ItemMeta im = is.getItemMeta();
                    if (!kitcfg.getString(path + ".Item-Name").equalsIgnoreCase("none")) {
                        im.setDisplayName(kitcfg.getString(path + ".Item-Name").replace("&", ChatColor.COLOR_CHAR + ""));
                    }
                    is.setItemMeta(im);
                    boots = is;

                } else {
                    String path = "Armor.Boots." + amount_boots_group + ".a";
                    ItemStack is = new ItemStack(Material.matchMaterial(kitcfg.getString(path + ".Item")));
                    //Test if the item has enchantments or not
                    if (!kitcfg.getString(path + ".Enchants").equalsIgnoreCase("none")) {
                        //Apply the enchantments
                        for (String s : kitcfg.getString(path + ".Enchants").split(";")) {
                            String[] enchantInfo = s.split("-");
                            is.addEnchantment(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]));

                        }
                    }
                    ItemMeta im = is.getItemMeta();
                    if (!kitcfg.getString(path + ".Item-Name").equalsIgnoreCase("none")) {
                        im.setDisplayName(kitcfg.getString(path + ".Item-Name").replace("&", ChatColor.COLOR_CHAR + ""));
                    }
                    is.setItemMeta(im);
                    boots = is;
                }

            }
            if (complexity < minimumComplexity) getKit(minimumComplexity, maximumComplexity);

            //Sword
            ItemStack sword = null;
            {
                int available_complexity = maximumComplexity - complexity;
                int available_groups = kitcfg.getConfigurationSection("Weapons.Swords").getKeys(false).size() - 1;
                int selected_group;

                if (available_complexity >= available_groups) {
                    selected_group = available_groups;
                } else {
                    selected_group = available_complexity;
                }

                int group_size = kitcfg.getConfigurationSection("Weapons.Swords." + selected_group + "").getKeys(false).size();

                if (group_size > 1) {
                    int selected = new Random().nextInt(group_size) + 1;
                    String path = "Weapons.Swords." + selected_group + "." + fromNumberToLetter(selected);

                    ItemStack is = new ItemStack(Material.matchMaterial(kitcfg.getString(path + ".Item")));
                    //Test if the item has enchantments or not
                    if (!kitcfg.getString(path + ".Enchants").equalsIgnoreCase("none")) {
                        //Apply the enchantments
                        for (String s : kitcfg.getString(path + ".Enchants").split(";")) {
                            String[] enchantInfo = s.split("-");
                            is.addEnchantment(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]));
                        }
                    }
                    ItemMeta im = is.getItemMeta();
                    if (!kitcfg.getString(path + ".Item-Name").equalsIgnoreCase("none")) {
                        im.setDisplayName(kitcfg.getString(path + ".Item-Name").replace("&", ChatColor.COLOR_CHAR + ""));
                    }
                    is.setItemMeta(im);
                    sword = is;

                } else {
                    String path = "Weapons.Swords." + selected_group + ".a";

                    ItemStack is = new ItemStack(Material.matchMaterial(kitcfg.getString(path + ".Item")));
                    //Test if the item has enchantments or not
                    if (!kitcfg.getString(path + ".Enchants").equalsIgnoreCase("none")) {
                        //Apply the enchantments
                        for (String s : kitcfg.getString(path + ".Enchants").split(";")) {
                            String[] enchantInfo = s.split("-");
                            is.addEnchantment(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]));
                        }
                    }
                    ItemMeta im = is.getItemMeta();
                    if (!kitcfg.getString(path + ".Item-Name").equalsIgnoreCase("none")) {
                        im.setDisplayName(kitcfg.getString(path + ".Item-Name").replace("&", ChatColor.COLOR_CHAR + ""));
                    }
                    is.setItemMeta(im);
                    sword = is;

                }

            }

            //Bow
            ItemStack bow = null;
            {
                int available_complexity = maximumComplexity - complexity;
                int available_groups = kitcfg.getConfigurationSection("Weapons.Bows").getKeys(false).size() - 1;
                int selected_group;

                if (available_complexity > available_groups) {
                    selected_group = available_groups;

                } else {
                    selected_group = available_complexity;
                }

                int group_size = kitcfg.getConfigurationSection("Weapons.Bows." + selected_group + "").getKeys(false).size();

                if (group_size > 1) {
                    int selected = new Random().nextInt(group_size) + 1;
                    String path = "Weapons.Bows." + selected_group + "." + fromNumberToLetter(selected);

                    ItemStack is = new ItemStack(Material.matchMaterial(kitcfg.getString(path + ".Item")));
                    //Test if the item has enchantments or not
                    if (!kitcfg.getString(path + ".Enchants").equalsIgnoreCase("none")) {
                        //Apply the enchantments
                        for (String s : kitcfg.getString(path + ".Enchants").split(";")) {
                            String[] enchantInfo = s.split("-");
                            is.addEnchantment(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]));
                        }
                    }
                    ItemMeta im = is.getItemMeta();
                    if (!kitcfg.getString(path + ".Item-Name").equalsIgnoreCase("none")) {
                        im.setDisplayName(kitcfg.getString(path + ".Item-Name").replace("&", ChatColor.COLOR_CHAR + ""));
                    }
                    is.setItemMeta(im);
                    bow = is;

                } else {
                    String path = "Weapons.Bows." + selected_group + ".a";

                    ItemStack is = new ItemStack(Material.matchMaterial(kitcfg.getString(path + ".Item")));
                    //Test if the item has enchantments or not
                    if (!kitcfg.getString(path + ".Enchants").equalsIgnoreCase("none")) {
                        //Apply the enchantments
                        for (String s : kitcfg.getString(path + ".Enchants").split(";")) {
                            String[] enchantInfo = s.split("-");
                            is.addEnchantment(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]));
                        }
                    }
                    ItemMeta im = is.getItemMeta();
                    if (!kitcfg.getString(path + ".Item-Name").equalsIgnoreCase("none")) {
                        im.setDisplayName(kitcfg.getString(path + ".Item-Name").replace("&", ChatColor.COLOR_CHAR + ""));
                    }
                    is.setItemMeta(im);
                    bow = is;

                }

            }
            //Get healing:
            int goldenApples;
            int goldenHeads;
            {
                int group_size = kitcfg.getConfigurationSection("Healing").getKeys(false).size();
                int selected = new Random().nextInt(group_size - 1) + 1;

                goldenApples = kitcfg.getInt("Healing." + selected + ".Golden-Apples");
                goldenHeads = kitcfg.getInt("Healing." + selected + ".Golden-Heads");
            }

            //Get cobwebs

            int cb = 0;
            if (new Random().nextDouble() < (kitcfg.getDouble("Cobweb.Chance") / 100)) {
                int min = kitcfg.getInt("Cobweb.Amount-Min");
                int max = kitcfg.getInt("Cobweb.Amount-Max");
                int amt = new Random().nextInt(max - min) + min + 1;
                cb = amt;
            }
            //Fire resistance
            boolean resistance = false;
            if (new Random().nextDouble() < (kitcfg.getDouble("Potions.Fire-Resistance.Chance") / 100)) {

                resistance = true;
            }
            //Speed Potion
            boolean speed = false;
            if (new Random().nextDouble() < (kitcfg.getDouble("Potions.Swiftness.Chance") / 100)) {
                speed = true;
            }


            return new Kit(helmet, chestplate, leggings, boots, sword, bow, goldenApples, goldenHeads, cb, speed, resistance);
        }
    }
    public Kit getKit(Integer minimumComplexity, Integer maximumComplexity){
        try{
            return getKitNull(minimumComplexity, maximumComplexity);

        }catch(Exception io){
            return null;
        }
    }

    private String fromNumberToLetter(Integer i){
        String str = "";
        switch(i){
            case 1:{
                str = "a";
                break;
            }
            case 2:{
                str = "b";
                break;
            }
            case 3:{
                str = "c";
                break;
            }
            case 4:{
                str = "d";
                break;
            }
            case 5:{
                str = "e";
                break;
            }
        }
        return str;
    }
    private Integer avalaiableComplexity(Integer max, Integer current, Integer left){
        return max - current - left;
    }

    private Integer randomNumberA(Integer max, Integer current, Integer left, Integer possible){
        int num =  new Random().nextInt(possible - 1) + 1;
        if(avalaiableComplexity(max, current, left) >= num){
            return num;
        }
        else{
            randomNumberA(max, current, left, possible);
        }
        return 3;
    }


    public void giveDefaultEditKit(Player p){
        setKit(p, defaultKit, true, false, false);
    }

    public void detectInventory(Player p){
        PlayerInventory inv = p.getInventory();
        GamePlayer gp = Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId());

        int[] hotbar = gp.getHotbar().clone();
        int water = 1;
        int lava = 1;

        for(ItemStack i : inv.getContents()){
            if(i == null || i.getType() == Material.AIR){
                continue;
            }
            int slot;
            switch(i.getType()){
                case DIAMOND_SWORD:{
                    slot = inv.first(Material.DIAMOND_SWORD);
                    hotbar[0] = slot;
                    break;
                }
                case FISHING_ROD:{
                    slot = inv.first(Material.FISHING_ROD);
                    hotbar[1] = slot;
                    break;
                }
                case BOW:{
                    slot = inv.first(Material.BOW);
                    hotbar[2] = slot;
                    break;
                }/*
                case APPLE:{
                    slot = inv.first(Material.APPLE);
                    hotbar[3] = slot;
                    break;
                }*/
                case GOLDEN_APPLE:{
                    slot = inv.first(Material.GOLDEN_APPLE);
                    ItemStack ie = inv.getItem(slot);
                    if(ie.hasItemMeta()){
                        hotbar[4] = slot;
                    }
                    else {
                        hotbar[3] = slot;
                    }
                    inv.remove(ie);
                    break;
                }
                case WATER_BUCKET:{
                    slot = inv.first(Material.WATER_BUCKET);
                    inv.setItem(slot, new ItemStack(Material.AIR));
                    hotbar[5 + (water-1)] = slot;
                    water++;
                    break;
                }
                case LAVA_BUCKET:{
                    slot = inv.first(Material.LAVA_BUCKET);
                    inv.setItem(slot, new ItemStack(Material.AIR));
                    hotbar[7 + (lava-1)] = slot;
                    lava++;
                    break;
                }
                case COOKED_BEEF:{
                    slot = inv.first(Material.COOKED_BEEF);
                    hotbar[9] = slot;
                    break;
                }
                case COBBLESTONE:{
                    slot = inv.first(Material.COBBLESTONE);
                    hotbar[10] = slot;
                    break;
                }
                case WOOD:{
                    slot = inv.first(Material.WOOD);
                    hotbar[11] = slot;
                    break;
                }
                case DIAMOND_AXE:{
                    slot = inv.first(Material.DIAMOND_AXE);
                    hotbar[12] = slot;
                    break;
                }
                case DIAMOND_PICKAXE:{
                    slot = inv.first(Material.DIAMOND_PICKAXE);
                    hotbar[13] = slot;
                    break;
                }
                case ARROW:{
                    slot = inv.first(Material.ARROW);
                    hotbar[14] = slot;
                    break;
                }
                case WEB:{
                    slot = inv.first(Material.WEB);
                    hotbar[15] = slot;
                    break;
                }
                case POTION:{
                    slot = inv.first(Material.POTION);
                    ItemStack ie = inv.getItem(slot);
                    if(ie.getDurability() == 8258){
                        hotbar[16] = slot;
                    }
                    else if(ie.getDurability() == 8259){
                        hotbar[17] = slot;
                    }
                    inv.remove(ie);
                    break;
                }
                case ANVIL:
                    slot = inv.first(Material.ANVIL);
                    hotbar[18] = slot;
                    break;
                case ENCHANTMENT_TABLE:
                    slot = inv.first(Material.ENCHANTMENT_TABLE);
                    hotbar[19] = slot;
                    break;

            }
        }
        gp.setHotbar(hotbar);
        Meetup.getInstance().getInter().savePlayerInventory(p);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

    }

    public void setKit(Player p, Kit kit, Boolean editing, Boolean rodLess, Boolean bowLess){
        PlayerInventory inv = p.getInventory();

        //Clear players armor
        inv.clear();
        inv.setArmorContents(null);

        //Set armor
        inv.setHelmet(kit.getHelmet());
        inv.setLeggings(kit.getLeggings());
        inv.setChestplate(kit.getChestplate());
        inv.setBoots(kit.getBoots());

        //Get the gameplayer's hotbar
        int[] hotbar = Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId()).getHotbar();
        if(hotbar[0] == hotbar[1]){
            p.sendMessage(ChatColor.RED + "You joined too late into the game, you'll recieve your kit in one second!");
            new BukkitRunnable(){
             @Override
             public void run(){
                 try {
                     Meetup.getInstance().getInter().loadPlayerInventory(p);
                     setKit(p, kit, editing, rodLess, bowLess);
                 }catch(Exception io){
                     io.printStackTrace();
                     Bukkit.broadcastMessage(ChatColor.RED + "An exception has occurred, reported it via twitter!");
                 }

             }
            }.runTaskLaterAsynchronously(Meetup.getInstance(), 5L);
            return;
            //Meetup.getInstance().getPlayerManager().getUuidGamePlayerMap().get(p.getUniqueId()).setHotbar(new int[]{0, 1, 2, 3, 5, 4, 25, 6, 7, 34, 8, 16, 9, 18, 27, 35, 17, 26, 10, 19});
        }

        //Set sword
        inv.setItem(hotbar[0], kit.getSword());
        //Set rod
        if(!rodLess) {
            inv.setItem(hotbar[1], new ItemStack(Material.FISHING_ROD, 1));
        }
        //Set Bow
        if(!bowLess) {
            inv.setItem(hotbar[2], kit.getBow());
        }
        //Set Golden Apples
        if(kit.getGoldenApples() > 0) {
            ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, kit.getGoldenApples());
            inv.setItem(hotbar[3], gapple);
        }
        //Set Golden Heads
        if(kit.getGoldenHeads() > 0) {
            ItemStack ghead = Meetup.getInstance().goldenHead.clone();
            ghead.setAmount(kit.getGoldenHeads());
            inv.setItem(hotbar[4], ghead);
        }
        //Set Water 1
        inv.setItem(hotbar[5], new ItemStack(Material.WATER_BUCKET, 1));
        //Set Water 2
        inv.setItem(hotbar[6], new ItemStack(Material.WATER_BUCKET, 1));
        //Set Lava 1
        inv.setItem(hotbar[7], new ItemStack(Material.LAVA_BUCKET, 1));
        //Set Lava 2
        inv.setItem(hotbar[8], new ItemStack(Material.LAVA_BUCKET, 1));
        //Set Food

        if(editing) {
            inv.setItem(hotbar[9], new ItemStack(Material.COOKED_BEEF, 32));
        }
        else{
            inv.setItem(hotbar[9], new ItemStack(Material.COOKED_BEEF, new Random().nextInt(48)+16));

        }
        //Set Cobblestone
        inv.setItem(hotbar[10], new ItemStack(Material.COBBLESTONE, 64));
        //Set Wood
        inv.setItem(hotbar[11], new ItemStack(Material.WOOD, 64));
        //Set Axe
        inv.setItem(hotbar[12], new ItemStack(Material.DIAMOND_AXE, 1));
        //Set Pickaxe
        inv.setItem(hotbar[13], new ItemStack(Material.DIAMOND_PICKAXE, 1));
        //Set Arrow
        if(editing) {
            inv.setItem(hotbar[14], new ItemStack(Material.ARROW, 16));
        }
        else{
            inv.setItem(hotbar[14], new ItemStack(Material.ARROW, new Random().nextInt(48) + 8));

        }
        //Set Cobweb
        if(editing){
            kit.setCobweb(10);
        }
        if(kit.getCobweb() > 0){

            inv.setItem(hotbar[15], new ItemStack(Material.WEB, kit.getCobweb()));
        }
        //Set Speed Potion
        if(kit.getSpeed()){
            if(editing){
                ItemStack i = new ItemStack(Material.POTION, 1, (short) 8194);
                ItemMeta im = i.getItemMeta();
                im.setDisplayName(ChatColor.DARK_AQUA + "Speed Potion");
                i.setItemMeta(im);
                inv.setItem(hotbar[16], i);
            }
            else {
                inv.setItem(hotbar[16], new ItemStack(Material.POTION, 1, (short) 8194));
            }
        }
        //Set Fire Resistance Potion
        if(kit.getFire()){
            if(editing){
                ItemStack i = new ItemStack(Material.POTION,1, (short)8195);
                ItemMeta im = i.getItemMeta();
                im.setDisplayName(ChatColor.RED + "Fire Resistance Potion");
                i.setItemMeta(im);
                inv.setItem(hotbar[17], i);
            }
            else {
                inv.setItem(hotbar[17], new ItemStack(Material.POTION, 1, (short) 8195));
            }
        }
        //Set Anvil
        inv.setItem(hotbar[18], new ItemStack(Material.ANVIL, 1));
        //Set Enchanting Table
        //inv.setItem(hotbar[19], new ItemStack(Material.ENCHANTMENT_TABLE, 1));





    }
}
