package me.olloth.plugins.flight;

import java.io.File;
import java.util.Map;

import org.bukkit.util.config.Configuration;

public class Config {
	private File enableMap;
	private File bindsMap;
	private File directory;
	private File configFile;
	private Configuration config;

	SpoutFlight plugin;

	public Config(SpoutFlight plugin) {
		this.plugin = plugin;
		init();
	}

	public void init() {
		directory = plugin.getDataFolder();
		configFile = new File(directory, "config.yml");
		if (!directory.exists())
			directory.mkdir();
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		load();

		loadMaps();
	}

	public void load() {
		config = new Configuration(configFile);
		config.load();
		
		getDefaultSpeed();
		
		config.save();

	}

	public int getDefaultSpeed() {
		int speed = config.getInt("default_speed", 5);
		if (speed < 1) {
			speed = 1;
		} else if (speed > 20) {
			speed = 10;
		}

		return speed;
	}

	@SuppressWarnings("unchecked")
	public void loadMaps() {
		enableMap = new File(directory, "flying.bin");
		bindsMap = new File(directory, "binds.bin");
		if (!enableMap.exists()) {
			saveMap(plugin.getEnabledMap(), enableMap.getPath());
		} else {
			plugin.setEnabledMap((Map<String, Boolean>) HMapSL.load(enableMap.getPath()));
		}

		if (!bindsMap.exists()) {
			saveMap(plugin.getBindsMap(), bindsMap.getPath());
		} else {
			plugin.setBindsMap((Map<String, Integer>) HMapSL.load(bindsMap.getPath()));
		}
	}

	public void saveMap(Map<String, ?> map, String path) {
		HMapSL.save(map, path);

	}

	public void saveMaps() {
		saveMap(plugin.getEnabledMap(), enableMap.getPath());
		saveMap(plugin.getBindsMap(), bindsMap.getPath());

	}
}
