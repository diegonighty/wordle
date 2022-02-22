package com.github.diegonighty.wordle.packets.model;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ModelDataApplier {

	public static ItemStack apply(ItemStack item, int data) {
		ItemMeta meta = item.getItemMeta();

		if (meta != null) {
			meta.setCustomModelData(data);
		}

		item.setItemMeta(meta);

		return item;
	}

}
