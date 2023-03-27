package ch.gabzdev.antivpn.listeners;

import ch.gabzdev.antivpn.Main;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.IOUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PlayerJoinListener implements Listener {

    private final Main plugin;

    public PlayerJoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Obtenir l'adresse IP du joueur
        String ipAddress = player.getAddress().getAddress().getHostAddress();

        // Interrogez IP-API.com pour obtenir des informations sur l'adresse IP
        try {
            URL url = new URL("http://ip-api.com/json/" + ipAddress);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            InputStream inputStream = connection.getInputStream();
            String json = IOUtils.toString(inputStream);
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(json).getAsJsonObject();

            // Vérifiez si l'adresse IP provient d'un VPN, d'un proxy ou d'un réseau mobile
            boolean isVpn = jsonObject.get("proxy").getAsBoolean();
            boolean isProxy = jsonObject.get("proxy").getAsBoolean();
            boolean isMobile = jsonObject.get("mobile").getAsBoolean();
            if (isVpn || isProxy || isMobile) {
                plugin.kickPlayer(player);
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to query IP-API.com: " + e.getMessage());
        }
    }
}
