package com.github.diegonighty.wordle.storage.mapping;

import com.github.diegonighty.wordle.game.WordlePlayer;
import com.github.diegonighty.wordle.statistic.WordlePlayerStatistic;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.DataType;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.Element;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.Table;
import com.github.diegonighty.wordle.storage.implementation.sql.dsl.TableMapper;
import com.github.diegonighty.wordle.storage.serialization.WordleIntentsSerializer;
import com.github.diegonighty.wordle.user.User;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserMapper implements TableMapper<User> {

	private final static Table TABLE = new Table(
			"user",
			Element.create("id", DataType.UUID),
			Element.create("name", DataType.STRING),
			Element.create("wonToday", DataType.BOOLEAN),
			Element.create("wins", DataType.NUMBER),
			Element.create("fails", DataType.NUMBER),
			Element.create("badPositions", DataType.NUMBER),
			Element.create("recordOfTries", DataType.NUMBER),
			Element.create("currentGame", DataType.UUID),
			Element.create("currentIntents", DataType.TEXT)
	);

	@Override
	public Map<String, Object> map(User object) {
		Map<String, Object> map = new HashMap<>();

		map.put("id", object.getId());
		map.put("name", object.getName());
		map.put("wonToday", object.statisticOf().isWonToday());
		map.put("wins", object.statisticOf().wins());
		map.put("fails", object.statisticOf().fails());
		map.put("badPositions", object.statisticOf().badPositions());
		map.put("recordOfTries", object.statisticOf().recordOfTries());
		map.put("currentGame", object.getPlayer().getCurrentGame());
		map.put("currentIntents", WordleIntentsSerializer.toJSON(object.getPlayer().getCurrentIntents()));

		return map;
	}

	@Override
	public Table getTable() {
		return TABLE;
	}

	@Override
	public User map(ResultSet rs, StatementContext ctx) throws SQLException {
		return new User(
				UUID.fromString(rs.getString("id")),
				rs.getString("name"),
				new WordlePlayerStatistic(
						rs.getBoolean("wonToday"),
						rs.getInt("wins"),
						rs.getInt("fails"),
						rs.getInt("badPositions"),
						rs.getInt("recordOfTries")
				),
				new WordlePlayer(
						UUID.fromString(rs.getString("id")),
						UUID.fromString(rs.getString("currentGame")),
						WordleIntentsSerializer.fromJSON(rs.getString("currentIntents"))
				)
		);
	}

}
