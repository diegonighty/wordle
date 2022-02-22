package com.github.diegonighty.wordle.ux;

public enum WordleSound {

	WIN("WIN"),
	PRESS_KEY("PRESS-KEY"),
	LOSE("LOSE")
	;

	String configurationPath;

	WordleSound(String configurationPath) {
		this.configurationPath = configurationPath;
	}

	public String path() {
		return configurationPath;
	}
}
