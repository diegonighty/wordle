package com.github.diegonighty.wordle.gui;

import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.game.intent.WordleIntent;
import com.github.diegonighty.wordle.gui.listener.WordleGUIListenerHandler;
import com.github.diegonighty.wordle.user.User;
import com.github.diegonighty.wordle.word.HeadWordDictionaryService;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.diegonighty.wordle.game.intent.WordleIntent.WordleIntentPart;

public class WordleGUIProvider {

	private final Configuration gui;
	private final HeadWordDictionaryService headService;
	private final WordleGUIListenerHandler listenerHandler;

	public WordleGUIProvider(
			Configuration gui,
			HeadWordDictionaryService headService,
			WordleGUIListenerHandler listenerHandler
	) {
		this.gui = gui;
		this.headService = headService;
		this.listenerHandler = listenerHandler;
	}

	public Inventory buildFor(User user) {
		GUIBuilder builder = GUIBuilder.builder(gui.getString("title-gui"), 5);

		fillBorders(builder);
		placeIntents(builder, user.getPlayer().getCurrentIntents());

		builder
				.openAction(listenerHandler::handleOpenEvent)
				.closeAction(event -> listenerHandler.handleQuitEvent(event, user));

		return builder.build();
	}

	private void placeIntents(GUIBuilder builder, List<WordleIntent> intents) {
		for (int i = 0; i < intents.size(); i++) {
			WordleIntent intent = intents.get(i);

			AtomicInteger slot = new AtomicInteger(getStartSlot(i));
			for (WordleIntentPart part : intent.getParts()) {
				builder.addItem(buildWordPreview(part), slot.getAndIncrement());
			}
		}
	}

	private ItemClickable buildWordPreview(WordleIntentPart part) {
		return ItemClickable.builderCancellingEvent()
				.setItemStack(headService.getHead(part.getLetter(), part.getType()))
				.build();
	}

	private void fillBorders(GUIBuilder builder) {
		ItemClickable border = ItemClickable.builderCancellingEvent()
				.setItemStack(gui.getItem("border"))
				.build();

		builder.fillColumn(border, 0);
		builder.fillColumn(border, 1);
		builder.fillColumn(border, 7);
		builder.fillColumn(border, 8);
	}

	private int getStartSlot(int index) {
		return gui.getInt("word-index." + index);
	}

}
