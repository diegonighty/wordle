package com.github.diegonighty.wordle.game;

import java.util.UUID;

public class WordleGame {

	private final UUID id;
	private final String phrase;

	public WordleGame(UUID id, String phrase) {
		this.id = id;
		this.phrase = phrase;

	}

	public UUID getId() {
		return id;
	}

	public String getPhrase() {
		return phrase;
	}

}
