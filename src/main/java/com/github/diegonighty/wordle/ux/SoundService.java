package com.github.diegonighty.wordle.ux;

import com.github.diegonighty.wordle.LoggerProvider;
import com.github.diegonighty.wordle.configuration.Configuration;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class SoundService {

	private final static Logger LOGGER = LoggerProvider.get();

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
				getSoundOrWarning(section.getString("sound")),
				section.getInt("volume"),
				section.getInt("pitch")
		);
	}

	private Sound getSoundOrWarning(String sound) {
		try {
			return Sound.valueOf(sound);
		} catch (IllegalArgumentException ignored) {
			LOGGER.warning("Getting invalid sound!, PLEASE configure the sound correctly in sounds.yml");

			return Sound.values()[0];
		}
	}

}
