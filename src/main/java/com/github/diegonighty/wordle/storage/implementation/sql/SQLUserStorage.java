package com.github.diegonighty.wordle.storage.implementation.sql;

import com.github.diegonighty.wordle.storage.implementation.CachedAbstractUserStorage;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.Table;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.TableMapper;
import com.github.diegonighty.wordle.storage.source.StorageSource;
import com.github.diegonighty.wordle.user.User;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SQLUserStorage extends CachedAbstractUserStorage {

	private final Jdbi connection;
	private final TableMapper<User> mapper;
	private final Table table;

	public SQLUserStorage(StorageSource<Jdbi> source, TableMapper<User> mapper) {
		this.connection = source.connection();
		this.table = mapper.getTable();
		this.mapper = mapper;
	}

	@Override
	public void init() {
		try (Handle handle = connection.open()) {
			handle.execute("CREATE TABLE IF NOT EXISTS " + table.getName() + "(" + table.getDeclaration() + ")");
		}
	}

	@Override
	protected @Nullable User findByNameInStorage(String playerName) {
		try (Handle handle = connection.open()) {
			return handle
					.select("SELECT * FROM :table WHERE name = :name")
					.bind("table", table.getName())
					.bind("name", playerName)
					.map(mapper)
					.findFirst()
					.orElse(null);
		}
	}

	@Override
	protected @Nullable User findByIdInStorage(UUID id) {
		try (Handle handle = connection.open()) {
			return handle
					.select("SELECT * FROM :table WHERE id = :id")
					.bind("table", table.getName())
					.bind("id", id.toString())
					.map(mapper)
					.findFirst()
					.orElse(null);
		}
	}

	@Override
	protected void updateInStorage(User user) {
		try (Handle handle = connection.open()) {
			handle
					.createUpdate("REPLACE INTO :table (:columns) VALUES (:values)")
					.bind("table", table.getName())
					.bind("columns", table.getColumns())
					.bind("values", table.getParameters())
					.bindMap(mapper.map(user))
					.execute();
		}
	}
}
