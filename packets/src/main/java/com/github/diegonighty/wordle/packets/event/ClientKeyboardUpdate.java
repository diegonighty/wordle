package com.github.diegonighty.wordle.packets.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClientKeyboardUpdate extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	private final int windowID;
	private final Player player;

	public ClientKeyboardUpdate(int windowID, Player player) {
		this.windowID = windowID;
		this.player = player;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public int getWindowID() {
		return windowID;
	}

	public Player getPlayer() {
		return player;
	}
}
