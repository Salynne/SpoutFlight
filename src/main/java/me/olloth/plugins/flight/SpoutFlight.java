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

import me.olloth.plugins.flight.keys.MovementKey;
import me.olloth.plugins.flight.keys.ToggleKey;
import me.olloth.plugins.flight.keys.UpDownKey;
import me.olloth.plugins.flight.listener.Players;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.keyboard.KeyBindingManager;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.plugin.SpoutPlugin;

public class SpoutFlight extends SpoutPlugin {

	public static final Logger log = Logger.getLogger("Minecraft");
	public static String PREFIX = "[SpoutFlight] ";

	private PluginDescriptionFile info;

	public Players players;

	private Config config;

	final private Map<String, Boolean> enabled = new HashMap<String, Boolean>();
	final private Map<String, Double> gravitys = new HashMap<String, Double>();
	final private Map<String, Integer> speeds = new HashMap<String, Integer>();

	public void onDisable() {
		config.saveMaps();
		log.log(Level.INFO, PREFIX + "is now disabled.");
	}

	public void onEnable() {
		info = getDescription();

		config = new Config(this);

		for (Player player : getServer().getOnlinePlayers()) {
			enabled.put(player.getName(), false);
			speeds.put(player.getName(), config.getDefaultSpeed());
		}

		players = new Players(this);

		KeyBindingManager kbm = SpoutManager.getKeyBindingManager();

		try {
			kbm.registerBinding("SpoutFlight.Toggle", Keyboard.KEY_LCONTROL, "The key to toggle flying on/off", new ToggleKey(this), this);
			kbm.registerBinding("SpoutFlight.Up", Keyboard.KEY_SPACE, "The key to fly up", new UpDownKey(this, -0.1F), this);
			kbm.registerBinding("SpoutFlight.Down", Keyboard.KEY_LSHIFT, "The key to fly down", new UpDownKey(this, 0.1F), this);
			kbm.registerBinding("SpoutFlight.Forward", Keyboard.KEY_W, "The key to move forward", new MovementKey(this), this);
			kbm.registerBinding("SpoutFlight.Backward", Keyboard.KEY_S, "The key to move backward", new MovementKey(this), this);
			kbm.registerBinding("SpoutFlight.Left", Keyboard.KEY_A, "The key to move left", new MovementKey(this), this);
			kbm.registerBinding("SpoutFlight.Right", Keyboard.KEY_D, "The key to move right", new MovementKey(this), this);

		} catch (Exception e) {
			e.printStackTrace();
		}

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

		return commandSuccess;
	}

	public void setPlayerEnabled(Player player, boolean enable) {
		if (enabled == null) {
			log.severe("SpoutFlight: setPlayerEnabled, enabled map is null");
			return;
		}

		if (player == null) {
			log.warning("SpoutFlight: setPlayerEnabled, player is null");
			return;
		}

		enabled.put(player.getName(), enable);

	}

	public boolean getPlayerEnabled(Player player) {
		if (enabled == null) {
			log.severe("SpoutFlight: getPlayerEnabled, enabled map is null");
			return false;
		}

		if (player == null) {
			log.warning("SpoutFlight: getPlayerEnabled, player is null");
			return false;
		}

		if (!enabled.containsKey(player.getName())) {
			setPlayerEnabled(player, false);
		}

		return enabled.get(player.getName());
	}

	public void setPlayerSpeed(Player player, int speed) {
		speeds.put(player.getName(), speed);
		SpoutManager.getPlayer(player).setAirSpeedMultiplier(1 * speed);
	}

	public int getPlayerSpeed(Player player) {
		if (!speeds.containsKey(player.getName())) {
			setPlayerSpeed(player, config.getDefaultSpeed());
		}

		return speeds.get(player.getName());
	}

	public void setPlayerGravity(Player player, double gravity) {
		gravitys.put(player.getName(), gravity);
	}

	public double getPlayerGravity(Player player) {
		if (!gravitys.containsKey(player.getName())) {
			setPlayerGravity(player, config.getDefaultGravity());
		}

		return gravitys.get(player.getName());
	}

	public void setEnabledMap(Map<String, Boolean> map) {
		enabled.clear();
		if (map != null)
			enabled.putAll(map);
	}

	public Map<String, Boolean> getEnabledMap() {
		return enabled;
	}

	public void setGravityMap(Map<String, Double> map) {
		gravitys.clear();
		if (map != null)
			gravitys.putAll(map);
	}

	public Map<String, Double> getGravityMap() {
		return gravitys;
	}

	public Config getSFConfig() {
		return config;
	}

}