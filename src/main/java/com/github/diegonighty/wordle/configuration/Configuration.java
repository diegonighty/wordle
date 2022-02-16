package com.github.diegonighty.wordle.configuration;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public final class Configuration extends YamlConfiguration {

	private final Plugin plugin;
	private final String fileName;
	private final File file;

	public Configuration(Plugin plugin, File file, String fileName) {
		requireNonNull(plugin);
		requireNonNull(fileName, "File name cannot be null");
		requireNonNull(file);

		this.plugin = plugin;
		this.fileName = fileName.endsWith(".yml") ? fileName : fileName + ".yml";
		this.file = new File(file, fileName);

		saveDef();
		loadFile();
	}

	public Configuration(Plugin plugin, String fileName) {
		this(plugin, plugin.getDataFolder(), fileName);
	}

	@Override
	public String getString(String path) {
		return formatString(super.getString(path, path));
	}

	@Override
	public List<String> getStringList(String path) {
		return super.getStringList(path).stream()
				.map(this::formatString)
				.collect(Collectors.toList());
	}

	private void loadFile() {
		try {
			this.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void saveDef() {
		if (!file.exists()) {
			plugin.saveResource(fileName, false);
		}
	}

	public void save() {
		try {
			save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ItemStack getItem(String sectionPath) {
		ConfigurationSection section = getConfigurationSection(sectionPath);

		Material material = Material.matchMaterial(section.getString("material"));
		int data = section.getInt("data", 0);

		return data != 0 ? new ItemStack(material, 1, (short) data) : new ItemStack(material);
	}

	public void reloadFile() {
		try {
			load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private String formatString(final String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

}
