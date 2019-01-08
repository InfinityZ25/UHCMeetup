package me.infinity.minigame.meetup.Utils.Player;

import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {
    private Map<UUID, GamePlayer> uuidGamePlayerMap;
    public List<GamePlayer> playersAlive;

    public PlayerManager() {
        this.uuidGamePlayerMap = new HashMap<>();
        this.playersAlive= new ArrayList<>();
    }

    public Map<UUID, GamePlayer> getUuidGamePlayerMap() {
        return uuidGamePlayerMap;
    }


    public int playersAlive(){
        return playersAlive.size();
    }



}
