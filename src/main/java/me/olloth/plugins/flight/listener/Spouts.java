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
