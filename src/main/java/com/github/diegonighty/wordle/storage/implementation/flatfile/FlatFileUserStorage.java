package com.github.diegonighty.wordle.storage.implementation.flatfile;

import com.github.diegonighty.wordle.storage.implementation.CachedAbstractUserStorage;
import com.github.diegonighty.wordle.storage.source.StorageSource;
import com.github.diegonighty.wordle.user.User;
import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.UUID;

public class FlatFileUserStorage extends CachedAbstractUserStorage {

	private static final String JSON_EXTENSION = ".json";
	private static final Gson SERIALIZER = new Gson();

	private final File database;

	public FlatFileUserStorage(StorageSource<File> source) {
		this.database = new File(source.connection(), "users");
	}

	@Override
	public void init() {
		if (!database.exists() && !database.mkdirs()) {
			throw new IllegalStateException("Cannot create database " + database.getName());
		}
	}

	@Override
	protected @Nullable User findByNameInStorage(String playerName) {
		//flatfile buh
		return null;
	}

	@Override
	protected @Nullable User findByIdInStorage(UUID id) {
		File file = getData(id);

		if (!file.exists()) {
			return null;
		}

		try (Reader reader = new FileReader(file)) {
			return SERIALIZER.fromJson(reader, User.class);
		} catch (IOException e) {
			throw new RuntimeException("Failed to serialize a " + file.getName() + " user!", e);
		}
	}


	@Override
	protected void updateInStorage(User user) {
		File file = getData(user.getId());

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException exception) {
			throw new IllegalStateException("Cannot create a user file! " + file, exception);
		}

		try (Writer writer = new FileWriter(file)) {
			SERIALIZER.toJson(user, writer);
		} catch (IOException exception) {
			throw new IllegalStateException("Cannot write a user file! " + file, exception);
		}
	}

	private File getData(UUID id) {
		return new File(database, id.toString() + JSON_EXTENSION);
	}
}
