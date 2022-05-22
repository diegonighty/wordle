package com.github.diegonighty.wordle.packets;

import com.github.diegonighty.wordle.packets.intercept.Interceptors;
import com.github.diegonighty.wordle.packets.intercept.PacketInterceptor;
import net.minecraft.network.protocol.game.PacketPlayOutWindowItems;
import org.bukkit.entity.Player;

import java.util.concurrent.Executor;

public class KeyboardUpdate1_18_2_R0_1 implements PacketInterceptor<PacketPlayOutWindowItems> {

	private final Executor bukkitExecutor;

	public KeyboardUpdate1_18_2_R0_1(Executor bukkitExecutor) {
		this.bukkitExecutor = bukkitExecutor;
	}

	@Override
	public PacketPlayOutWindowItems out(Player player, PacketPlayOutWindowItems packet) {
		Interceptors.handleKeyboardUpdate(bukkitExecutor, player, packet.b());
		return packet;
	}
}
