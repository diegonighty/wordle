package com.github.diegonighty.wordle.utils;

import com.github.diegonighty.wordle.LoggerProvider;
import org.bukkit.Material;

import java.util.logging.Logger;

public class SafeMaterial {

	private final static Logger LOGGER = LoggerProvider.get();

	public static Material from(String name) {
		Material material = Material.matchMaterial(name);

		if (material == null) {
			LOGGER.warning("Getting invalid material, please configure it correctly");
			return Material.STONE;
		}

		return material;
	}

}
