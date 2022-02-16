package com.github.diegonighty.wordle.storage.implementation.sql.dsl;

import org.jdbi.v3.core.mapper.RowMapper;

import java.util.Map;

public interface TableMapper<T> extends RowMapper<T> {

	Map<String, Object> map(T object);

	Table getTable();

}
