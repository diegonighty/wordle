package com.github.diegonighty.wordle.packets;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.Executor;

public interface PacketHandler {

	void setFakeItem(Player player, byte windowID, int slot, ItemStack item);

	int currentWindowID(Player player);

	void registerPacketInterceptors(Executor mainThreadExecutor);

	void injectPlayer(String channel, Player player);

}
