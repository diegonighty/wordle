package com.github.diegonighty.wordle.game;

import com.github.diegonighty.wordle.concurrent.Promise;
import com.github.diegonighty.wordle.game.intent.WordleIntent;
import com.github.diegonighty.wordle.statistic.WordlePlayerStatistic;
import com.github.diegonighty.wordle.storage.GameStorage;
import com.github.diegonighty.wordle.storage.UserStorage;
import com.github.diegonighty.wordle.user.User;
import com.github.diegonighty.wordle.word.WordGeneratorHandler;

import java.util.UUID;

public class GameService {

	private final GameStorage gameStorage;
	private final UserStorage userStorage;

	private final WordGeneratorHandler wordGeneratorHandler;

	public GameService(GameStorage gameStorage, UserStorage userStorage, WordGeneratorHandler wordGeneratorHandler) {
		this.gameStorage = gameStorage;
		this.userStorage = userStorage;
		this.wordGeneratorHandler = wordGeneratorHandler;
	}

	public void setupGame() {
		WordleGame game = getActualGame();
		if (game == null) {
			gameStorage.loadGame();

			WordleGame gameLoaded = getActualGame();
			if (gameLoaded == null) {
				createGame();
			}
		}
	}

	public void createGame() {
		WordleGame wordleGame = new WordleGame(UUID.randomUUID(), wordGeneratorHandler.chooseRandomWord());
		gameStorage.updateGame(wordleGame);
	}

	public boolean testPhrase(User user, WordleGame game, String phrase) {
		WordleIntent intent = WordleIntent.createOf(phrase, game);

		WordlePlayer player = user.getPlayer();
		WordlePlayerStatistic stats = user.statisticOf();

		player.addIntent(intent);

		if (intent.isCorrect()) {
			stats.addWin();
			stats.setWonToday(true);

			if (stats.recordOfTries() > player.getCurrentIntents().size()) {
				stats.setRecordOfTries(player.getCurrentIntents().size());
			}

		} else {
			stats.addFail();
			stats.addBadPosition(intent.badPositions());
		}

		saveCache(user);
		return intent.isCorrect();
	}

	public WordleGame getActualGame() {
		return gameStorage.getGame();
	}

	public User findUserById(UUID id) {
		return userStorage.findById(id);
	}

	public Promise<User> findUserByName(String name) {
		return userStorage.findByName(name);
	}

	public void saveAsync(User user) {
		userStorage.update(user, false);
	}

	public void saveCache(User user) {
		userStorage.update(user, true);
	}

}
