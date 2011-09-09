/*
 * This file is part of SpoutFlight (https://github.com/Olloth/SpoutFlight).
 * 
 * SpoutFlight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.olloth.plugins.flight;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.olloth.plugins.flight.listener.Entities;
import me.olloth.plugins.flight.listener.Keys;
import me.olloth.plugins.flight.listener.Players;
import me.olloth.plugins.flight.listener.Spouts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class SpoutFlight extends JavaPlugin {

	public static final Logger log = Logger.getLogger("Minecraft");
	public static String PREFIX = "[SpoutFlight] ";

	private PluginDescriptionFile info;
	private PluginManager pm;

	private Plugin oldPermissions;
	private PermissionHandler permissionHandler;
	private boolean useOldPerms;

	private Players players;
	private Spouts spouts;
	private Keys keys;
	private Entities entities;

	private Config config;

	private Map<String, Boolean> enabled;
	private Map<String, Integer> speeds;
	private Map<String, Integer> binds;
	private Map<String, Boolean> bindMode;

	public void onDisable() {
		if (SpoutManager.getPlayerManager().getOnlinePlayers() != null) {
			for (SpoutPlayer player : SpoutManager.getPlayerManager().getOnlinePlayers()) {
				player.setGravityMultiplier(1);
				player.setAirSpeedMultiplier(1);
			}
		}

		config.saveMaps();
		log.log(Level.INFO, PREFIX + "is now disabled.");
	}

	public void onEnable() {
		info = getDescription();
		pm = getServer().getPluginManager();

		enabled = new HashMap<String, Boolean>();
		speeds = new HashMap<String, Integer>();
		binds = new HashMap<String, Integer>();
		bindMode = new HashMap<String, Boolean>();
		config = new Config(this);

		useOldPerms = false;

		for (Player player : getServer().getOnlinePlayers()) {
			enabled.put(player.getName(), false);
			speeds.put(player.getName(), config.getDefaultSpeed());
		}

		players = new Players(this);
		pm.registerEvent(Type.PLAYER_TELEPORT, players, Priority.Low, this);
		pm.registerEvent(Type.PLAYER_BED_ENTER, players, Priority.Low, this);
		pm.registerEvent(Type.PLAYER_PORTAL, players, Priority.Low, this);
		pm.registerEvent(Type.PLAYER_RESPAWN, players, Priority.Low, this);

		entities = new Entities(this);
		pm.registerEvent(Type.ENTITY_DAMAGE, entities, Priority.Low, this);

		spouts = new Spouts(this);
		pm.registerEvent(Type.CUSTOM_EVENT, spouts, Priority.Low, this);

		keys = new Keys(this);
		pm.registerEvent(Type.CUSTOM_EVENT, keys, Priority.Low, this);

		oldPermissions = this.getServer().getPluginManager().getPlugin("Permissions");

		if (oldPermissions != null && config.useOldPermissions()) {
			useOldPerms = true;
			permissionHandler = ((Permissions) oldPermissions).getHandler();
			log.log(Level.INFO, PREFIX + "Found and will use plugin " + oldPermissions.getDescription().getFullName());
			log.log(Level.INFO, PREFIX + "version " + info.getVersion() + " is now enabled.");
			return;
		}

		log.log(Level.INFO, PREFIX + "Using Bukkit SuperPerms");
		log.log(Level.INFO, PREFIX + "version " + info.getVersion() + " is now enabled.");
	}

	/**
	 * Calls when a player uses a command.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String commandName = command.getName().toLowerCase();
		boolean commandSuccess = false;

		// Only Players
		if (!(sender instanceof Player)) {
			sender.sendMessage(PREFIX + "/" + commandName + " can only be run from in game.");
			commandSuccess = true;
		}

		else if (commandName.equals("sfspeed")) {
			Player player = (Player) sender;

			if (args.length == 0) {
				player.sendMessage("Your current speed is " + getPlayerSpeed(player));
				commandSuccess = true;

			} else {

				try {
					int speed = Integer.parseInt(args[0]);
					if (speed < 1) {
						speed = 1;
					} else if (speed > config.getMaxSpeed()) {
						speed = config.getMaxSpeed();
					}
					setPlayerSpeed(player, speed);
					player.sendMessage("Your current speed is " + speed);

					commandSuccess = true;
				} catch (NumberFormatException e) {
					commandSuccess = false;
				}

			}

		}

		if (commandName.equals("sfbind")) {
			Player player = (Player) sender;
			bindMode.put(player.getName(), true);
			sender.sendMessage("Press any key to bind it to the flight toggle key.");

			commandSuccess = true;
		}

		return commandSuccess;
	}

	public void setPlayerEnabled(Player player, boolean enable) {
		enabled.put(player.getName(), enable);
	}

	public boolean getPlayerEnabled(Player player) {
		if (!enabled.containsKey(player.getName())) {
			setPlayerEnabled(player, false);
		}
		return enabled.get(player.getName());
	}

	public void setPlayerSpeed(Player player, int speed) {
		speeds.put(player.getName(), speed);
	}

	public int getPlayerSpeed(Player player) {
		if (!speeds.containsKey(player.getName())) {
			setPlayerSpeed(player, config.getDefaultSpeed());
		}

		return speeds.get(player.getName());
	}

	public void setPlayerBind(Player player, int bind) {
		binds.put(player.getName(), bind);
	}

	public int getPlayerBind(Player player) {
		if (!binds.containsKey(player.getName())) {
			setPlayerBind(player, Keyboard.KEY_LCONTROL.getKeyCode());
		}
		return binds.get(player.getName());
	}

	public boolean getBindMode(Player player) {
		boolean mode = false;

		if (bindMode.containsKey(player.getName())) {
			mode = bindMode.get(player.getName());
		}

		return mode;
	}

	public void removeBindMode(Player player) {
		if (bindMode.containsKey(player.getName())) {
			bindMode.remove(player.getName());
		}
	}

	public void setEnabledMap(Map<String, Boolean> map) {
		enabled = map;
	}

	public Map<String, Boolean> getEnabledMap() {
		return enabled;
	}

	public void setBindsMap(Map<String, Integer> map) {
		binds = map;
	}

	public Map<String, Integer> getBindsMap() {
		return binds;
	}

	public Config getConfig() {
		return config;
	}

	public PermissionHandler getOldPermissions() {
		return permissionHandler;
	}

	public boolean useOldPerms() {
		return useOldPerms;
	}
}
