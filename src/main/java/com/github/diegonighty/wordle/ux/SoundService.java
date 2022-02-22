package com.github.diegonighty.wordle.ux;

import com.github.diegonighty.wordle.configuration.Configuration;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SoundService {

	private final Configuration configuration;

	public SoundService(Plugin plugin) {
		this.configuration = new Configuration(plugin, "sounds.yml");
	}

	public void play(Player player, WordleSound sound) {
		ConfigurationSection section = configuration.getConfigurationSection(sound.path());

		if (!section.getBoolean("enable")) {
			return;
		}

		player.playSound(
				player.getEyeLocation(),
				Sound.valueOf(section.getString("sound")),
				section.getInt("volume"),
				section.getInt("pitch")
		);
	}

}
