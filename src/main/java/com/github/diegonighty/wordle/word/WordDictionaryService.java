package com.github.diegonighty.wordle.word;

import com.github.diegonighty.wordle.configuration.Configurable;
import org.bukkit.inventory.ItemStack;

public interface WordDictionaryService extends Configurable {

	ItemStack getHead(Character letter, WordType type);

	ItemStack getMarkHead();

	ItemStack getBackspaceHead();

}
