package com.github.diegonighty.wordle.keyboard;

import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.word.HeadWordDictionaryService;
import com.github.diegonighty.wordle.word.WordType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class KeyboardService {

	private final static char[] KEYBOARD = "qwertyuiopasdfghjklzxcvbnm".toCharArray();
	private final static char UNKNOWN_KEY = '?';

	private final static char MARK_KEY = '1';
	private final static char BACKSPACE_KEY = '2';

	private final static int START_KEYBOARD_SLOT = 54;
	private final static int START_HOTBAR_KEYBOARD_SLOT = 81;

	private final Map<UUID, ItemStack[]> inventories = new HashMap<>();

	private final HeadWordDictionaryService headService;
	private final Configuration keyboardConfig;

	public KeyboardService(Plugin plugin, HeadWordDictionaryService wordDictionaryService) {
		this.keyboardConfig = new Configuration(plugin, "keyboard.yml");
		this.headService = wordDictionaryService;
	}

	public void setKeyboard(Player player) {
		PlayerInventory inventory = player.getInventory();

		inventories.put(player.getUniqueId(), inventory.getContents());
		inventory.clear();

		AtomicInteger slot = new AtomicInteger(9);
		for (int i = 0; i < KEYBOARD.length; i++) {
			inventory.setItem(slot.getAndIncrement(), headService.getHead(KEYBOARD[i], WordType.NORMAL));
		}

		inventory.setItem(getBackspaceSlot(), getBackspaceItem());
		inventory.setItem(getMarkSlot(), getMarkItem());
	}

	public char getClickedKey(InventoryClickEvent event) {
		//TODO maybe change this?
		int index = event.getRawSlot() - START_KEYBOARD_SLOT;

		if (index < 0 || index >= KEYBOARD.length) {
			return getMiscKey(event.getRawSlot());
		}

		return KEYBOARD[index];
	}

	public boolean isTyping(UUID id) {
		return inventories.containsKey(id);
	}

	public void removeKeyboard(Player player) {
		PlayerInventory inventory = player.getInventory();

		inventory.clear();
		inventory.setContents(inventories.get(player.getUniqueId()));

		inventories.remove(player.getUniqueId());
	}

	private char getMiscKey(int rawSlot) {
		if (rawSlot == getBackspaceSlot() + START_HOTBAR_KEYBOARD_SLOT) {
			return BACKSPACE_KEY;
		} else if (rawSlot == getMarkSlot() + START_HOTBAR_KEYBOARD_SLOT) {
			return MARK_KEY;
		} else {
			return UNKNOWN_KEY;
		}
	}

	private ItemStack getBackspaceItem() {
		return headService.getBackspaceHead()
				.setName(keyboardConfig.getString("keys.backspace.name"))
				.build();
	}

	private int getBackspaceSlot() {
		return keyboardConfig.getInt("keys.backspace.slot");
	}

	private ItemStack getMarkItem() {
		return headService.getMarkHead()
				.setName(keyboardConfig.getString("keys.mark.name"))
				.build();
	}

	private int getMarkSlot() {
		return keyboardConfig.getInt("keys.mark.slot");
	}

	public char getMarkKey() {
		return MARK_KEY;
	}

	public char getBackspaceKey() {
		return BACKSPACE_KEY;
	}

	public char getUnknownKey() {
		return UNKNOWN_KEY;
	}
}
