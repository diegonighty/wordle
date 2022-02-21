package com.github.diegonighty.wordle.packets;

import com.github.diegonighty.wordle.packets.intercept.PacketChannelDuplexHandler;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayInWindowClick;
import net.minecraft.server.v1_12_R1.PacketPlayOutSetSlot;
import net.minecraft.server.v1_12_R1.PacketPlayOutWindowItems;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.Executor;

public class PacketHandler1_12_2_R0_1 implements PacketHandler {

	@Override
	public void setFakeItem(Player player, byte windowID, int slot, ItemStack item) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		EntityPlayer entityPlayer = craftPlayer.getHandle();

		PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(windowID, slot, CraftItemStack.asNMSCopy(item));

		entityPlayer.playerConnection.sendPacket(packet);
	}

	@Override
	public int currentWindowID(Player player) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		EntityPlayer entityPlayer = craftPlayer.getHandle();

		return entityPlayer.activeContainer.windowId;
	}

	@Override
	public void registerPacketInterceptors(Executor mainThreadExecutor) {
		PacketChannelDuplexHandler.addInterceptor(
				PacketPlayInWindowClick.class,
				new KeyboardInterceptor1_12_2_R0_1(mainThreadExecutor)
		);


		PacketChannelDuplexHandler.addInterceptor(
				PacketPlayOutWindowItems.class,
				new KeyboardUpdate1_12_2_R0_1(mainThreadExecutor)
		);
	}

	@Override
	public void injectPlayer(String channel, Player player) {
		((CraftPlayer) player).getHandle()
				.playerConnection.networkManager
				.channel.pipeline()
				.addBefore("packet_handler", channel, new PacketChannelDuplexHandler(player));
	}
}
