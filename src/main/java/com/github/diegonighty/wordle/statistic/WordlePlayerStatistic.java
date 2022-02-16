package com.github.diegonighty.wordle.statistic;

public class WordlePlayerStatistic {

	private boolean wonToday;

	private int wins;
	private int fails;
	private int badPositions;
	private int recordOfTries;

	public WordlePlayerStatistic(boolean wonToday, int wins, int fails, int badPositions, int recordOfTries) {
		this.wonToday = wonToday;
		this.wins = wins;
		this.fails = fails;
		this.badPositions = badPositions;
		this.recordOfTries = recordOfTries;
	}

	public int wins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public void addWin() {
		this.wins += 1;
	}

	public int fails() {
		return fails;
	}

	public void addFail() {
		this.fails += 1;
	}

	public void setFails(int fails) {
		this.fails = fails;
	}

	public void addBadPosition(int badPositions) {
		this.badPositions += badPositions;
	}

	public int badPositions() {
		return badPositions;
	}

	public void setBadPositions(int badPositions) {
		this.badPositions = badPositions;
	}

	public int recordOfTries() {
		return recordOfTries;
	}

	public void setRecordOfTries(int recordOfTries) {
		this.recordOfTries = recordOfTries;
	}

	public boolean isWonToday() {
		return wonToday;
	}

	public void setWonToday(boolean wonToday) {
		this.wonToday = wonToday;
	}
}
