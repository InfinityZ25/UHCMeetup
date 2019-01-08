package me.infinity.minigame.meetup.Utils.Scenario;

import me.infinity.minigame.meetup.Meetup;
import me.infinity.minigame.meetup.Utils.MessageSenderUtil;
import me.infinity.minigame.meetup.Utils.Scenario.Listeners.*;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class ScenarioManager extends MessageSenderUtil {
    public List<Scenario> scenarios = new ArrayList<>();
    private Inventory votinGui;
    public BukkitTask t;
    //  private HashMap<Integer, > intLore;

    public ScenarioManager(){


        loadScenarios();
        votinGui = setupInventory();

       t = new BukkitRunnable(){
           Meetup instance = Meetup.getInstance();

            @Override
            public void run(){
                Team team = instance.getScoreboardManager().getLobbyScoreboard().getTeam("scenarioTeam");
                if(team == null)return;
                team.setSuffix(getMostVoted().getName());

            }

        }.runTaskTimer(Meetup.getInstance(), 0, 20L);
    }

    public Scenario getScenario(String name){
        for(Scenario scenario : scenarios){
            if(!scenario.getName().equalsIgnoreCase(name))continue;
            return scenario;
        }
        return null;

    }

    public Scenario getMostVoted(){
        int i = 0;
        Scenario scenario= null;


        for(Scenario scen : scenarios){
            int points = scen.getPoints();
            if (points > i) {
                i = points;
                scenario = scen;
            }
        }

        if(scenario == null){
            scenario = new Scenario(new ScenarioTemplate(), "Vanilla");
        }

        return scenario;
    }

    public void voteScenario(Integer slot, Scenario scen, Integer vote){
        ItemMeta m = votinGui.getItem(slot).getItemMeta();
        scen.add(vote);

        List<String> str = m.getLore();
        str.remove(m.getLore().size()-1);
        str.add("§6Votes: §f" + scen.getPoints());
        m.setLore(str);

        votinGui.getItem(slot).setItemMeta(m);
    }

    private void loadScenarios(){
        Scenario absorptionLess = createScenario(new Absorptionless(), "Absorptionless");
        Scenario bowLess = createScenario(new Bowless(), "Bowless");
        Scenario fireLess = createScenario(new Fireless(), "Fireless");
        Scenario noClean = createScenario(new NoClean(), "NoClean");
        Scenario rodLess = createScenario(new Rodless(), "Rodless");
        Scenario safeLoot = createScenario(new Safeloot(), "Safeloot");
        Scenario timeBomb = createScenario(new Timebomb(), "Timebomb");
    }

    private Scenario createScenario(Listener listener, String name){
        Scenario scenario = new Scenario(listener, name);
        this.scenarios.add(scenario);
        return scenario;
    }

    private Inventory setupInventory(){
        Inventory inv = Bukkit.createInventory(null, 9 , getLang("scenarios.voting-gui-title"));

        ItemStack noClean = itemCreator(Material.NAME_TAG,  1,  (short)0, "&cNo Clean", createLore(getLang("scenarios.items-lore")));
        ItemStack timeBomb = itemCreator(Material.TNT,  1,  (short)0, "&cTimebomb", createLore(getLang("scenarios.items-lore")));
        ItemStack fireLess = itemCreator(Material.BLAZE_POWDER,  1,  (short)0, "&cFireless", createLore(getLang("scenarios.items-lore")));
        ItemStack bowLess = itemCreator(Material.BOW,  1,  (short)0, "&cBowless", createLore(getLang("scenarios.items-lore")));
        ItemStack rodLess = itemCreator(Material.FISHING_ROD,  1,  (short)0, "&cRodless", createLore(getLang("scenarios.items-lore")));
        ItemStack absorptionLess = itemCreator(Material.GOLDEN_APPLE,  1,  (short)0, "&CAbsorptionless", createLore(getLang("scenarios.items-lore")));
        ItemStack safeLoot = itemCreator(Material.SEEDS,  1,  (short)0, "&cSafe Loot", createLore(getLang("scenarios.items-lore")));

        inv.setItem(1, noClean);
        inv.setItem(2, timeBomb);
        inv.setItem(3, fireLess);
        inv.setItem(4, bowLess);
        inv.setItem(5, rodLess);
        inv.setItem(6, absorptionLess);
        inv.setItem(7, safeLoot);


        return inv;
    }
    private Inventory in(){

        Inventory inv = Bukkit.createInventory(null, 9 , getLang("scenarios.voting-gui-title"));
        int s = 0;
        for(ItemStack i : votinGui){
            inv.setItem(s, i);
            s++;
        }

        return inv;
    }

    public Inventory getVotinGui() {
        return votinGui;
    }

    private ItemStack addGlow(ItemStack item){
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
        tag.set("HideFlags", new NBTTagInt(1));
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);


    }

}
