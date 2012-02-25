package me.olloth.plugins.flight.keys;

import me.olloth.plugins.flight.SpoutFlight;

import org.bukkit.util.Vector;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;

public class MovementKey implements BindingExecutionDelegate {

	SpoutFlight plugin;
	boolean stopDrifting;

	public MovementKey(SpoutFlight plugin) {
		this.plugin = plugin;
		stopDrifting = plugin.getSFConfig().stopDrifting();
	}

	@Override
	public void keyPressed(KeyBindingEvent event) {
	}

	@Override
	public void keyReleased(KeyBindingEvent event) {
		if (event.getScreenType().equals(ScreenType.GAME_SCREEN) && plugin.getPlayerEnabled(event.getPlayer()) && stopDrifting) {
			event.getPlayer().setVelocity(new Vector(0, 0, 0));
		}
	}
}
