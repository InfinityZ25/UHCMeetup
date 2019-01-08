package me.infinity.minigame.meetup.Utils.Player;

import org.bukkit.entity.Player;

import java.util.UUID;

public class GamePlayer {

    private Player p;
    private UUID uuid;
    private boolean spectator;
    private int wins;
    private int kills;
    private int deaths;
    private int playedTime;
    private int maxKills;
    private int gappsEaten;
    private int local_kills;
    private long startingTime;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    private int points;
    private int[] hotbar;

    public GamePlayer(Player p, UUID uuid, Boolean spectator, int wins, int kills, int deaths, int maxKills, int gappsEaten) {
        this.p = p;
        this.uuid = uuid;
        this.spectator = spectator;
        this.wins = wins;
        this.deaths = deaths;
        this.kills = kills;
        this.maxKills = maxKills;
        this.gappsEaten = gappsEaten;
        this.hotbar = new int[20];

    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void addKills(int number) {
        setKills(getKills() + number);
    }

    public void addDeaths(int number) {
        setDeaths(getDeaths() + number);
    }

    public void addWins(int number) {
        setWins(getWins() + number);
    }

    public int getLKills() {
        return local_kills;
    }

    public void setLKills(int local_kills) {
        this.local_kills = local_kills;
    }

    public void addLKills(int number) {
        setLKills(getLKills() + number);
    }

    public int getPlayedTime() {
        return playedTime;
    }

    public int getMaxKills() {
        return maxKills;
    }

    public void setMaxKills(int maxKills) {
        this.maxKills = maxKills;
    }

    public void setPlayedTime(int playedTime) {
        this.playedTime = playedTime;
    }
    public int getGappsEaten() {
        return gappsEaten;
    }

    public void setGappsEaten(int gappsEaten) {
        this.gappsEaten = gappsEaten;
    }
    public void addGappsEaten(int gappsEaten) {
        this.gappsEaten = gappsEaten + getGappsEaten();
    }

    public void addPlayedTime(int playedTime) {
        setPlayedTime(getPlayedTime() + playedTime);
    }
    public long getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(long startingTime) {
        this.startingTime = startingTime;
    }

    public boolean isSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer() {
        return p;
    }

    public int[] getHotbar() {
        return hotbar;
    }

    public void setHotbar(int[] hotbar) {
        this.hotbar = hotbar;
    }


}
