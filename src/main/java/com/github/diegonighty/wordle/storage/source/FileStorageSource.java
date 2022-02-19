package com.github.diegonighty.wordle.storage.source;

import org.bukkit.plugin.Plugin;

import java.io.File;

public class FileStorageSource implements StorageSource<File> {

	private final File pluginFolder;

	public FileStorageSource(Plugin plugin) {
		this.pluginFolder = plugin.getDataFolder();
	}

	@Override
	public File connection() {
		return pluginFolder;
	}
}
