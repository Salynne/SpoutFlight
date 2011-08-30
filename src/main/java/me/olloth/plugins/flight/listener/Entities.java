package me.olloth.plugins.flight.listener;

import me.olloth.plugins.flight.SpoutFlight;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;

public class Entities extends EntityListener {

	SpoutFlight plugin;

	public Entities(SpoutFlight plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getCause().equals(DamageCause.FALL) && event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (plugin.getPlayerEnabled(player) || player.hasPermission("spoutflight.nofalldmg")) {
				event.setCancelled(true);
			}
		}
	}

}
