package com.github.diegonighty.wordle.gui;

import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.game.intent.WordleIntent;
import com.github.diegonighty.wordle.gui.listener.WordleGUIListenerHandler;
import com.github.diegonighty.wordle.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.github.diegonighty.wordle.game.intent.WordleIntent.WordleIntentPart;

public class WordleGUIProvider {

	private final Configuration gui;
	private final WordleGUIListenerHandler listenerHandler;

	public WordleGUIProvider(
			Configuration gui,
			WordleGUIListenerHandler listenerHandler
	) {
		this.gui = gui;
		this.listenerHandler = listenerHandler;
	}

	public void open(Player bukkitPlayer, User user) {
		Inventory inventory = Bukkit.createInventory(null, 54, gui.getString("title"));
		listenerHandler.handleOpen(bukkitPlayer);

		fillBorders(inventory);
		placeIntents(inventory, user.getPlayer().getCurrentIntents());

		bukkitPlayer.openInventory(inventory);
	}

	private void placeIntents(Inventory builder, List<WordleIntent> intents) {
		for (int i = 0; i < intents.size(); i++) {
			WordleIntent intent = intents.get(i);

			int slot = listenerHandler.getStartSlot(i);
			for (WordleIntentPart part : intent.getParts()) {
				builder.setItem(slot++, listenerHandler.buildWordPart(part));
			}
		}
	}

	private void fillBorders(Inventory builder) {
		ItemStack itemStack = gui.getItem("border");

		fill(builder, itemStack, 1);
		fill(builder, itemStack, 2);
		fill(builder, itemStack, 8);
		fill(builder, itemStack, 9);
	}

	private void fill(Inventory inventory, ItemStack item, int column) {
		int indexStart = column - 1;
		int indexEnd = (inventory.getSize() - 9) + column;

		for (int slot = indexStart; slot <= indexEnd; slot += 9) {
			inventory.setItem(slot, item.clone());
		}
	}

}
