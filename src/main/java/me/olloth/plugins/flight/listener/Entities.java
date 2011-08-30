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
			boolean fallDmgPerm;

			if (plugin.useOldPerms()) {
				fallDmgPerm = plugin.getOldPermissions().has(player, "spoutflight.nofalldmg");
			} else {
				fallDmgPerm = player.hasPermission("spoutflight.nofalldmg");
			}

			if (plugin.getPlayerEnabled(player) || fallDmgPerm) {
				event.setCancelled(true);
			}
		}
	}

}
