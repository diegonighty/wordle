package com.github.diegonighty.wordle.ux;

import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.game.WordlePlayer;
import com.github.diegonighty.wordle.game.intent.WordleIntent;
import com.github.diegonighty.wordle.game.intent.WordleIntent.WordleIntentPart;
import com.google.common.collect.Lists;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND;

public class SummaryTextService {

	private static final String SQUARE = "⬜";

	private static final ChatColor INCORRECT_COLOR = ChatColor.WHITE;
	private static final ChatColor BAD_POS_COLOR = ChatColor.YELLOW;
	private static final ChatColor CORRECT_COLOR = ChatColor.GREEN;

	private final Configuration configuration;

	public SummaryTextService(Plugin plugin) {
		this.configuration = new Configuration(plugin, "summary.yml");
	}

	public void sendSummaryIfMaxIntents(Player player, WordlePlayer user) {
		if (!configuration.getBoolean("enable")) {
			return;
		}

		if (!user.isMaxIntents()) {
			return;
		}

		String title = configuration.getString("title");
		List<String> summaryCopy = Lists.newArrayList(title, "");

		player.sendMessage(title);

		for (WordleIntent currentIntent : user.getCurrentIntents()) {
			String intentString = formatIntent(currentIntent);

			summaryCopy.add(intentString);
			player.sendMessage(intentString);
		}

		BaseComponent[] components = new ComponentBuilder(configuration.getString("footer"))
				.event(new ClickEvent(SUGGEST_COMMAND, String.join("\n", summaryCopy)))
				.create();

		player.spigot().sendMessage(components);
	}

	private String formatIntent(WordleIntent intent) {
		StringBuilder builder = new StringBuilder();

		for (WordleIntentPart part : intent.getParts()) {
			switch (part.getType()) {
				case INCORRECT:
					builder.append(INCORRECT_COLOR);
					break;
				case CORRECT:
					builder.append(CORRECT_COLOR);
					break;
				case BAD_POSITION:
					builder.append(BAD_POS_COLOR);
					break;
			}

			builder.append(SQUARE);
		}

		return builder.toString();
	}

}
