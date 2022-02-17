package com.github.diegonighty.wordle.game;

import com.github.diegonighty.wordle.WordlePluginLoader;
import com.github.diegonighty.wordle.concurrent.bukkit.BukkitExecutor;
import com.github.diegonighty.wordle.concurrent.bukkit.BukkitExecutorProvider;
import com.github.diegonighty.wordle.configuration.Configuration;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class GameTaskHandler {

	private final BukkitExecutor executor = BukkitExecutorProvider.get();

	private final Configuration config;
	private final GameService service;

	public GameTaskHandler(WordlePluginLoader loader, GameService service) {
		this.config = new Configuration(loader, "task.yml");
		this.service = service;
	}

	public void createTask() {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of(config.getString("zone-id")));
		ZonedDateTime next = getNextSchedule(now);

		long seconds = Duration.between(now, next).getSeconds();

		executor.scheduleTaskAtFixedRateAsync(service::createGame, seconds);
	}

	private ZonedDateTime getNextSchedule(ZonedDateTime now) {
		ZonedDateTime next = now
				.withHour(
						config.getInt("time-restart.hour")
				)
				.withMinute(
						config.getInt("time-restart.minute")
				).withSecond(
						config.getInt("time-restart.second")
				);

		if (now.compareTo(next) > 0) {
			next = next.plusDays(1);
		}

		return next;
	}

}
