package com.github.diegonighty.wordle.storage;

import com.github.diegonighty.wordle.game.WordleGame;

public interface GameStorage {

	void init();

	WordleGame getGame();

	void updateGame(WordleGame game);

	void loadGame();
}
