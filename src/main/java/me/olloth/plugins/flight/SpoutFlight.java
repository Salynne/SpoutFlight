package me.olloth.plugins.flight;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.olloth.plugins.flight.listener.Keys;
import me.olloth.plugins.flight.listener.Players;
import me.olloth.plugins.flight.listener.Spouts;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SpoutFlight extends JavaPlugin {

	public static final Logger log = Logger.getLogger("Minecraft");
	public static String PREFIX = "[Flight] ";

	private PluginDescriptionFile info;
	private PluginManager pm;

	private Players players;
	private Spouts spouts;
	private Keys keys;

	private static Map<String, Boolean> ENABLED;
	private static Map<String, Boolean> FLIGHT;
	private static Map<String, Integer> ZFLIGHT;

	public void onDisable() {
		ENABLED.clear();
		FLIGHT.clear();
		log.log(Level.INFO, PREFIX + "is now disabled.");
	}

	public void onEnable() {
		info = getDescription();
		pm = getServer().getPluginManager();

		ENABLED = new HashMap<String, Boolean>();
		FLIGHT = new HashMap<String, Boolean>();
		ZFLIGHT = new HashMap<String, Integer>();

		for (Player player : getServer().getOnlinePlayers()) {
			ENABLED.put(player.getName(), false);
			FLIGHT.put(player.getName(), false);
			ZFLIGHT.put(player.getName(), 0);
		}

		players = new Players(this);
		pm.registerEvent(Type.PLAYER_JOIN, players, Priority.Low, this);
		pm.registerEvent(Type.PLAYER_TELEPORT, players, Priority.Low, this);
		pm.registerEvent(Type.PLAYER_BED_ENTER, players, Priority.Low, this);
		pm.registerEvent(Type.PLAYER_PORTAL, players, Priority.Low, this);
		pm.registerEvent(Type.PLAYER_RESPAWN, players, Priority.Low, this);

		spouts = new Spouts(this);
		pm.registerEvent(Type.CUSTOM_EVENT, spouts, Priority.Low, this);

		keys = new Keys(this);
		pm.registerEvent(Type.CUSTOM_EVENT, keys, Priority.Low, this);

		log.log(Level.INFO, PREFIX + "version " + info.getVersion() + " is now enabled.");
	}

	public void setPlayerEnabled(Player player, boolean enable) {
		ENABLED.put(player.getName(), enable);
	}

	public boolean getPlayerEnabled(Player player) {
		return ENABLED.get(player.getName());
	}

	public void setPlayerFlight(Player player, boolean enable) {
		FLIGHT.put(player.getName(), enable);
	}

	public boolean getPlayerFlight(Player player) {
		return FLIGHT.get(player.getName());
	}
	
	public void setPlayerZFlight(Player player, int direction) {
		ZFLIGHT.put(player.getName(), direction);
	}
	
	public int getPlayerZFlight(Player player) {
		return ZFLIGHT.get(player.getName());
	}

}
