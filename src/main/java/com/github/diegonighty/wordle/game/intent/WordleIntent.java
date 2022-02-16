package com.github.diegonighty.wordle.game.intent;

import com.github.diegonighty.wordle.game.WordleGame;
import com.github.diegonighty.wordle.word.WordType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WordleIntent {

	private final List<WordleIntentPart> parts;

	private final boolean correct;
	private final int badPositions;

	private WordleIntent(List<WordleIntentPart> parts, boolean correct, int badPositions) {
		this.parts = parts;
		this.correct = correct;
		this.badPositions = badPositions;
	}

	public List<WordleIntentPart> getParts() {
		return parts;
	}

	public boolean isCorrect() {
		return correct;
	}

	public int badPositions() {
		return badPositions;
	}

	//TODO recreate this algorithm?
	public static WordleIntent createOf(String intentPhrase, WordleGame game) {
		List<WordleIntentPart> parts = new ArrayList<>();
		String realPhrase = game.getPhrase();

		if (realPhrase.equals(intentPhrase)) {
			for (char letter : intentPhrase.toCharArray()) {
				parts.add(new WordleIntentPart(letter, WordType.CORRECT));
			}

			return new WordleIntent(parts, true, 0);
		}

		char[] realPhraseArray = realPhrase.toCharArray();
		char[] sortedRealPhraseArray = realPhrase.toCharArray();
		char[] intentPhraseArray = intentPhrase.toCharArray();

		AtomicInteger badPositions = new AtomicInteger(0);

		Arrays.sort(sortedRealPhraseArray);

		for (int i = 0; i < intentPhraseArray.length; i++) {
			char actualChar = intentPhraseArray[i];

			if (realPhraseArray[i] == actualChar) {
				parts.add(new WordleIntentPart(actualChar, WordType.CORRECT));
			} else if (Arrays.binarySearch(sortedRealPhraseArray, actualChar) != -1) {
				badPositions.incrementAndGet();
				parts.add(new WordleIntentPart(actualChar, WordType.BAD_POSITION));
			} else {
				parts.add(new WordleIntentPart(actualChar, WordType.NORMAL));
			}
		}

		return new WordleIntent(parts, false, badPositions.get());
	}

	public static class WordleIntentPart {
		private final char letter;
		private final WordType type;

		public WordleIntentPart(char letter, WordType type) {
			this.letter = letter;
			this.type = type;
		}

		public char getLetter() {
			return letter;
		}

		public WordType getType() {
			return type;
		}
	}
}
