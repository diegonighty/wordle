package com.github.diegonighty.wordle.game;

import com.github.diegonighty.wordle.game.intent.WordleIntent;

import java.util.List;
import java.util.UUID;

public class WordlePlayer {

	private static final int MAX_PLAYER_INTENTS = 6;

	private final UUID id;
	private UUID currentGame;

	private List<WordleIntent> currentIntents;

	public WordlePlayer(UUID id, UUID currentGame, List<WordleIntent> currentIntents) {
		this.id = id;
		this.currentGame = currentGame;
		this.currentIntents = currentIntents;
	}

	public UUID getId() {
		return id;
	}

	public UUID getCurrentGame() {
		return currentGame;
	}

	public boolean isMaxIntents() {
		return currentIntents.size() >= MAX_PLAYER_INTENTS;
	}

	public void addIntent(WordleIntent intent) {
		currentIntents.add(intent);
	}

	public void setCurrentGame(UUID currentGame) {
		this.currentGame = currentGame;
	}

	public void clearIntents() {
		currentIntents.clear();
	}

	public List<WordleIntent> getCurrentIntents() {
		return currentIntents;
	}
}
