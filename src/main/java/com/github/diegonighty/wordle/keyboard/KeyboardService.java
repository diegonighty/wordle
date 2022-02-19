package com.github.diegonighty.wordle.keyboard;

import com.github.diegonighty.wordle.concurrent.bukkit.BukkitExecutor;
import com.github.diegonighty.wordle.concurrent.bukkit.BukkitExecutorProvider;
import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.packets.PacketHandler;
import com.github.diegonighty.wordle.packets.event.ClientKeyboardPressKey;
import com.github.diegonighty.wordle.utils.FakeInventory;
import com.github.diegonighty.wordle.word.HeadWordDictionaryService;
import com.github.diegonighty.wordle.word.WordType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class KeyboardService {

	private final static char[] KEYBOARD = "qwertyuiopasdfghjklzxcvbnm".toCharArray();
	private final static char UNKNOWN_KEY = '?';

	private final static char MARK_KEY = '1';
	private final static char BACKSPACE_KEY = '2';

	private final static int START_KEYBOARD_SLOT = 45;
	private final static int START_HOTBAR_KEYBOARD_SLOT = 72;

	private final Set<UUID> writing = new HashSet<>();

	private final HeadWordDictionaryService headService;
	private final Configuration keyboardConfig;

	private final BukkitExecutor bukkitExecutor = BukkitExecutorProvider.get();
	private final PacketHandler packetHandler;

	public KeyboardService(Plugin plugin, HeadWordDictionaryService wordDictionaryService, PacketHandler packetHandler) {
		this.keyboardConfig = new Configuration(plugin, "keyboard.yml");
		this.headService = wordDictionaryService;
		this.packetHandler = packetHandler;
	}

	public void setKeyboard(Player player) {
		writing.add(player.getUniqueId());

		bukkitExecutor.executeTaskWithDelay(() -> {
			FakeInventory.clearInventoryFor(packetHandler, player);

			int slot = 9;
			for (int i = 0; i < KEYBOARD.length; i++) {
				packetHandler.setFakeItem(
						player,
						(byte) -2,
						slot++,
						headService.getHead(KEYBOARD[i], WordType.NORMAL)
				);
			}

			packetHandler.setFakeItem(player, (byte) -2, getBackspaceSlot(), getBackspaceItem());
			packetHandler.setFakeItem(player, (byte) -2, getMarkSlot(), getMarkItem());
		}, 1);
	}

	public char getClickedKey(ClientKeyboardPressKey event) {
		//TODO maybe change this?
		int index = event.getRawSlot() - START_KEYBOARD_SLOT;

		if (index < 0 || index >= KEYBOARD.length) {
			return getMiscKey(event.getRawSlot());
		}

		return KEYBOARD[index];
	}

	public boolean isTyping(UUID id) {
		return writing.contains(id);
	}

	public void removeKeyboard(Player player) {
		writing.remove(player.getUniqueId());

		bukkitExecutor.executeTaskWithDelay(player::updateInventory, 1);
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
