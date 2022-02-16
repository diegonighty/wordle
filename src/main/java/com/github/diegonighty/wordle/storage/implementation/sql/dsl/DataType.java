package com.github.diegonighty.wordle.storage.implementation.sql.dsl;

public enum DataType {

	TIMESTAMP("TIMESTAMP"),
	BOOLEAN("TINYINT(1)"),
	TEXT("TEXT"),
	STRING("VARCHAR(100)"),
	NUMBER("INT"),
	DECIMAL("FLOAT"),
	EPOCH("INT(21)"),
	UUID("VARCHAR(36)");

	final String sql;

	DataType(String sql) {
		this.sql = sql;
	}

	public String toSql() {
		return sql;
	}
}
