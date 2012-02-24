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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;

public class Players implements Listener {
	SpoutFlight plugin;

	public Players(SpoutFlight plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (!event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName())) {
			plugin.setPlayerEnabled(event.getPlayer(), false);
			SpoutManager.getPlayer(event.getPlayer()).setGravityMultiplier(1);
			SpoutManager.getPlayer(event.getPlayer()).setAirSpeedMultiplier(1);
		}
	}

	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		plugin.setPlayerEnabled(event.getPlayer(), false);
		SpoutManager.getPlayer(event.getPlayer()).setGravityMultiplier(1);
		SpoutManager.getPlayer(event.getPlayer()).setAirSpeedMultiplier(1);
	}

	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent event) {
		plugin.setPlayerEnabled(event.getPlayer(), false);
		SpoutManager.getPlayer(event.getPlayer()).setGravityMultiplier(1);
		SpoutManager.getPlayer(event.getPlayer()).setAirSpeedMultiplier(1);
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		plugin.setPlayerEnabled(event.getPlayer(), false);
		SpoutManager.getPlayer(event.getPlayer()).setGravityMultiplier(1);
		SpoutManager.getPlayer(event.getPlayer()).setAirSpeedMultiplier(1);
	}

	@EventHandler
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {
		if (event.getPlayer() == null)
			return;

		if (plugin.getPlayerEnabled(event.getPlayer())) {
			event.getPlayer().setCanFly(true);
			event.getPlayer().setGravityMultiplier(0);
			event.getPlayer().setAirSpeedMultiplier(1 * plugin.getPlayerSpeed(event.getPlayer()));
		}

	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getCause().equals(DamageCause.FALL) && event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			if (plugin.getPlayerEnabled(player) || player.hasPermission("spoutflight.nofalldmg")) {
				event.setCancelled(true);
			}
		}
	}
}
