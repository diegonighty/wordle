package com.github.diegonighty.wordle.keyboard;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KeyboardInputHandler {

	private final Map<UUID, StringBuilder> inputMap = new HashMap<>();

	public void write(Player player, char letter) {
		UUID id = player.getUniqueId();

		StringBuilder input = inputMap.getOrDefault(id, new StringBuilder())
				.append(letter);

		inputMap.put(id, input);
	}

	public void backspace(Player player) {
		StringBuilder input = inputMap.get(player.getUniqueId());

		if (input == null) {
			return;
		}

		input.deleteCharAt(input.length() - 1);
	}

	public boolean isCompleted(Player player) {
		StringBuilder input = inputMap.get(player.getUniqueId());

		return input != null && input.length() >= 5;
	}

	public int length(Player player) {
		StringBuilder input = inputMap.get(player.getUniqueId());

		if (input == null) {
			return 0;
		}

		return input.length();
	}

	public String out(Player player) {
		StringBuilder input = inputMap.get(player.getUniqueId());

		clearInput(player);
		return input.toString();
	}

	public void clearInput(Player player) {
		inputMap.remove(player.getUniqueId());
	}

}
