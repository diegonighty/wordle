package com.github.diegonighty.wordle.packets.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;

public class ClientKeyboardPressKey extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	private final Player player;
	private final InventoryView inventoryView;
	private final int rawSlot;

	private boolean cancelPacket;

	public ClientKeyboardPressKey(Player player, InventoryView inventoryView, int rawSlot) {
		this.player = player;
		this.inventoryView = inventoryView;
		this.rawSlot = rawSlot;

		this.cancelPacket = false;
	}

	public InventoryView getView() {
		return inventoryView;
	}

	public int getRawSlot() {
		return rawSlot;
	}

	public boolean clickedOutside() {
		return rawSlot < 0;
	}

	public void setCancelPacket(boolean cancelPacket) {
		this.cancelPacket = cancelPacket;
	}

	public boolean cancelPacket() {
		return cancelPacket;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
