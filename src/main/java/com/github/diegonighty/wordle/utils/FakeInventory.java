package com.github.diegonighty.wordle.utils;

import com.github.diegonighty.wordle.packets.PacketHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FakeInventory {

	private static final ItemStack AIR = new ItemStack(Material.AIR);
	private static final int INVENTORY_SIZE = 46;

	public static void clearInventoryFor(PacketHandler packetHandler, Player player) {
		byte window = -2;

		for (int i = 0; i < INVENTORY_SIZE; i++) {
			packetHandler.setFakeItem(player, window, i, AIR);
		}
	}


}
