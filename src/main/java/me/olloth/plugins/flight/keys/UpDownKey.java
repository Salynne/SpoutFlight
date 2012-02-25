package me.olloth.plugins.flight.keys;

import me.olloth.plugins.flight.SpoutFlight;

import org.bukkit.util.Vector;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;

public class UpDownKey implements BindingExecutionDelegate {

	SpoutFlight plugin;
	float direction;

	public UpDownKey(SpoutFlight plugin, float direction) {
		this.plugin = plugin;
		this.direction = direction;
	}

	@Override
	public void keyPressed(KeyBindingEvent event) {
		if (event.getScreenType().equals(ScreenType.GAME_SCREEN) && plugin.getPlayerEnabled(event.getPlayer())) {
			event.getPlayer().setGravityMultiplier(direction * plugin.getPlayerSpeed(event.getPlayer()));
		}
	}

	@Override
	public void keyReleased(KeyBindingEvent event) {
		if (event.getScreenType().equals(ScreenType.GAME_SCREEN) && plugin.getPlayerEnabled(event.getPlayer())) {
			event.getPlayer().setGravityMultiplier(0);
			event.getPlayer().setVelocity(new Vector(0, 0, 0));
		}
	}
}