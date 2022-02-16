package com.github.diegonighty.wordle.word;

import com.github.diegonighty.wordle.configuration.Configurable;
import com.github.diegonighty.wordle.configuration.Configuration;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import team.unnamed.gui.core.item.type.ItemBuilder;

public class HeadWordDictionaryService implements Configurable {

	private final Table<Character, WordType, String> dictionary = HashBasedTable.create();
	private final Configuration config;

	public HeadWordDictionaryService(Plugin plugin) {
		this.config = new Configuration(plugin, "heads.yml");

		reload();
	}

	public ItemStack getHead(Character letter, WordType type) {
		return ItemBuilder.newSkullBuilder(1)
				.setUrl(dictionary.get(letter, type))
				.setName(letter.toString())
				.build();
	}

	public ItemBuilder getMarkHead() {
		return ItemBuilder.newSkullBuilder(1)
				.setUrl(dictionary.get('1', WordType.MISC));
	}

	public ItemBuilder getBackspaceHead() {
		return ItemBuilder.newSkullBuilder(1)
				.setUrl(dictionary.get('2', WordType.MISC));
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
		loadHeads(WordType.NORMAL);
		loadHeads(WordType.BAD_POSITION);
		loadHeads(WordType.MISC);
	}

}
