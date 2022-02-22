package com.github.diegonighty.wordle.gui;

import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.game.intent.WordleIntent;
import com.github.diegonighty.wordle.gui.listener.WordleGUIListenerHandler;
import com.github.diegonighty.wordle.user.User;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;

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

	public Inventory buildFor(User user) {
		GUIBuilder builder = GUIBuilder.builder(gui.getString("title"), 5)
				.openAction(listenerHandler::handleOpenEvent)
				.closeAction(event -> listenerHandler.handleQuitEvent(event, user));

		fillBorders(builder);
		placeIntents(builder, user.getPlayer().getCurrentIntents());

		return builder.build();
	}

	private void placeIntents(GUIBuilder builder, List<WordleIntent> intents) {
		for (int i = 0; i < intents.size(); i++) {
			WordleIntent intent = intents.get(i);

			int slot = listenerHandler.getStartSlot(i);
			for (WordleIntentPart part : intent.getParts()) {
				builder.addItem(buildWordPreview(part), slot++);
			}
		}
	}

	private ItemClickable buildWordPreview(WordleIntentPart part) {
		return ItemClickable.builderCancellingEvent()
				.setItemStack(listenerHandler.buildWordPart(part))
				.build();
	}

	private void fillBorders(GUIBuilder builder) {
		ItemClickable border = ItemClickable.builderCancellingEvent()
				.setItemStack(gui.getItem("border"))
				.build();

		builder.fillColumn(border, 1);
		builder.fillColumn(border, 2);
		builder.fillColumn(border, 8);
		builder.fillColumn(border, 9);
	}

}
