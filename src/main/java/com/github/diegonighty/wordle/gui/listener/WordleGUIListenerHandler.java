package com.github.diegonighty.wordle.gui.listener;

import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.game.GameService;
import com.github.diegonighty.wordle.game.intent.WordleIntent;
import com.github.diegonighty.wordle.game.intent.WordleIntent.WordleIntentPart;
import com.github.diegonighty.wordle.keyboard.KeyboardInputHandler;
import com.github.diegonighty.wordle.keyboard.KeyboardService;
import com.github.diegonighty.wordle.packets.PacketHandler;
import com.github.diegonighty.wordle.packets.event.ClientKeyboardPressKey;
import com.github.diegonighty.wordle.packets.event.ClientKeyboardUpdate;
import com.github.diegonighty.wordle.user.User;
import com.github.diegonighty.wordle.ux.SoundService;
import com.github.diegonighty.wordle.ux.WordleSound;
import com.github.diegonighty.wordle.word.WordDictionaryService;
import com.github.diegonighty.wordle.word.WordType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WordleGUIListenerHandler implements Listener {

	private final GameService gameService;
	private final PacketHandler packetHandler;

	private final KeyboardService keyboardService;
	private final KeyboardInputHandler inputHandler;

	private final WordDictionaryService headWordDictionaryService;
	private final Configuration gui;

	private final SoundService soundService;

	public WordleGUIListenerHandler(
			GameService gameService,
			PacketHandler packetHandler,
			KeyboardService keyboardService,
			KeyboardInputHandler inputHandler,
			WordDictionaryService headWordDictionaryService,
			Configuration gui,
			SoundService soundService
	) {
		this.gameService = gameService;
		this.packetHandler = packetHandler;
		this.keyboardService = keyboardService;
		this.inputHandler = inputHandler;
		this.headWordDictionaryService = headWordDictionaryService;
		this.gui = gui;
		this.soundService = soundService;
	}

	@EventHandler
	public void onRefresh(ClientKeyboardUpdate event) {
		if (!keyboardService.isTyping(event.getPlayer().getUniqueId())) {
			return;
		}

		Player player = event.getPlayer();
		if (event.getWindowID() != packetHandler.currentWindowID(player)) {
			return;
		}

		keyboardService.setKeyboard(player);
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
			event.setCancelPacket(false);
			return;
		}

		player.updateInventory();

		User user = gameService.findUserById(player.getUniqueId());
		if (user.statisticOf().isWonToday() || user.getPlayer().isMaxIntents()) {
			event.setCancelPacket(true);
			return;
		}

		Inventory topInventory = event.getView().getTopInventory();
		soundService.play(player, WordleSound.PRESS_KEY);

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
		ItemStack headToWrite = headWordDictionaryService.getHead(key, WordType.KEYBOARD)
				.toBukkit();

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
			soundService.play(bukkitPlayer, WordleSound.WIN);
		} else {
			// YOU LOSE :v
			soundService.play(bukkitPlayer, WordleSound.LOSE);
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
		return getStartSlot(user.getPlayer().getCurrentIntents().size()) + inputHandler.length(bukkitPlayer);
	}

	public void handleOpen(Player bukkitPlayer) {
		keyboardService.setKeyboard(bukkitPlayer);
	}

	@EventHandler
	public void handleQuitEvent(InventoryCloseEvent event) {
		Player bukkitPlayer = (Player) event.getPlayer();

		if (!keyboardService.isTyping(bukkitPlayer.getUniqueId())) {
			return;
		}

		keyboardService.removeKeyboard(bukkitPlayer);
		inputHandler.clearInput(bukkitPlayer);

		gameService.saveAsync(gameService.findUserById(bukkitPlayer.getUniqueId()));
	}

	public int getStartSlot(int index) {
		return gui.getInt("word-index." + index);
	}

	public ItemStack buildWordPart(WordleIntentPart part) {
		return headWordDictionaryService.getHead(part.getLetter(), part.getType())
				.toBukkit();
	}

	@EventHandler
	public void onClickGUI(InventoryClickEvent event) {
		if (event.getRawSlot() < 0) {
			return;
		}

		if (!keyboardService.isTyping(event.getWhoClicked().getUniqueId())) {
			return;
		}

		event.setCancelled(true);
	}

}
