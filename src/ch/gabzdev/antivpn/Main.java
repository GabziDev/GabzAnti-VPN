package ch.gabzdev.antivpn;

import ch.gabzdev.antivpn.listeners.PlayerJoinListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin {

    private String kickMessage;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        kickMessage = getConfig().getString("kickMessage");

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    @Override
    public void onDisable() {
    }

    public void kickPlayer(Player player) {
        player.kickPlayer(ChatColor.AQUA + kickMessage);
    }
}
