package me.olloth.plugins.flight;

import java.io.File;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	private File enableMap;
	private File gravityMap;
	private File directory;

	SpoutFlight plugin;
	FileConfiguration config;

	public Config(SpoutFlight plugin) {
		this.plugin = plugin;
		this.plugin.getConfig().options().copyDefaults(true);

		if (!this.plugin.getConfigFile().exists())
			this.plugin.saveConfig();

		this.config = this.plugin.getConfig();
		
		loadMaps();
	}

	public int getDefaultSpeed() {
		int speed = config.getInt("default_speed", 5);
		if (speed < 1) {
			speed = 1;
		} else if (speed > getMaxSpeed()) {
			speed = getMaxSpeed();
		}

		return speed;
	}

	public double getDefaultGravity() {
		double gravity = config.getDouble("default_gravity", 1);
		if (gravity < 0) {
			gravity = 0.1D;
		} else if (gravity > getMaxGravity()) {
			gravity = getMaxGravity();
		}

		return gravity;
	}

	public boolean useOps() {
		return config.getBoolean("useOps", true);
	}

	public boolean stopDrifting() {
		return config.getBoolean("stopDrifting", true);
	}

	public int getMaxSpeed() {
		return config.getInt("maxSpeed", 10);
	}

	public double getMaxGravity() {
		return config.getDouble("maxGravity", 2);
	}

	public boolean sendNotifications() {
		return config.getBoolean("sendNotifications", true);
	}

	@SuppressWarnings("unchecked")
	public void loadMaps() {
		enableMap = new File(directory, "flying.bin");

		gravityMap = new File(directory, "gravity.bin");
		if (!enableMap.exists()) {
			saveMap(plugin.getEnabledMap(), enableMap.getPath());
		} else {
			plugin.setEnabledMap((Map<String, Boolean>) HMapSL.load(enableMap.getPath()));
		}

		if (!gravityMap.exists()) {
			saveMap(plugin.getGravityMap(), gravityMap.getPath());
		} else {
			plugin.setGravityMap((Map<String, Double>) HMapSL.load(gravityMap.getPath()));
		}
	}

	public void saveMap(Map<String, ?> map, String path) {
		HMapSL.save(map, path);
	}

	public void saveMaps() {
		saveMap(plugin.getEnabledMap(), enableMap.getPath());
		saveMap(plugin.getGravityMap(), gravityMap.getPath());
	}
}
