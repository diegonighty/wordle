package com.github.diegonighty.wordle.packets;

import com.github.diegonighty.wordle.packets.intercept.PacketChannelDuplexHandler;
import net.minecraft.network.protocol.game.PacketPlayInWindowClick;
import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
import net.minecraft.network.protocol.game.PacketPlayOutWindowItems;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.Executor;

public class PacketHandler1_18_1_R0_1 implements PacketHandler {

	@Override
	public void setFakeItem(Player player, byte windowID, int slot, ItemStack item) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		EntityPlayer entityHuman = craftPlayer.getHandle();

		PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(windowID, slot, entityHuman.bW.k(), CraftItemStack.asNMSCopy(item));

		entityHuman.b.a(packet);
	}

	@Override
	public int currentWindowID(Player player) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		EntityPlayer entityPlayer = craftPlayer.getHandle();

		return entityPlayer.bW.j;
	}

	@Override
	public void registerPacketInterceptors(Executor mainThreadExecutor) {
		PacketChannelDuplexHandler.addInterceptor(
				PacketPlayInWindowClick.class,
				new KeyboardInterceptor1_18_1_R0_1(mainThreadExecutor)
		);


		PacketChannelDuplexHandler.addInterceptor(
				PacketPlayOutWindowItems.class,
				new KeyboardUpdate1_18_1_R0_1(mainThreadExecutor)
		);
	}

	@Override
	public void injectPlayer(String channel, Player player) {
		((CraftPlayer) player).getHandle()
				.b.a.k.pipeline()
				.addBefore("packet_handler", channel, new PacketChannelDuplexHandler(player));
	}
}
