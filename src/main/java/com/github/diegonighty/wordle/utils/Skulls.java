package com.github.diegonighty.wordle.utils;

import org.bukkit.Material;

import static team.unnamed.gui.core.version.ServerVersionProvider.SERVER_VERSION_INT;

public class Skulls {

	public static final Material MATERIAL;

	static {
		if (SERVER_VERSION_INT < 13) {
			MATERIAL = Material.SKULL_ITEM;
		} else {
			MATERIAL = Material.valueOf("PLAYER_HEAD");
		}
	}

}
