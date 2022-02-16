package com.github.diegonighty.wordle.storage.mapping;

import com.github.diegonighty.wordle.game.WordleGame;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.Constraint;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.DataType;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.Element;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.Table;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.TableMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameMapper implements TableMapper<WordleGame> {

	private final static Table TABLE = new Table(
			"game",
			Element.create("id", DataType.UUID, Constraint.PRIMARY),
			Element.create("phrase", DataType.STRING)
	);

	@Override
	public Map<String, Object> map(WordleGame object) {
		Map<String, Object> map = new HashMap<>();

		map.put("id", object.getId());
		map.put("phrase", object.getPhrase());

		return map;
	}

	@Override
	public Table getTable() {
		return TABLE;
	}

	@Override
	public WordleGame map(ResultSet rs, StatementContext ctx) throws SQLException {
		return new WordleGame(
				UUID.fromString(rs.getString("id")),
				rs.getString("phrase")
		);
	}

}
