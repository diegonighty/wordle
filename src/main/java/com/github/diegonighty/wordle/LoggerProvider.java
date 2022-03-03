package com.github.diegonighty.wordle;

import com.github.diegonighty.wordle.concurrent.bukkit.BukkitExecutorProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class LoggerProvider {

	private static Logger logger;

	public static synchronized Logger get() {
		if (logger == null) {
			logger = JavaPlugin.getProvidingPlugin(BukkitExecutorProvider.class).getLogger();
		}

		return logger;
	}

}
