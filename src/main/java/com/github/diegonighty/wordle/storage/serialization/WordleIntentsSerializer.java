package com.github.diegonighty.wordle.storage.serialization;

import com.github.diegonighty.wordle.game.intent.WordleIntent;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WordleIntentsSerializer {

	private final static Gson GSON = new Gson();
	private final static Type TYPE = new TypeToken<ArrayList<WordleIntent>>() {}.getType();

	public static String toJSON(List<WordleIntent> intents) {
		return GSON.toJson(intents);
	}

	public static List<WordleIntent> fromJSON(String json) {
		return GSON.fromJson(json, TYPE);
	}

}
