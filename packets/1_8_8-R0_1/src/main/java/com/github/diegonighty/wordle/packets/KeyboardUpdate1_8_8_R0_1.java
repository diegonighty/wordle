package com.github.diegonighty.wordle.packets;

import com.github.diegonighty.wordle.packets.intercept.Interceptors;
import com.github.diegonighty.wordle.packets.intercept.PacketInterceptor;
import com.github.diegonighty.wordle.packets.utils.PacketFieldAccessor;
import net.minecraft.server.v1_8_R3.PacketPlayOutWindowItems;
import org.bukkit.entity.Player;

import java.util.concurrent.Executor;

public class KeyboardUpdate1_8_8_R0_1 implements PacketInterceptor<PacketPlayOutWindowItems> {

	private final Executor bukkitExecutor;

	public KeyboardUpdate1_8_8_R0_1(Executor bukkitExecutor) {
		this.bukkitExecutor = bukkitExecutor;
	}

	@Override
	public PacketPlayOutWindowItems out(Player player, PacketPlayOutWindowItems packet) {
		Interceptors.handleKeyboardUpdate(bukkitExecutor, player, PacketFieldAccessor.getWindowID(packet, "a"));
		return packet;
	}
}
