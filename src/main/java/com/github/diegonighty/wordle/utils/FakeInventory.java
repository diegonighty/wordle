package com.github.diegonighty.wordle.utils;

import com.github.diegonighty.wordle.packets.PacketHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FakeInventory {

	private static final ItemStack AIR = new ItemStack(Material.AIR);

	public static void clearInventoryFor(PacketHandler packetHandler, Player player) {
		byte window = -2;
		int slot = 9;

		for (int i = 0; i < player.getInventory().getContents().length; i++) {
			packetHandler.setFakeItem(player, window, slot++, AIR);
		}
	}

	public static void setFakeItem(PacketHandler packetHandler, Player player, int slot, ItemStack item) {
		packetHandler.setFakeItem(
				player,
				(byte) packetHandler.currentWindowID(player),
				slot,
				item
		);
	}


}
