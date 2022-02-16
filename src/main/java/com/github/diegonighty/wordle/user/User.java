package com.github.diegonighty.wordle.user;

import com.github.diegonighty.wordle.game.WordlePlayer;
import com.github.diegonighty.wordle.statistic.WordlePlayerStatistic;

import java.util.UUID;

public class User {

	private final UUID id;
	private final String name;

	private final WordlePlayerStatistic stats;
	private final WordlePlayer player;

	public User(UUID id, String name, WordlePlayerStatistic stats, WordlePlayer player) {
		this.id = id;
		this.name = name;
		this.stats = stats;
		this.player = player;
	}

	public String getName() {
		return name;
	}

	public UUID getId() {
		return id;
	}

	public WordlePlayer getPlayer() {
		return player;
	}

	public WordlePlayerStatistic statisticOf() {
		return stats;
	}
}
