package com.github.diegonighty.wordle.gui.listener;

import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.game.GameService;
import com.github.diegonighty.wordle.game.intent.WordleIntent;
import com.github.diegonighty.wordle.game.intent.WordleIntent.WordleIntentPart;
import com.github.diegonighty.wordle.keyboard.KeyboardInputHandler;
import com.github.diegonighty.wordle.keyboard.KeyboardService;
import com.github.diegonighty.wordle.user.User;
import com.github.diegonighty.wordle.word.HeadWordDictionaryService;
import com.github.diegonighty.wordle.word.WordType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WordleGUIListenerHandler implements Listener {

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
	public void onClickKey(InventoryClickEvent event) {
		if (event.getClickedInventory() == null) {
			return;
		}

		if (!keyboardService.isTyping(event.getWhoClicked().getUniqueId())) {
			return;
		}

		Player player = (Player) event.getWhoClicked();
		char key = keyboardService.getClickedKey(event);

		if (key == keyboardService.getUnknownKey()) {
			event.setCancelled(true);
			return;
		}

		User user = gameService.findUserById(player.getUniqueId());
		Inventory topInventory = event.getView().getTopInventory();

		if (key == keyboardService.getMarkKey()) {
			handleMarkKey(topInventory, user, player);
		} else if (key == keyboardService.getBackspaceKey()) {
			handleBackspace(topInventory, user, player);
		} else {
			handleWrite(topInventory, user, player, key);
		}

		event.setCancelled(true);
	}

	private void handleWrite(Inventory topInventory, User user, Player bukkitPlayer, char key) {
		// TODO
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
		int intentIndex = intents.size() - 1;

		if (intentIndex == -1) {
			// what?
			return;
		}

		AtomicInteger startSlotIntent = new AtomicInteger(getStartSlot(intentIndex));
		for (WordleIntentPart part : intents.get(intentIndex).getParts()) {
			topInventory.setItem(startSlotIntent.getAndIncrement(), buildWordPart(part));
		}
	}

	private int currentTypingSlot(User user, Player bukkitPlayer) {
		int index = user.getPlayer().getCurrentIntents().size() - 1;

		if (index == -1) {
			return 0;
		}

		return getStartSlot(index) + inputHandler.length(bukkitPlayer);
	}

	public boolean handleOpenEvent(InventoryOpenEvent event) {
		keyboardService.setKeyboard((Player) event.getPlayer());
		return true;
	}

	public void handleQuitEvent(InventoryCloseEvent event, User player) {
		Player bukkitPlayer = (Player) event.getPlayer();

		keyboardService.removeKeyboard(bukkitPlayer);
		inputHandler.clearInput(bukkitPlayer);

		gameService.saveAsync(player);
	}

	public int getStartSlot(int index) {
		return gui.getInt("word-index." + index);
	}

	public ItemStack buildWordPart(WordleIntentPart part) {
		return headWordDictionaryService.getHead(part.getLetter(), part.getType());
	}

}
