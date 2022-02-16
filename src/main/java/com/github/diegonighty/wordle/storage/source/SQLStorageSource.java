package com.github.diegonighty.wordle.storage.source;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;
import org.jdbi.v3.core.Jdbi;

public class SQLStorageSource implements StorageSource<Jdbi> {

	private final Jdbi connection;

	public SQLStorageSource(ConfigurationSection source) {
		this.connection = connect(source);
	}

	@Override
	public Jdbi connection() {
		return connection;
	}

	private Jdbi connect(ConfigurationSection source) {
		HikariConfig hikari = new HikariConfig();
		hikari.setUsername(source.getString("user"));
		hikari.setPassword(source.getString("password"));
		hikari.setJdbcUrl(getUri(source));
		hikari.setMaximumPoolSize(6);

		return Jdbi.create(new HikariDataSource(hikari));
	}

	private String getUri(ConfigurationSection source) {
		return "jdbc:mysql://" + source.getString("address") + "/" + source.getString("database");
	}
}
