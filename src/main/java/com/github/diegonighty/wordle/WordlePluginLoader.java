package com.github.diegonighty.wordle;

import com.github.diegonighty.wordle.game.GameService;
import com.github.diegonighty.wordle.gui.WordleGUIProvider;
import com.github.diegonighty.wordle.gui.listener.WordleGUIListenerHandler;
import com.github.diegonighty.wordle.keyboard.KeyboardInputHandler;
import com.github.diegonighty.wordle.keyboard.KeyboardService;
import com.github.diegonighty.wordle.storage.GameStorage;
import com.github.diegonighty.wordle.storage.UserStorage;
import com.github.diegonighty.wordle.word.HeadWordDictionaryService;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

public class WordlePluginLoader extends JavaPlugin {

	private final WordlePluginBootstrap bootstrap;
	private final TaskTimeCount counter;

	private GameStorage gameStorage;
	private UserStorage userStorage;

	private HeadWordDictionaryService headWordDictionaryService;

	private KeyboardService keyboardService;
	private KeyboardInputHandler keyboardInputHandler;

	private GameService gameService;

	private WordleGUIProvider wordleGUIProvider;
	private WordleGUIListenerHandler wordleGUIListenerHandler;

	public WordlePluginLoader() {
		this.bootstrap = new WordlePluginBootstrap(this);
		this.counter = new TaskTimeCount(logger());
	}

	@Override
	public void onLoad() {
		bootstrap.setupStorage();

		counter.start();
		// ---
		gameStorage.init();
		userStorage.init();
		// ---
		counter.finish("Initializing databases took %s ms");
	}

	@Override
	public void onEnable() {
		logger().info("--------------------");
		logger().info("Starting all WordleCraft services!");

		counter.start();

		bootstrap.setupDictionaries();
		bootstrap.setupKeyboard();
		bootstrap.setupGame();
		bootstrap.setupGui();
		bootstrap.registerCommands();

		counter.finish("Starting all WordleCraft services took %s ms");
		logger().info("--------------------");

		logger().info("WordleCraft is now enabled!");
	}

	public Logger logger() {
		return this.getLogger();
	}

	public void setGameStorage(GameStorage gameStorage) {
		this.gameStorage = gameStorage;
	}

	public void setUserStorage(UserStorage userStorage) {
		this.userStorage = userStorage;
	}

	public void setHeadWordDictionaryService(HeadWordDictionaryService headWordDictionaryService) {
		this.headWordDictionaryService = headWordDictionaryService;
	}

	public GameStorage getGameStorage() {
		return gameStorage;
	}

	public HeadWordDictionaryService getHeadWordDictionaryService() {
		return headWordDictionaryService;
	}

	public UserStorage getUserStorage() {
		return userStorage;
	}

	public KeyboardService getKeyboardService() {
		return keyboardService;
	}

	public void setKeyboardService(KeyboardService keyboardService) {
		this.keyboardService = keyboardService;
	}

	public KeyboardInputHandler getKeyboardInputHandler() {
		return keyboardInputHandler;
	}

	public void setKeyboardInputHandler(KeyboardInputHandler keyboardInputHandler) {
		this.keyboardInputHandler = keyboardInputHandler;
	}

	public WordleGUIProvider getWordleGUIProvider() {
		return wordleGUIProvider;
	}

	public void setWordleGUIProvider(WordleGUIProvider wordleGUIProvider) {
		this.wordleGUIProvider = wordleGUIProvider;
	}

	public WordleGUIListenerHandler getWordleGUIListenerHandler() {
		return wordleGUIListenerHandler;
	}

	public void setWordleGUIListenerHandler(WordleGUIListenerHandler wordleGUIListenerHandler) {
		this.wordleGUIListenerHandler = wordleGUIListenerHandler;
	}

	public GameService getGameService() {
		return gameService;
	}

	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}

	private static class TaskTimeCount {

		private final Logger logger;
		private Instant startTime;

		private TaskTimeCount(Logger logger) {
			this.logger = logger;
		}

		public void start() {
			this.startTime = Instant.now();
		}

		public void finish(String message) {
			Duration duration = Duration.between(startTime, Instant.now());
			logger.info(String.format(message, duration.toMillis()));
		}

	}
}
