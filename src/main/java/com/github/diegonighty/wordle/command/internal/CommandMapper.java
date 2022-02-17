package com.github.diegonighty.wordle.command.internal;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

public class CommandMapper {

	private final static Field COMMAND_MAP_FIELD;
	private final static CommandMap COMMAND_MAP;
	private final static String FALLBACK_PREFIX = "wordle";

	static {
		try {
			COMMAND_MAP_FIELD = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			COMMAND_MAP_FIELD.setAccessible(true);

			COMMAND_MAP = (CommandMap) COMMAND_MAP_FIELD.get(Bukkit.getServer());
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException("CommandMap not found in Bukkit Server while loading Wordle plugin");
		}
	}

	public static void register(Command command) {
		COMMAND_MAP.register(FALLBACK_PREFIX, command);
	}

}
