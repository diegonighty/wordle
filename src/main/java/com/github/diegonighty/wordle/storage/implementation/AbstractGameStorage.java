package com.github.diegonighty.wordle.storage.implementation;

import com.github.diegonighty.wordle.concurrent.Promise;
import com.github.diegonighty.wordle.game.WordleGame;
import com.github.diegonighty.wordle.storage.GameStorage;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractGameStorage implements GameStorage {

	private final AtomicReference<WordleGame> actualGame = new AtomicReference<>();

	@Override
	public WordleGame getGame() {
		return actualGame.get();
	}

	@Override
	public void updateGame(WordleGame game) {
		deleteGames();
		updateGameInStorage(game);

		actualGame.set(game);
	}

	@Override
	public Promise<WordleGame> loadGame() {
		return Promise.supplyAsync(() -> {
			actualGame.set(getGameInStorage());

			return actualGame.get();
		});
	}

	protected abstract @Nullable WordleGame getGameInStorage();
	protected abstract void updateGameInStorage(WordleGame game);
	protected abstract void deleteGames();
}
