package com.github.diegonighty.wordle.packets;

import com.github.diegonighty.wordle.packets.intercept.PacketChannelDuplexHandler;
import net.minecraft.network.protocol.game.PacketPlayInWindowClick;
import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
import net.minecraft.network.protocol.game.PacketPlayOutWindowItems;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.Executor;

public class PacketHandler1_18_2_R0_1 implements PacketHandler {

	@Override
	public void setFakeItem(Player player, byte windowID, int slot, ItemStack item) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		EntityPlayer entityHuman = craftPlayer.getHandle();

		PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(windowID, entityHuman.bV.j(), slot, CraftItemStack.asNMSCopy(item));

		entityHuman.b.a(packet);
	}

	@Override
	public int currentWindowID(Player player) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		EntityPlayer entityPlayer = craftPlayer.getHandle();

		return entityPlayer.bV.j;
	}

	@Override
	public void registerPacketInterceptors(Executor mainThreadExecutor) {
		PacketChannelDuplexHandler.addInterceptor(
				PacketPlayInWindowClick.class,
				new KeyboardInterceptor1_18_2_R0_1(mainThreadExecutor)
		);


		PacketChannelDuplexHandler.addInterceptor(
				PacketPlayOutWindowItems.class,
				new KeyboardUpdate1_18_2_R0_1(mainThreadExecutor)
		);
	}

	@Override
	public void injectPlayer(String channel, Player player) {
		((CraftPlayer) player).getHandle()
				.b.a.m.pipeline()
				.addBefore("packet_handler", channel, new PacketChannelDuplexHandler(player));
	}
}
