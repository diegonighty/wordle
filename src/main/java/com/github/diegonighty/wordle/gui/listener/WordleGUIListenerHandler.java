package com.github.diegonighty.wordle.gui.listener;

import com.github.diegonighty.wordle.game.GameService;
import com.github.diegonighty.wordle.keyboard.KeyboardInputHandler;
import com.github.diegonighty.wordle.keyboard.KeyboardService;
import com.github.diegonighty.wordle.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class WordleGUIListenerHandler implements Listener {

	private final GameService gameService;

	private final KeyboardService keyboardService;
	private final KeyboardInputHandler inputHandler;

	public WordleGUIListenerHandler(
			GameService gameService,
			KeyboardService keyboardService,
			KeyboardInputHandler inputHandler
	) {
		this.gameService = gameService;
		this.keyboardService = keyboardService;
		this.inputHandler = inputHandler;
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

		if (key == keyboardService.getMarkKey()) {
			handleMarkKey(player);
		} else if (key == keyboardService.getBackspaceKey()) {
			inputHandler.backspace(player);
		} else {
			inputHandler.write(player, key);
		}
	}

	private void handleBackspace(InventoryClickEvent event, Player player) {
		// TODO

		inputHandler.backspace(player);
	}

	private void handleMarkKey(Player bukkitPlayer) {
		if (!inputHandler.isCompleted(bukkitPlayer)) {
			return;
		}

		User user = gameService.findUserById(bukkitPlayer.getUniqueId());

		if (gameService.testPhrase(user, gameService.getActualGame(), inputHandler.out(bukkitPlayer))) {

		}
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

}
