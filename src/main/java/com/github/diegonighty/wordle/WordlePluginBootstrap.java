package com.github.diegonighty.wordle;

import com.github.diegonighty.wordle.command.WordleGUICommand;
import com.github.diegonighty.wordle.command.internal.CommandMapper;
import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.game.GameService;
import com.github.diegonighty.wordle.game.GameTaskHandler;
import com.github.diegonighty.wordle.gui.WordleGUIProvider;
import com.github.diegonighty.wordle.gui.listener.WordleGUIListenerHandler;
import com.github.diegonighty.wordle.keyboard.KeyboardInputHandler;
import com.github.diegonighty.wordle.keyboard.KeyboardService;
import com.github.diegonighty.wordle.packets.PacketHandlerFactory;
import com.github.diegonighty.wordle.storage.GameStorage;
import com.github.diegonighty.wordle.storage.StorageFactory;
import com.github.diegonighty.wordle.storage.source.StorageSource;
import com.github.diegonighty.wordle.user.UserDataHandlerListener;
import com.github.diegonighty.wordle.user.UserService;
import com.github.diegonighty.wordle.word.HeadWordDictionaryService;
import com.github.diegonighty.wordle.word.WordGeneratorHandler;
import org.bukkit.plugin.PluginManager;
import team.unnamed.gui.core.GUIListeners;

public class WordlePluginBootstrap {

	private final WordlePluginLoader loader;
	private final PluginManager pluginManager;

	public WordlePluginBootstrap(WordlePluginLoader loader) {
		this.loader = loader;
		this.pluginManager = loader.getServer().getPluginManager();
	}

	public void setupPacketFactory() {
		loader.setPacketHandler(PacketHandlerFactory.createNewPacketHandler());
	}

	public void setupStorage() {
		StorageFactory storageFactory = new StorageFactory(loader);
		StorageSource<?> source = storageFactory.createNewSource();

		loader.setGameStorage(storageFactory.createNewGameStorage(source));
		loader.setUserStorage(storageFactory.createNewUserStorage(source));
	}

	public void setupDictionaries() {
		loader.setHeadWordDictionaryService(new HeadWordDictionaryService(loader));
	}

	public void setupKeyboard() {
		loader.setKeyboardInputHandler(new KeyboardInputHandler());
		loader.setKeyboardService(new KeyboardService(loader, loader.getHeadWordDictionaryService(), loader.getPacketHandler()));
	}

	public void setupGame() {
		GameStorage storage = loader.getGameStorage();
		GameService service = new GameService(
				storage,
				loader.getUserStorage(),
				new WordGeneratorHandler(loader)
		);

		GameTaskHandler taskHandler = new GameTaskHandler(loader, service);
		taskHandler.createTask();

		service.setupGame();

		loader.setGameService(
				service
		);
	}

	public void setupGui() {
		Configuration gui = new Configuration(loader, "gui.yml");
		WordleGUIListenerHandler listenerHandler = new WordleGUIListenerHandler(
				loader.getGameService(),
				loader.getKeyboardService(),
				loader.getKeyboardInputHandler(),
				loader.getHeadWordDictionaryService(),
				gui
		);

		loader.setWordleGUIListenerHandler(
				listenerHandler
		);

		loader.setWordleGUIProvider(new WordleGUIProvider(gui, listenerHandler));

		pluginManager.registerEvents(listenerHandler, loader);
		pluginManager.registerEvents(new GUIListeners(), loader);
	}

	public void setupUserServices() {
		UserService userService = new UserService(loader.getUserStorage(), loader.getGameService());
		UserDataHandlerListener userDataHandlerListener = new UserDataHandlerListener(userService);

		pluginManager.registerEvents(userDataHandlerListener, loader);
		loader.setUserService(userService);
	}

	public void registerCommands() {
		WordleGUICommand wordleGUICommand = new WordleGUICommand(loader.getWordleGUIProvider(), loader.getGameService());
		CommandMapper.register(wordleGUICommand);
		CommandMapper.register(new WordleGUICommand.TestCommand(loader.getPacketHandler()));
	}

}
