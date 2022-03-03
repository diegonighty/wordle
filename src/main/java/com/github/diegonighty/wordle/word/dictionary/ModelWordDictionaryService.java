package com.github.diegonighty.wordle.word.dictionary;

import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.utils.FluentItem;
import com.github.diegonighty.wordle.utils.SafeMaterial;
import com.github.diegonighty.wordle.word.WordDictionaryService;
import com.github.diegonighty.wordle.word.WordType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class ModelWordDictionaryService implements WordDictionaryService {

	private final Configuration config;

	public ModelWordDictionaryService(Plugin plugin) {
		this.config = new Configuration(plugin, "heads-model.yml");

		reload();
	}

	@Override
	public FluentItem getHead(Character letter, WordType type) {
		return getItem(letter, type);
	}

	@Override
	public FluentItem getMarkHead() {
		return getHead('1', WordType.MISC);
	}

	@Override
	public FluentItem getBackspaceHead() {
		return getHead('2', WordType.MISC);
	}

	@Override
	public void reload() {
		config.reloadFile();
	}

	private FluentItem getItem(Character letter, WordType type) {
		ConfigurationSection section = config.getConfigurationSection(type.path() + "." + letter);

		return FluentItem.create(SafeMaterial.from(section.getString("material")))
						.setName(letter.toString())
						.setModelData(section.getInt("model-data"));
	}

}
