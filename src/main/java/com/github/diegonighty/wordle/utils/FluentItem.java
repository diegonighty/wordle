package com.github.diegonighty.wordle.utils;

import com.github.diegonighty.wordle.packets.model.ModelDataApplier;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class FluentItem {

	private final ItemStack backingItem;

	public static FluentItem create(Material material) {
		return new FluentItem(new ItemStack(material));
	}

	public static FluentItem create(ItemStack item) {
		return new FluentItem(item);
	}

	private FluentItem(ItemStack backingItem) {
		this.backingItem = backingItem;
	}

	public FluentItem setName(String name) {
		return withMeta(itemMeta -> itemMeta.setDisplayName(name));
	}

	public FluentItem setModelData(int data) {
		ModelDataApplier.apply(backingItem, data);
		return this;
	}

	public FluentItem withMeta(Consumer<ItemMeta> metaAction) {
		ItemMeta consumedMeta = backingItem.getItemMeta();
		metaAction.accept(consumedMeta);

		backingItem.setItemMeta(consumedMeta);
		return this;
	}

	public ItemStack toBukkit() {
		return backingItem;
	}
}
