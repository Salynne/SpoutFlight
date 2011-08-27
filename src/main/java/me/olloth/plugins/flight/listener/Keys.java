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

import org.getspout.spoutapi.event.input.InputListener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.input.KeyReleasedEvent;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Keys extends InputListener {

	SpoutFlight plugin;

	public Keys(SpoutFlight plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onKeyPressedEvent(KeyPressedEvent event) {

		if (event.getScreenType().toString().equals("GAME_SCREEN") && event.getPlayer().hasPermission("spoutflight.fly")) {

			SpoutPlayer player = event.getPlayer();

			if (event.getKey().equals(Keyboard.KEY_LCONTROL)) {
				if (plugin.getPlayerEnabled(player)) {
					plugin.setPlayerEnabled(player, false);
					plugin.setPlayerFlight(event.getPlayer(), false);
					plugin.setPlayerZFlight(event.getPlayer(), 0);
					player.setGravityMultiplier(1);
					player.setCanFly(false);
				} else {
					plugin.setPlayerEnabled(player, true);
					player.setCanFly(true);
					player.setGravityMultiplier(0);
				}
			}

			if (event.getKey().equals(player.getForwardKey()) && plugin.getPlayerEnabled(player)) {
				plugin.setPlayerFlight(player, true);

			} else if (event.getKey().equals(player.getJumpKey()) && plugin.getPlayerEnabled(player)) {
				plugin.setPlayerZFlight(player, 1);
			} else if (event.getKey().equals(player.getSneakKey()) && plugin.getPlayerEnabled(player)) {
				plugin.setPlayerZFlight(player, -1);
			}
		}

	}

	@Override
	public void onKeyReleasedEvent(KeyReleasedEvent event) {
		if (event.getKey().equals(event.getPlayer().getForwardKey())) {
			plugin.setPlayerFlight(event.getPlayer(), false);
		} else if (event.getKey().equals(event.getPlayer().getJumpKey())) {
			plugin.setPlayerZFlight(event.getPlayer(), 0);
		} else if (event.getKey().equals(event.getPlayer().getSneakKey())) {
			plugin.setPlayerZFlight(event.getPlayer(), -0);
		}
	}

}
