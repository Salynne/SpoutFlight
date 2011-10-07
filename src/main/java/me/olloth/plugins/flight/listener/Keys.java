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

package me.olloth.plugins.flight.listener;

import me.olloth.plugins.flight.SpoutFlight;

import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.getspout.spoutapi.event.input.InputListener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.input.KeyReleasedEvent;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Keys extends InputListener {

	SpoutFlight plugin;
	boolean stopDrifting;

	public Keys(SpoutFlight plugin) {
		this.plugin = plugin;
		stopDrifting = plugin.getConfig().stopDrifting();
	}

	@Override
	public void onKeyPressedEvent(KeyPressedEvent event) {
		SpoutPlayer player = event.getPlayer();
		boolean flightPerm = false;

		if (plugin.getConfig().useOps()) {
			flightPerm = player.isOp();
		}
		
		if (plugin.useOldPerms()) {
			if (plugin.getOldPermissions().has(player, "spoutflight.fly")) {
				flightPerm = true;
			}
		} else if (player.hasPermission("spoutflight.fly")) {
			flightPerm = true;
		}

		if (plugin.getBindMode(player)) {
			plugin.setPlayerBind(player, event.getKey().getKeyCode());
			player.sendMessage("Flight toggle key bound to " + event.getKey().toString());
			plugin.removeBindMode(player);
		}

		else if (event.getScreenType().toString().equals("GAME_SCREEN") && flightPerm) {

			if (event.getKey().equals(Keyboard.getKey(plugin.getPlayerBind(player)))) {
				if (plugin.getPlayerEnabled(player)) {
					plugin.setPlayerEnabled(player, false);
					player.setAirSpeedMultiplier(1);
					player.setGravityMultiplier(plugin.getPlayerGravity(player));
					player.setFallDistance(0);
					if (plugin.getConfig().sendNotifications()) {
						player.sendNotification("SpoutFlight", "Flying disabled!", Material.FEATHER);
					}

				}

				else {
					plugin.setPlayerEnabled(player, true);
					player.setCanFly(true);
					player.setAirSpeedMultiplier(1 * plugin.getPlayerSpeed(player));
					plugin.setPlayerGravity(player, player.getGravityMultiplier());
					player.setGravityMultiplier(0);
					if (plugin.getConfig().sendNotifications()) {
						player.sendNotification("SpoutFlight", "Flying enabled!", Material.FEATHER);
					}

				}

				player.setVelocity(new Vector(0, 0, 0));

			}

			if (plugin.getPlayerEnabled(player)) {

				player.setAirSpeedMultiplier(1 * plugin.getPlayerSpeed(player));

				if (event.getKey().equals(Keyboard.KEY_E())) {
					player.setGravityMultiplier(-0.1 * plugin.getPlayerSpeed(player));
				}

				else if (event.getKey().equals(Keyboard.KEY_Q())) {
					player.setGravityMultiplier(0.1 * plugin.getPlayerSpeed(player));
				}

			}

		}

	}

	@Override
	public void onKeyReleasedEvent(KeyReleasedEvent event) {
		SpoutPlayer player = event.getPlayer();

		if (plugin.getPlayerEnabled(player)) {
			if (event.getKey().equals(player.getJumpKey())) {
				player.setGravityMultiplier(0);
				player.setVelocity(new Vector(0, 0, 0));
			}

			else if (event.getKey().equals(player.getSneakKey())) {
				player.setGravityMultiplier(0);
				player.setVelocity(new Vector(0, 0, 0));
			}

			if (stopDrifting) {
				if (event.getKey().equals(player.getForwardKey())) {
					player.setVelocity(new Vector(0, 0, 0));
				}

				else if (event.getKey().equals(player.getBackwardKey())) {
					player.setVelocity(new Vector(0, 0, 0));
				}

				else if (event.getKey().equals(player.getLeftKey())) {
					player.setVelocity(new Vector(0, 0, 0));
				}

				else if (event.getKey().equals(player.getRightKey())) {
					player.setVelocity(new Vector(0, 0, 0));
				}
			}
		}

	}

}
