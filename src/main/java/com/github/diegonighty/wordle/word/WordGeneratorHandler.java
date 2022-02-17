package com.github.diegonighty.wordle.word;

import com.github.diegonighty.wordle.WordlePluginLoader;
import com.github.diegonighty.wordle.configuration.Configuration;

import java.util.List;
import java.util.Random;

public class WordGeneratorHandler {

	private final static Random RANDOM = new Random();

	private final Configuration config;

	public WordGeneratorHandler(WordlePluginLoader loader) {
		this.config = new Configuration(loader, "words.yml");
	}

	public String chooseRandomWord() {
		List<String> words = config.getStringList("words");

		return words.get(RANDOM.nextInt(words.size()));
	}
}
