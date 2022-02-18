package com.github.diegonighty.wordle.packets.intercept;

import com.github.diegonighty.wordle.packets.event.ClientKeyboardPressKey;
import com.github.diegonighty.wordle.packets.event.ClientKeyboardUpdate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class Interceptors {

	public static boolean handleWindowUpdate(
			Executor bukkitExecutor,
			Player player,
			InventoryView view,
			int slot
	) {
		return CompletableFuture.supplyAsync(() -> {
			ClientKeyboardPressKey event = new ClientKeyboardPressKey(player, view, slot);

			Bukkit.getPluginManager().callEvent(event);
			return event.cancelPacket();
		}, bukkitExecutor).join();
	}

	public static void handleKeyboardUpdate(
			Executor bukkitExecutor,
			Player player,
			int windowID
	) {
		CompletableFuture.runAsync(() -> {
			ClientKeyboardUpdate event = new ClientKeyboardUpdate(windowID, player);

			Bukkit.getPluginManager().callEvent(event);
		}, bukkitExecutor);
	}

}
