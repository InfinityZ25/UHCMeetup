package me.infinity.minigame.meetup;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.infinity.minigame.meetup.Commands.AdminCommands;
import me.infinity.minigame.meetup.Commands.MainCommand;
import me.infinity.minigame.meetup.Commands.UserCommands;
import me.infinity.minigame.meetup.Database.Core.MySQL;
import me.infinity.minigame.meetup.Database.PlayerDataInterface;
import me.infinity.minigame.meetup.Database.Types.VMySQL;
import me.infinity.minigame.meetup.EventsManager.CoreEvents;
import me.infinity.minigame.meetup.EventsManager.LobbyEvents;
import me.infinity.minigame.meetup.Utils.ConfigFile;
import me.infinity.minigame.meetup.Utils.Kit.KitManager;
import me.infinity.minigame.meetup.Utils.LocationUtils;
import me.infinity.minigame.meetup.Utils.Player.PlayerManager;
import me.infinity.minigame.meetup.Utils.Scenario.ScenarioManager;
import me.infinity.minigame.meetup.Utils.Scoreboard.ScoreboardManager;
import me.infinity.minigame.meetup.Utils.ZipperUnzipper;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Meetup extends JavaPlugin{

    private static Meetup instance;

    public ConfigFile scoreboardFile;
    private ConfigFile langFile;
    public ConfigFile kitFile;
    private World lobby;
    public World game;
    public ItemStack goldenHead;
    public List<Location> scatterLocations;
    private PlayerManager playerManager;
    private KitManager kitManager;
    private ScoreboardManager scoreboardManager;
    private ScenarioManager scenarioManager;
    private LobbyEvents lobbyEvents;
    private PlayerDataInterface inter;
    public Boolean started;

    public static Meetup getInstance() {
        return instance;
    }

    @Override
    public void onEnable(){
        //Instantiate the class Main so that it can be called back later.
        instance = this;

        //Setup Bungee
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        //Set game loading..
        getStates.state = getStates.LOADING;
        getMode.state = getMode.SOLO;

        //Setup all configs
        langFile = new ConfigFile(this, "lang.yml", "lang.yml");
        langFile.save();
        langFile.reload();

        scoreboardFile = new ConfigFile(this, "scoreboards.yml", "scoreboards.yml");
        scoreboardFile.save();
        scoreboardFile.reload();

        kitFile = new ConfigFile(this, "kit.yml", "kit.yml");
        kitFile.save();
        kitFile.reload();

        configDefault();

        //Zipper-Unzipper method.
        log(" Initializing unzipping process");
        try {
            int i = getConfig().getInt("Settings.AvailableMaps");
            ZipperUnzipper.unzip(getDataFolder().getAbsolutePath() + "/map_" + ( new Random().nextInt(i-1)+1) + ".zip", getServer().getWorldContainer().getAbsolutePath() + "/game");
            log(" Unzipping process has successfully completed!");
        }catch(Exception io){
            log(" An error has occurred whilst unzipping the map.zip file.");
        }

        //Scenario Manager
        scenarioManager = new ScenarioManager();

        //Scoreboard Manager
        scoreboardManager = new ScoreboardManager(this);

        //Player manager
        playerManager = new PlayerManager();

        //Golden Head
        goldenHead = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta goldenMeta = goldenHead.getItemMeta();
        goldenMeta.setDisplayName("&4Golden Head".replace("&", ChatColor.COLOR_CHAR + ""));
        goldenHead.setItemMeta(goldenMeta);

        //Load worlds and generate the Game world
        game = getServer().createWorld(new WorldCreator("game"));
        game.setGameRuleValue("naturalRegeneration", "false");
        game.getWorldBorder().setCenter(0,0);


        lobby = Bukkit.getWorlds().get(0);
        lobby.setDifficulty(Difficulty.PEACEFUL);
        lobby.setGameRuleValue("naturalRegeneration", "false");
        lobby.setFullTime(5000);
        lobby.getWorldBorder().setCenter(0,0);
        lobby.getWorldBorder().setSize(400);

        //Kits
        kitManager = new KitManager();


        //Connect to the database(MariaDB or MySQL)
        loadDatabase();

        //Load all Listeners
        loadEvents();

        //Register Commands
        loadCommands();

        super.onEnable();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb game setcorners 100 -100 -100 100");

        //Calculate the spawn locations:
        log(" Calculating spawn locations");
        scatterLocations = new ArrayList<>();
        for(int i =  24; i != 0; i--){
            Location loc = LocationUtils.findValidLocation(game, 99, 15);
            scatterLocations.add(loc);
        }
        log(" Spawn locations have been successfully calculated!");
        started = false;
        getStates.state = getStates.LOBBY;
    }

    @Override
    public void onDisable(){
        MySQL.disconnect();
        try {
            ZipperUnzipper.deleteWorld(Bukkit.getWorld("game").getWorldFolder());
        }catch(Exception io){};
        Bukkit.unloadWorld("game", false);
        super.onDisable();

    }

    private String getLang(String path){
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', getConfig().getString(path));
    }

    private void sendMessage(Player sender , String message){
        if(message.isEmpty())return;
        sender.sendMessage(message);
    }
    public void sendHub(Player player){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        sendMessage(player, getLang("Settings.SendingLobbyMessage"));
        out.writeUTF("Connect");
        out.writeUTF(getConfig().getString("Settings.HubServer"));

        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    public PlayerDataInterface getInter() {
        return inter;
    }
    public KitManager getKitManager() {
        return kitManager;
    }
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
    public ConfigFile getLangFile() {
        return langFile;
    }
    public LobbyEvents getLobbyEvents() {
        return lobbyEvents;
    }
    public ScenarioManager getScenarioManager() {
        return scenarioManager;
    }

    public void log(String message){
        System.out.print("[UHC Meetup]" +message);
    }
    public void log(String message, Boolean bol) {
        if(bol){
            for(Player all : Bukkit.getOnlinePlayers()){
                all.sendMessage(message.replace("&", ChatColor.COLOR_CHAR + ""));
            }
        }
        System.out.print("[UHC Meetup]" +message);
    }

    private void loadDatabase() {
        log(" Loading MYSQL");
        loadMySQL();
        inter = new VMySQL(this);


    }

    private void loadEvents(){
        getServer().getPluginManager().registerEvents(new CoreEvents(), this);
       // getServer().getPluginManager().registerEvents(new Safeloot(), this);
        //Lobby Events
        {
            lobbyEvents = new LobbyEvents(this);
            getServer().getPluginManager().registerEvents(lobbyEvents, this);
        }

    }

    private void configDefault() {
        FileConfiguration config = getConfig();

        config.addDefault("Stats.Ip", "142.44.143.99");
        config.addDefault("Stats.Port", 3306);
        config.addDefault("Stats.Database", "noobsters_data");
        config.addDefault("Stats.Username", "root");
        config.addDefault("Stats.Password", "Juanorlando1!");

        config.addDefault("Settings.MinForceStart", 1);
        config.addDefault("Settings.MinAutoStart", 16);
        config.addDefault("Settings.AvailableMaps", 2);
        config.addDefault("Settings.CloseServerAfter-Seconds", 15);
        config.addDefault("Settings.UseFireworksFor-Seconds", 5);
        config.addDefault("Settings.MaxNonVip", 24);
        config.addDefault("Settings.HubServer", "lobby");
        config.addDefault("Settings.SendingLobbyMessage", "&cConnecting to lobby");
        config.addDefault("Settings.Scenarios-Bonus-Pricing", 500);
        config.addDefault("Settings.Scenarios-Bonus-Amount", 20);
        config.addDefault("Settings.Scenarios-Pricing", 2);
        config.addDefault("Settings.Scenarios-Amount", 2);
        //Settings.Scenarios-Pricing"

        config.addDefault("Spawn.Lobby.X", 0.5D);
        config.addDefault("Spawn.Lobby.Y", 65.0D);
        config.addDefault("Spawn.Lobby.Z", 0.5D);
        config.addDefault("Spawn.Lobby.Yaw", 0.0F);
        config.addDefault("Spawn.Lobby.Pitch", 0.F);

        config.addDefault("LobbyItems.Vote.Item", "PAPER");
        config.addDefault("LobbyItems.Vote.Name", "&cScenarios Vote");
        config.addDefault("LobbyItems.Vote.Lore", "&fClick to vote for a scenario");
        config.addDefault("LobbyItems.Vote.Slot", 0);

        config.addDefault("LobbyItems.KitEditor.Item", "BOW");
        config.addDefault("LobbyItems.KitEditor.Name", "&cEdit kit");
        config.addDefault("LobbyItems.KitEditor.Lore", "&fClick to edit your kit");
        config.addDefault("LobbyItems.KitEditor.Slot", 4);

        config.addDefault("LobbyItems.Bed.Item", "BED");
        config.addDefault("LobbyItems.Bed.Name", "&cReturn to the lobby");
        config.addDefault("LobbyItems.Bed.Lore", "&fClick to return to the lobby");
        config.addDefault("LobbyItems.Bed.Slot", 8);



        config.options().copyDefaults(true);
        saveConfig();
    }
    private void loadCommands(){
        getCommand("meetup").setExecutor(new MainCommand());

        CommandExecutor adminCommand = new AdminCommands();
        getCommand("build").setExecutor(adminCommand);
        getCommand("closeinv").setExecutor(adminCommand);

        CommandExecutor userCommands = new UserCommands();
        getCommand("kit").setExecutor(userCommands);
        getCommand("editkit").setExecutor(userCommands);
        getCommand("savekit").setExecutor(userCommands);
        getCommand("stats").setExecutor(userCommands);
        getCommand("vote").setExecutor(userCommands);
        getCommand("start").setExecutor(userCommands);
        getCommand("reroll").setExecutor(userCommands);
    }

    public boolean checkAutoStart(){
        return Bukkit.getOnlinePlayers().size() >= getConfig().getInt("Settings.MinAutoStart");
    }

    public boolean checkForceStart(){
        return Bukkit.getOnlinePlayers().size() >= getConfig().getInt("Settings.MinForceStart");
    }

    public void unregisterListener(Listener listener) {
        System.out.println("[UHC Meetup] Unloading listener" + listener.getClass().getSimpleName());
        HandlerList.unregisterAll(listener);
        System.out.println("[UHC Meetup] " +listener.getClass().getSimpleName() + " has been unloaded!");
    }

    public void registerListener(Listener listener) {
        System.out.println("[UHC Meetup] Loading listener" + listener.getClass().getSimpleName());
        getServer().getPluginManager().registerEvents(listener, this);
        System.out.println("[UHC Meetup] " +listener.getClass().getSimpleName() + " has been loaded!");
    }

    private void loadMySQL() {
        MySQL.host = getConfig().getString("Stats.Ip");
        MySQL.port = getConfig().getInt("Stats.Port");
        MySQL.database = getConfig().getString("Stats.Database");
        MySQL.username = getConfig().getString("Stats.Username");
        MySQL.password = getConfig().getString("Stats.Password");

        MySQL.connect();
        MySQL.update("CREATE TABLE IF NOT EXISTS Meetup_solo_stats (UUID VARCHAR(100), Wins Integer, Kills Integer, Deaths Integer, PlayedTime Integer, MaxKills Integer, GappsEaten Integer)");
        MySQL.update("CREATE TABLE IF NOT EXISTS Meetup_team_stats (UUID VARCHAR(100), Wins Integer, Kills Integer, Deaths Integer, PlayedTime Integer, MaxKills Integer, GappsEaten Integer)");
        MySQL.update("CREATE TABLE IF NOT EXISTS Meetup_bar (UUID VARCHAR(100), Sword Integer, Rod Integer, Bow Integer, Gapples Integer, Gheads Integer, Water_1 Integer, Water_2 Integer, Lava_1 Integer, Lava_2 Integer, Food Integer, Cobblestone Integer, Wood Integer, Axe Integer, Pickaxe Integer, Arrow Integer, Cobweb Integer, SpeedPot Integer, FirePot Integer, Anvil Integer, Enchanting Integer)");
    }

    public enum getStates {
        LOADING, LOBBY, COUNTDOWN,STARTING, INGAME, FINISHED;
        public static getStates state;
    }

    public enum getMode {
        SOLO, TEAM;
        public static getMode state;
    }
}
