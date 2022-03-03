package com.github.diegonighty.wordle.leaderboard;

import com.github.diegonighty.wordle.utils.ArrayIterator;

import java.util.Iterator;

/**
 * Represents a player leaderboard, a fixed-size
 * and sorted collection of player {@link LeaderboardEntry}
 */
public class Leaderboard implements Iterable<LeaderboardEntry> {

	public static final int DEFAULT_CAPACITY = 10;

	private final LeaderboardEntry[] entries;
	private int size;

	public Leaderboard(int capacity) {
		this.entries = new LeaderboardEntry[capacity];
		this.size = 0;
	}

	/**
	 * Returns the leader board entry at the
	 * specified index, no null entries will
	 * exist before a non-null entry
	 *
	 * <p>Entries with minor index have greater
	 * score</p>
	 *
	 * @param index The entry index
	 * @return The leaderboard entry
	 * @throws ArrayIndexOutOfBoundsException If
	 *                                        index is out of bounds
	 */
	public synchronized LeaderboardEntry at(int index) {
		return entries[index];
	}

	/**
	 * Adds the given entry to the entry array
	 * if it doesn't exist yet. It will update
	 * the entry position if it already exists
	 *
	 * <p>The internal entry array will remain
	 * sorted after this modification</p>
	 *
	 * @param entry The updated entry
	 */
	public synchronized void update(LeaderboardEntry entry) {

		int newScore = entry.score();
		int len = entries.length;

		for (int i = 0; i < len; i++) {
			LeaderboardEntry element = entries[i];

			if (element == null) {
				// found an empty slot, add our entry here
				entries[i] = entry;

				size++;
				break;
			}

			int score = element.score();

			if (element.is(entry)) {
				// we found an entry with our entry uuid first

				if (score == newScore) {
					// same score, do not update
					break;
				}

				// shift entries one slot to the left until
				// we find our slot
				for (int j = i; j < size; j++) {
					int next = j + 1;
					LeaderboardEntry e = entries[next];
					if (e != null && newScore > e.score()) {
						entries[j] = entry;
						break;
					}
					entries[j] = e;
				}
				break;
			}

			if (newScore > score) {

				// index of the next occurrence of
				// an entry with our entry uuid, or
				// -1 if not found
				int index = -1;
				for (int j = i + 1; j < size; j++) {
					// find a node with our uuid
					if (entries[j].is(entry)) {
						index = j;
						break;
					}
				}

				// the end index to shift entries
				int end;
				if (index == -1) {
					if (size < len) {
						// an element is added,
						// size increments
						end = size++;
					} else {
						// an element is replaced,
						// size remains the same
						end = size - 1;
					}
				} else {
					end = index;
				}

				// shift next entries
				System.arraycopy(entries, i, entries, i + 1, end - i);

				// finally, insert our entry in this slot
				entries[i] = entry;
				break;
			}
		}
	}

	public int capacity() {
		return entries.length;
	}

	public int size() {
		return size;
	}

	public static Leaderboard createDefaultCapacity() {
		return new Leaderboard(Leaderboard.DEFAULT_CAPACITY);
	}


	@Override
	public Iterator<LeaderboardEntry> iterator() {
		return new ArrayIterator<>(entries);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Leaderboard (")
				.append(size)
				.append(") [");
		for (int i = 0; i < entries.length; i++) {
			LeaderboardEntry entry = entries[i];
			if (entry == null) break;
			if (i != 0) builder.append(',');
			builder.append("\n\t")
					.append(i)
					.append(". ")
					.append(entry);
		}
		return builder.append("\n]").toString();
	}

}
