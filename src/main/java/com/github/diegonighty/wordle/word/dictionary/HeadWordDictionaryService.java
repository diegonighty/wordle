package com.github.diegonighty.wordle.word.dictionary;

import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.utils.FluentItem;
import com.github.diegonighty.wordle.utils.Skulls;
import com.github.diegonighty.wordle.word.WordDictionaryService;
import com.github.diegonighty.wordle.word.WordType;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class HeadWordDictionaryService implements WordDictionaryService {

	private final Table<Character, WordType, String> dictionary = HashBasedTable.create();
	private final Configuration config;

	public HeadWordDictionaryService(Plugin plugin) {
		this.config = new Configuration(plugin, "heads.yml");

		reload();
	}

	@Override
	public FluentItem getHead(Character letter, WordType type) {
		return Skulls.create(getURL(letter, type))
				.setName(letter.toString());
	}

	@Override
	public FluentItem getMarkHead() {
		return Skulls.create(getURL('1', WordType.MISC));
	}

	@Override
	public FluentItem getBackspaceHead() {
		return Skulls.create(getURL('2', WordType.MISC));
	}

	private void loadHeads(WordType type) {
		ConfigurationSection section = config.getConfigurationSection(type.path());

		for (String character : section.getKeys(false)) {
			dictionary.put(character.toCharArray()[0], type, section.getString(character));
		}
	}

	@Override
	public void reload() {
		config.reloadFile();

		loadHeads(WordType.CORRECT);
		loadHeads(WordType.KEYBOARD);
		loadHeads(WordType.INCORRECT);
		loadHeads(WordType.BAD_POSITION);
		loadHeads(WordType.MISC);
	}

	private String getURL(Character row, WordType type) {
		return String.format(
				config.getString("texture-repository"),
				dictionary.get(row, type)
		);
	}

}
