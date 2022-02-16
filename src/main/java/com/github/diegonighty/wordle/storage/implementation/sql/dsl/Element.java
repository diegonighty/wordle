package com.github.diegonighty.wordle.storage.implementation.sql.dsl;

import java.util.Arrays;
import java.util.List;

public class Element {

	private final String column;
	private final DataType type;

	private final List<Constraint> constraints;

	private ForeignKey key;
	private String declaration;

	public static Element create(String column, DataType type, Constraint... constraints) {
		return new Element(column, type, null, constraints);
	}

	public static Element create(String column, DataType type, ForeignKey key, Constraint... constraints) {
		return new Element(column, type, key, constraints);
	}

	private Element(String column, DataType type, ForeignKey key, Constraint... constraints) {
		this.column = column;
		this.type = type;
		this.key = key;
		this.constraints = Arrays.asList(constraints);
	}

	public boolean isPrimary() {
		return constraints.contains(Constraint.PRIMARY);
	}

	public boolean isSecondary() {
		return constraints.contains(Constraint.SECONDARY);
	}

	public boolean isNullable() {
		return !constraints.contains(Constraint.NOT_NULL);
	}

	public boolean isUnique() {
		return constraints.contains(Constraint.UNIQUE);
	}

	public boolean hasReference() {
		return key != null;
	}

	public String toParameter() {
		return ":" + column;
	}

	public String toDeclaration() {
		if (declaration != null) {
			return declaration;
		}

		final StringBuilder builder = new StringBuilder();

		builder
				.append(column)
				.append(" ")
				.append(type.toSql())
				.append(" ");

		constraints.forEach(constraint -> builder.append(constraint.toSql()).append(" "));

		if (hasReference()) {
			builder.append(key.toSql());
		}

		this.declaration = builder.toString();

		return declaration;
	}

	public String getColumn() {
		return column;
	}

	public static class ForeignKey {
		private final static String TEMPLATE = "FOREIGN KEY REFERENCES %s(%s) %s %s";

		private final String tableReference;
		private final String elementReference;

		private final ForeignTrigger trigger;
		private final ForeignAction action;

		private ForeignKey(
				String tableReference,
				String elementReference,
				ForeignTrigger trigger,
				ForeignAction action
		) {
			this.tableReference = tableReference;
			this.elementReference = elementReference;
			this.trigger = trigger;
			this.action = action;
		}

		public static ForeignKey of(
				String tableReference,
				String elementReference,
				ForeignTrigger trigger,
				ForeignAction action
		) {
			return new ForeignKey(tableReference, elementReference, trigger, action);
		}

		public String toSql() {
			return String.format(TEMPLATE, tableReference, elementReference, trigger.sql, action.sql);
		}

		public enum ForeignTrigger {
			DELETE("ON DELETE"),
			UPDATE("ON UPDATE");

			String sql;

			ForeignTrigger(String sql) {
				this.sql = sql;
			}

			public String getSql() {
				return sql;
			}
		}

		public enum ForeignAction {
			RESTRICT("RESTRICT"),
			CASCADE("CASCADE"),
			NULL("SET NULL"),
			NOTHING("NO ACTION"),
			DEFAULT("SET DEFAULT");

			String sql;

			ForeignAction(String sql) {
				this.sql = sql;
			}

			public String getSql() {
				return sql;
			}
		}
	}
}
