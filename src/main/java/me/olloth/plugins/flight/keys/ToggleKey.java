package me.olloth.plugins.flight.keys;

import me.olloth.plugins.flight.SpoutFlight;

import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.player.SpoutPlayer;

public class ToggleKey implements BindingExecutionDelegate {
	SpoutFlight plugin;

	public ToggleKey(SpoutFlight plugin) {
		this.plugin = plugin;
	}

	@Override
	public void keyPressed(KeyBindingEvent event) {
	}

	@Override
	public void keyReleased(KeyBindingEvent event) {
		SpoutPlayer player = event.getPlayer();
		boolean flightPerm = false;

		if (plugin.getSFConfig().useOps()) {
			flightPerm = player.isOp();
		}

		if (plugin.useOldPerms()) {
			if (plugin.getOldPermissions().has(player, "spoutflight.fly")) {
				flightPerm = true;
			}
		} else if (player.hasPermission("spoutflight.fly")) {
			flightPerm = true;
		}

		else if (event.getScreenType().toString().equals("GAME_SCREEN") && flightPerm) {

			if (plugin.getPlayerEnabled(player)) {
				plugin.setPlayerEnabled(player, false);
				player.setAirSpeedMultiplier(1);
				player.setGravityMultiplier(plugin.getPlayerGravity(player));
				player.setFallDistance(0);
				if (plugin.getSFConfig().sendNotifications()) {
					player.sendNotification("SpoutFlight", "Flying disabled!", Material.FEATHER);
				}
			}

			else {
				plugin.setPlayerEnabled(player, true);
				player.setCanFly(true);
				player.setAirSpeedMultiplier(1 * plugin.getPlayerSpeed(player));
				plugin.setPlayerGravity(player, player.getGravityMultiplier());
				player.setGravityMultiplier(0);
				if (plugin.getSFConfig().sendNotifications()) {
					player.sendNotification("SpoutFlight", "Flying enabled!", Material.FEATHER);
				}
			}

			player.setVelocity(new Vector(0, 0, 0));
		}
	}
}
