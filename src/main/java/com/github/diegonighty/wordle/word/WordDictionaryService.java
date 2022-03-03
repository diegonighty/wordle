package com.github.diegonighty.wordle.word;

import com.github.diegonighty.wordle.configuration.Configurable;
import com.github.diegonighty.wordle.utils.FluentItem;

public interface WordDictionaryService extends Configurable {

	FluentItem getHead(Character letter, WordType type);

	FluentItem getMarkHead();

	FluentItem getBackspaceHead();

}
