package me.infinity.minigame.meetup.Utils.Scenario;

import org.bukkit.event.Listener;

public class Scenario {

    private Listener listener;
    private String name;

    public int getPoints() {
        return points;
    }

    public void add(int points){
        this.points = points + getPoints();
    }

    public void setPoints(int points) {
        this.points = points;
    }

    private int points;

    public Scenario(Listener listener, String name){
        this.listener = listener;
        this.name = name;
        points = 0;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
