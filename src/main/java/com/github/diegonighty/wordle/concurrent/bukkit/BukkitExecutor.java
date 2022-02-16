package com.github.diegonighty.wordle.concurrent.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class BukkitExecutor implements Executor {

	private final Plugin plugin;

	public BukkitExecutor(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void execute(@NotNull Runnable command) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, command);
	}

	public void scheduleTask(Runnable command, int ticksDelay) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, command, ticksDelay);
	}

}