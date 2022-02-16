package com.github.diegonighty.wordle.storage.implementation.sql.dsl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Table {

	private final String tableName;
	private final List<Element> elements;

	private List<String> primaryColumns;

	private String columns;
	private String parameters;
	private String declaration;

	public Table(String tableName,
										List<Element> elements) {
		this.tableName = tableName;
		this.elements = elements;
	}

	public Table(String tableName,
										Element... elements) {
		this(tableName, Arrays.asList(elements));
	}

	public String getName() {
		return tableName;
	}

	public String getFirstPrimaryColumn() {
		if (primaryColumns != null) {
			return primaryColumns.get(0);
		}

		return getPrimaryColumns().get(0);
	}

	public List<String> getPrimaryColumns() {
		if (primaryColumns != null) {
			return primaryColumns;
		}

		this.primaryColumns = elements.stream()
				.filter(Element::isPrimary)
				.map(Element::getColumn)
				.collect(Collectors.toList());

		return primaryColumns;
	}

	public String getColumns() {
		if (columns != null) {
			return columns;
		}

		List<String> elementColumns = elements.stream()
				.map(Element::getColumn)
				.collect(Collectors.toList());

		this.columns = String.join(", ", elementColumns);
		return columns;
	}

	public String getParameters() {
		if (parameters != null) {
			return parameters;
		}

		List<String> elementParameter = elements.stream()
				.map(Element::toParameter)
				.collect(Collectors.toList());

		this.parameters = String.join(", ", elementParameter);
		return parameters;
	}

	public String getDeclaration() {
		if (declaration != null) {
			return declaration;
		}

		List<String> elementDeclaration = elements.stream()
				.map(Element::toDeclaration)
				.collect(Collectors.toList());

		this.declaration = String.join(", ", elementDeclaration);
		return declaration;
	}

}
