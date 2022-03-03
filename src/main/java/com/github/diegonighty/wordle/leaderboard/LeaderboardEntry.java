package com.github.diegonighty.wordle.leaderboard;

import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an entry in a leaderboard, compound by
 * player unique identifier, display name and the
 * actual score
 *
 * <p>Note that this class is immutable</p>
 */
public class LeaderboardEntry {

	private final UUID uuid;
	private final String name;
	private final int score;

	public LeaderboardEntry(UUID uuid, String name, int score) {
		this.uuid = uuid;
		this.name = name;
		this.score = score;
	}

	public static LeaderboardEntry of(Player player, int score) {
		return new LeaderboardEntry(player.getUniqueId(), player.getName(), score);
	}

	public UUID uuid() {
		return uuid;
	}

	public String name() {
		return name;
	}

	public int score() {
		return score;
	}

	public boolean is(LeaderboardEntry entry) {
		return uuid.equals(entry.uuid);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LeaderboardEntry that = (LeaderboardEntry) o;
		return score == that.score
				&& uuid.equals(that.uuid)
				&& name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, name, score);
	}

	@Override
	public String toString() {
		return name + " - " + score + " (" + uuid + ')';
	}

}
