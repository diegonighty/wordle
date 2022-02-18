package com.github.diegonighty.wordle.gui.listener;

import com.github.diegonighty.wordle.concurrent.bukkit.BukkitExecutor;
import com.github.diegonighty.wordle.concurrent.bukkit.BukkitExecutorProvider;
import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.game.GameService;
import com.github.diegonighty.wordle.game.intent.WordleIntent;
import com.github.diegonighty.wordle.game.intent.WordleIntent.WordleIntentPart;
import com.github.diegonighty.wordle.keyboard.KeyboardInputHandler;
import com.github.diegonighty.wordle.keyboard.KeyboardService;
import com.github.diegonighty.wordle.packets.event.ClientKeyboardPressKey;
import com.github.diegonighty.wordle.user.User;
import com.github.diegonighty.wordle.word.HeadWordDictionaryService;
import com.github.diegonighty.wordle.word.WordType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WordleGUIListenerHandler implements Listener {

	private final BukkitExecutor executor = BukkitExecutorProvider.get();

	private final GameService gameService;

	private final KeyboardService keyboardService;
	private final KeyboardInputHandler inputHandler;

	private final HeadWordDictionaryService headWordDictionaryService;
	private final Configuration gui;

	public WordleGUIListenerHandler(
			GameService gameService,
			KeyboardService keyboardService,
			KeyboardInputHandler inputHandler,
			HeadWordDictionaryService headWordDictionaryService,
			Configuration gui
	) {
		this.gameService = gameService;
		this.keyboardService = keyboardService;
		this.inputHandler = inputHandler;
		this.headWordDictionaryService = headWordDictionaryService;
		this.gui = gui;
	}

	@EventHandler
	public void onClickKey(ClientKeyboardPressKey event) {
		if (event.clickedOutside()) {
			return;
		}

		if (!keyboardService.isTyping(event.getPlayer().getUniqueId())) {
			return;
		}

		Player player = event.getPlayer();
		char key = keyboardService.getClickedKey(event);

		if (key == keyboardService.getUnknownKey()) {
			event.setCancelPacket(true);
			return;
		}

		User user = gameService.findUserById(player.getUniqueId());
		if (user.statisticOf().isWonToday() || user.getPlayer().getCurrentIntents().size() >= 5) {
			event.setCancelPacket(true);
			return;
		}

		Inventory topInventory = event.getView().getTopInventory();

		if (key == keyboardService.getMarkKey()) {
			handleMarkKey(topInventory, user, player);
		} else if (key == keyboardService.getBackspaceKey()) {
			handleBackspace(topInventory, user, player);
		} else {
			handleWrite(topInventory, user, player, key);
		}

		event.setCancelPacket(true);
	}

	private void handleWrite(Inventory topInventory, User user, Player bukkitPlayer, char key) {
		// TODO
		if (inputHandler.length(bukkitPlayer) >= 5) {
			return;
		}

		int typingSlot = currentTypingSlot(user, bukkitPlayer);
		ItemStack headToWrite = headWordDictionaryService.getHead(key, WordType.NORMAL);

		topInventory.setItem(typingSlot, headToWrite);
		inputHandler.write(bukkitPlayer, key);
	}

	private void handleBackspace(Inventory topInventory, User user, Player bukkitPlayer) {
		// TODO
		if (inputHandler.length(bukkitPlayer) < 1) {
			return;
		}

		int backspaceSlot = currentTypingSlot(user, bukkitPlayer) - 1;

		topInventory.setItem(backspaceSlot, new ItemStack(Material.AIR));
		inputHandler.backspace(bukkitPlayer);
	}

	private void handleMarkKey(Inventory topInventory, User user, Player bukkitPlayer) {
		if (!inputHandler.isCompleted(bukkitPlayer)) {
			return;
		}

		if (gameService.testPhrase(user, gameService.getActualGame(), inputHandler.out(bukkitPlayer))) {
			// YOU WON!
		}

		List<WordleIntent> intents = user.getPlayer().getCurrentIntents();
		int intentIndex = intents.size();

		int intentIndexGet = intentIndex - 1;
		int safeIntentIndex = intentIndexGet == -1 ? 0 : intentIndexGet;

		int startSlotIntent = getStartSlot(safeIntentIndex);
		for (WordleIntentPart part : intents.get(safeIntentIndex).getParts()) {
			topInventory.setItem(startSlotIntent++, buildWordPart(part));
		}
	}

	private int currentTypingSlot(User user, Player bukkitPlayer) {
		int index = user.getPlayer().getCurrentIntents().size();
		int wordNumber = inputHandler.length(bukkitPlayer);

		if (wordNumber == 0) {
			return getStartSlot(index);
		}

		return getStartSlot(index) + wordNumber;
	}

	public boolean handleOpenEvent(InventoryOpenEvent event) {
		executor.executeTaskWithDelay(() -> {
			keyboardService.setKeyboard((Player) event.getPlayer());
		}, 10);
		return false;
	}

	public void handleQuitEvent(InventoryCloseEvent event, User player) {
		Player bukkitPlayer = (Player) event.getPlayer();

		keyboardService.removeKeyboard(bukkitPlayer);
		inputHandler.clearInput(bukkitPlayer);

		bukkitPlayer.updateInventory();
		gameService.saveAsync(player);
	}

	public int getStartSlot(int index) {
		return gui.getInt("word-index." + index);
	}

	public ItemStack buildWordPart(WordleIntentPart part) {
		return headWordDictionaryService.getHead(part.getLetter(), part.getType());
	}

}
