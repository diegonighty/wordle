package com.github.diegonighty.wordle.game.intent;

import com.github.diegonighty.wordle.game.WordleGame;
import com.github.diegonighty.wordle.word.WordType;

import java.util.Arrays;

public class WordleIntent {

	private final WordleIntentPart[] parts;

	private final boolean correct;
	private final int badPositions;

	private WordleIntent(WordleIntentPart[] parts, boolean correct, int badPositions) {
		this.parts = parts;
		this.correct = correct;
		this.badPositions = badPositions;
	}

	public WordleIntentPart[] getParts() {
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
		WordleIntentPart[] parts = new WordleIntentPart[5];
		String realPhrase = game.getPhrase();

		if (realPhrase.equals(intentPhrase)) {
			char[] intentPhraseArray = intentPhrase.toCharArray();

			for (int i = 0; i < intentPhraseArray.length; i++) {
				char letter = intentPhraseArray[i];

				parts[i] = new WordleIntentPart(letter, WordType.CORRECT);
			}

			return new WordleIntent(parts, true, 0);
		}

		char[] realPhraseArray = realPhrase.toCharArray();
		char[] sortedRealPhraseArray = realPhrase.toCharArray();
		char[] intentPhraseArray = intentPhrase.toCharArray();

		int badPositions = 0;

		Arrays.sort(sortedRealPhraseArray);

		for (int i = 0; i < intentPhraseArray.length; i++) {
			char actualChar = intentPhraseArray[i];

			if (realPhraseArray[i] == actualChar) {
				parts[i] = new WordleIntentPart(actualChar, WordType.CORRECT);
			} else if (Arrays.binarySearch(sortedRealPhraseArray, actualChar) >= 0) {
				badPositions++;

				parts[i] = new WordleIntentPart(actualChar, WordType.BAD_POSITION);
			} else {
				parts[i] = new WordleIntentPart(actualChar, WordType.INCORRECT);
			}
		}

		return new WordleIntent(parts, false, badPositions);
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
