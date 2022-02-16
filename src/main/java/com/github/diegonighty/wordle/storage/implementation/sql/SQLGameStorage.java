package com.github.diegonighty.wordle.storage.implementation.sql;

import com.github.diegonighty.wordle.game.WordleGame;
import com.github.diegonighty.wordle.storage.implementation.AbstractGameStorage;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.Table;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.TableMapper;
import com.github.diegonighty.wordle.storage.source.StorageSource;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.Nullable;

public class SQLGameStorage extends AbstractGameStorage {

	private final Jdbi connection;
	private final TableMapper<WordleGame> mapper;
	private final Table table;

	public SQLGameStorage(StorageSource<Jdbi> source, TableMapper<WordleGame> mapper) {
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
	protected @Nullable WordleGame getGameInStorage() {
		try (Handle handle = connection.open()) {
			return handle
					.select("SELECT * FROM :table")
					.bind("table", table.getName())
					.map(mapper)
					.findFirst()
					.orElse(null);
		}
	}

	@Override
	protected void updateGameInStorage(WordleGame game) {
		try (Handle handle = connection.open()) {
			handle
					.createUpdate("REPLACE INTO :table (:columns) VALUES (:values)")
					.bind("table", table.getName())
					.bind("columns", table.getColumns())
					.bind("values", table.getParameters())
					.bindMap(mapper.map(game))
					.execute();
		}
	}

	@Override
	protected void deleteGames() {
		try (Handle handle = connection.open()) {
			handle
					.createUpdate("DELETE FROM :table")
					.bind("table", table.getName())
					.bind("column", table.getFirstPrimaryColumn())
					.execute();
		}
	}

}
