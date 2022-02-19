package com.github.diegonighty.wordle.concurrent.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BukkitExecutor implements Executor {

	private final Plugin plugin;
	private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	public BukkitExecutor(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void execute(@NotNull Runnable command) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, command);
	}

	public void scheduleTaskAtFixedRateAsync(Runnable command, long secondsDelay, long period) {
		scheduledExecutorService.scheduleAtFixedRate(
				command,
				secondsDelay,
				period,
				TimeUnit.SECONDS
		);
	}

	public void executeTaskWithDelay(Runnable command, long ticks) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, command, ticks);
	}

}