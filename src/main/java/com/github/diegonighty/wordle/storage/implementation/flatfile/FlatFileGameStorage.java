package com.github.diegonighty.wordle.storage.implementation.flatfile;

import com.github.diegonighty.wordle.game.WordleGame;
import com.github.diegonighty.wordle.storage.implementation.AbstractGameStorage;
import com.github.diegonighty.wordle.storage.source.StorageSource;
import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlatFileGameStorage extends AbstractGameStorage {

	private static final String JSON_EXTENSION = ".json";
	private static final Gson SERIALIZER = new Gson();

	private final File database;

	public FlatFileGameStorage(StorageSource<File> source) {
		this.database = new File(source.connection(), "game");
	}

	@Override
	public void init() {
		if (!database.exists() && !database.mkdirs()) {
			throw new IllegalStateException("Cannot create database " + database.getName());
		}
	}

	@Override
	protected @Nullable WordleGame getGameInStorage() {
		File game = getFiles().stream()
				.findFirst()
				.orElse(null);

		if (game == null || !game.exists()) {
			return null;
		}

		try (Reader reader = new FileReader(game)) {
			return SERIALIZER.fromJson(reader, WordleGame.class);
		} catch (IOException e) {
			throw new RuntimeException("Failed to serialize a " + game.getName() + " game!", e);
		}
	}

	@Override
	protected void updateGameInStorage(WordleGame game) {
		File file = getFile();

		if (file == null) {
			file = new File(database, game.getId().toString() + JSON_EXTENSION) ;
		}

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException exception) {
			throw new IllegalStateException("Cannot create a game file! " + file, exception);
		}

		try (Writer writer = new FileWriter(file)) {
			SERIALIZER.toJson(game, writer);
		} catch (IOException exception) {
			throw new IllegalStateException("Cannot write a game file! " + file, exception);
		}
	}

	@Override
	protected void deleteGames() {
		for (File file : getFiles()) {
			file.delete();
		}
	}

	private List<File> getFiles() {
		File[] files = database.listFiles(
				file -> file.getName().endsWith(JSON_EXTENSION)
		);

		if (files == null) {
			return new ArrayList<>();
		}

		return Arrays.asList(files);
	}

	private File getFile() {
		return getFiles().stream()
				.findFirst()
				.orElse(null);
	}

}
