package com.github.diegonighty.wordle.storage;

import com.github.diegonighty.wordle.configuration.Configuration;
import com.github.diegonighty.wordle.storage.implementation.flatfile.FlatFileGameStorage;
import com.github.diegonighty.wordle.storage.implementation.flatfile.FlatFileUserStorage;
import com.github.diegonighty.wordle.storage.implementation.sql.SQLGameStorage;
import com.github.diegonighty.wordle.storage.implementation.sql.SQLUserStorage;
import com.github.diegonighty.wordle.storage.mapping.GameMapper;
import com.github.diegonighty.wordle.storage.mapping.UserMapper;
import com.github.diegonighty.wordle.storage.source.FileStorageSource;
import com.github.diegonighty.wordle.storage.source.SQLStorageSource;
import com.github.diegonighty.wordle.storage.source.SourceType;
import com.github.diegonighty.wordle.storage.source.StorageSource;
import org.bukkit.plugin.Plugin;
import org.jdbi.v3.core.Jdbi;

import java.io.File;

public class StorageFactory {

	private final Configuration dataSource;
	private final SourceType type;

	private final Plugin plugin;

	public StorageFactory(Plugin plugin) {
		this.dataSource = new Configuration(plugin, "datasource.yml");
		this.type = SourceType.valueOf(dataSource.getString("type").toUpperCase());

		this.plugin = plugin;
	}

	public GameStorage createNewGameStorage(StorageSource<?> source) {
		switch (type) {
			case FLATFILE:
				return new FlatFileGameStorage((StorageSource<File>) source);
			case SQL:
				return new SQLGameStorage((StorageSource<Jdbi>) source, new GameMapper());
			default:
				throw new RuntimeException("Not supported storage type!");
		}
	}

	public UserStorage createNewUserStorage(StorageSource<?> source) {
		switch (type) {
			case FLATFILE:
				return new FlatFileUserStorage((StorageSource<File>) source);
			case SQL:
				return new SQLUserStorage((StorageSource<Jdbi>) source, new UserMapper());
			default:
				throw new RuntimeException("Not supported storage type!");
		}
	}

	public StorageSource<?> createNewSource() {
		switch (type) {
			case FLATFILE:
				return new FileStorageSource(plugin);
			case SQL:
				return new SQLStorageSource(dataSource.getConfigurationSection("credentials"));
			default:
				throw new RuntimeException("Not supported storage type!");
		}
	}

}
