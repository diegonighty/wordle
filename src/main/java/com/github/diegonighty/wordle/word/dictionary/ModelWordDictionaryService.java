package com.github.diegonighty.wordle.word.dictionary;

import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.packets.model.ModelDataApplier;
import com.github.diegonighty.wordle.word.WordDictionaryService;
import com.github.diegonighty.wordle.word.WordType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import team.unnamed.gui.core.item.type.ItemBuilder;

public class ModelWordDictionaryService implements WordDictionaryService {

	private final Configuration config;

	public ModelWordDictionaryService(Plugin plugin) {
		this.config = new Configuration(plugin, "heads-model.yml");

		reload();
	}

	@Override
	public ItemStack getHead(Character letter, WordType type) {
		return getItem(letter, type);
	}

	@Override
	public ItemStack getMarkHead() {
		return getHead('1', WordType.MISC);
	}

	@Override
	public ItemStack getBackspaceHead() {
		return getHead('2', WordType.MISC);
	}

	@Override
	public void reload() {
		config.reloadFile();
	}

	private ItemStack getItem(Character letter, WordType type) {
		ConfigurationSection section = config.getConfigurationSection(type.path() + "." + letter);

		return ModelDataApplier.apply(
				ItemBuilder.newBuilder(Material.valueOf(section.getString("material")))
						.setName(letter.toString())
						.build()
				,
				section.getInt("model-data")
		);
	}

}
