package me.infinity.minigame.meetup.Database;

import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;

public interface PlayerDataInterface {

    void loadPlayer(Player p, Boolean bol) throws IOException, SQLException;

    void savePlayer(Player p);

    void savePlayerInventory(Player p);

    void loadPlayerInventory(Player p) throws SQLException;

    void updateStats(Player p) throws SQLException;

}
