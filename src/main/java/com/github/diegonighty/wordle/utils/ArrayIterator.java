package com.github.diegonighty.wordle.utils;

import java.util.Iterator;
import java.util.Objects;

public class ArrayIterator<T> implements Iterator<T> {

	private final T[] array;
	private int cursor;

	public ArrayIterator(T[] array) {
		Objects.requireNonNull(array, "array cant be null!");
		this.array = array;
	}

	@Override
	public boolean hasNext() {
		return cursor < array.length;
	}

	@Override
	public T next() {
		return array[cursor++];
	}

}
