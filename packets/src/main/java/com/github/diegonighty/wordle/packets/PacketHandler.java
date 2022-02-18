package com.github.diegonighty.wordle.packets;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PacketHandler {

	void setFakeItem(Player player, byte windowID, int slot, ItemStack item);

}
