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

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.getspout.spoutapi.event.spout.ServerTickEvent;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.event.spout.SpoutListener;

public class Spouts extends SpoutListener {
	SpoutFlight plugin;

	public Spouts(SpoutFlight plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {
		plugin.setPlayerEnabled(event.getPlayer(), false);
		plugin.setPlayerFlight(event.getPlayer(), false);
		plugin.setPlayerZFlight(event.getPlayer(), 0);
		event.getPlayer().setGravityMultiplier(1);

	}

	@Override
	public void onServerTick(ServerTickEvent event) {

		for (Player player : plugin.getServer().getOnlinePlayers()) {
			Vector vector = new Vector(0, 0, 0);
			if (plugin.getPlayerFlight(player) || plugin.getPlayerZFlight(player) != 0) {
				player.setFallDistance(0);

				if (plugin.getPlayerFlight(player)) {
					vector = player.getLocation().getDirection();
				}
				if (plugin.getPlayerZFlight(player) != 0) {
					vector.setY(vector.getY() + plugin.getPlayerZFlight(player));
				}

				player.setVelocity(vector);
			}

		}
	}

}
