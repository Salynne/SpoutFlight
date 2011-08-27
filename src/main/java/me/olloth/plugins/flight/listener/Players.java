package me.olloth.plugins.flight.listener;

import me.olloth.plugins.flight.SpoutFlight;

import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.getspout.spoutapi.SpoutManager;

public class Players extends PlayerListener {
	SpoutFlight plugin;

	public Players(SpoutFlight plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (!event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName())) {
			plugin.setPlayerEnabled(event.getPlayer(), false);
			plugin.setPlayerFlight(event.getPlayer(), false);
			plugin.setPlayerZFlight(event.getPlayer(), 0);
			SpoutManager.getPlayer(event.getPlayer()).setGravityMultiplier(1);
		}
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.setPlayerEnabled(event.getPlayer(), false);
		plugin.setPlayerFlight(event.getPlayer(), false);
		plugin.setPlayerZFlight(event.getPlayer(), 0);
		SpoutManager.getPlayer(event.getPlayer()).setGravityMultiplier(1);
	}

	@Override
	public void onPlayerLogin(PlayerLoginEvent event) {
		plugin.setPlayerEnabled(event.getPlayer(), false);
		plugin.setPlayerFlight(event.getPlayer(), false);
		plugin.setPlayerZFlight(event.getPlayer(), 0);
		SpoutManager.getPlayer(event.getPlayer()).setGravityMultiplier(1);
	}

	@Override
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		plugin.setPlayerEnabled(event.getPlayer(), false);
		plugin.setPlayerFlight(event.getPlayer(), false);
		plugin.setPlayerZFlight(event.getPlayer(), 0);
		SpoutManager.getPlayer(event.getPlayer()).setGravityMultiplier(1);
	}

	@Override
	public void onPlayerPortal(PlayerPortalEvent event) {
		plugin.setPlayerEnabled(event.getPlayer(), false);
		plugin.setPlayerFlight(event.getPlayer(), false);
		plugin.setPlayerZFlight(event.getPlayer(), 0);
		SpoutManager.getPlayer(event.getPlayer()).setGravityMultiplier(1);
	}

	@Override
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		plugin.setPlayerEnabled(event.getPlayer(), false);
		plugin.setPlayerFlight(event.getPlayer(), false);
		plugin.setPlayerZFlight(event.getPlayer(), 0);
		SpoutManager.getPlayer(event.getPlayer()).setGravityMultiplier(1);
	}
}
