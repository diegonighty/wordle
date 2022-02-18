package com.github.diegonighty.wordle.packets.intercept;

import com.github.diegonighty.wordle.packets.PacketHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerInterceptListener implements Listener {

	private final PacketHandler packetHandler;

	public PlayerInterceptListener(PacketHandler packetHandler) {
		this.packetHandler = packetHandler;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		packetHandler.injectPlayer("wordle", event.getPlayer());
	}

}
