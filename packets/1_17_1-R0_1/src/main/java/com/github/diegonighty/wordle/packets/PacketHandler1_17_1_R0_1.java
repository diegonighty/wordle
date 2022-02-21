package com.github.diegonighty.wordle.packets;

import com.github.diegonighty.wordle.packets.intercept.PacketChannelDuplexHandler;
import com.github.diegonighty.wordle.packets.utils.PacketFieldAccessor;
import net.minecraft.network.protocol.game.PacketPlayInWindowClick;
import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
import net.minecraft.network.protocol.game.PacketPlayOutWindowItems;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.inventory.Container;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.Executor;

public class PacketHandler1_17_1_R0_1 implements PacketHandler {

	@Override
	public void setFakeItem(Player player, byte windowID, int slot, ItemStack item) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		EntityPlayer entityHuman = craftPlayer.getHandle();

		Container activeContainer = (Container) PacketFieldAccessor.activeContainer(entityHuman, "containerMenu");

		PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(windowID, slot, activeContainer.incrementStateId(), CraftItemStack.asNMSCopy(item));

		entityHuman.b.sendPacket(packet);
	}

	@Override
	public int currentWindowID(Player player) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		EntityPlayer entityPlayer = craftPlayer.getHandle();

		Container activeContainer = (Container) PacketFieldAccessor.activeContainer(entityPlayer, "containerMenu");

		return activeContainer.j;
	}

	@Override
	public void registerPacketInterceptors(Executor mainThreadExecutor) {
		PacketChannelDuplexHandler.addInterceptor(
				PacketPlayInWindowClick.class,
				new KeyboardInterceptor1_17_1_R0_1(mainThreadExecutor)
		);


		PacketChannelDuplexHandler.addInterceptor(
				PacketPlayOutWindowItems.class,
				new KeyboardUpdate1_17_1_R0_1(mainThreadExecutor)
		);
	}

	@Override
	public void injectPlayer(String channel, Player player) {
		((CraftPlayer) player).getHandle()
				.b.a.k.pipeline()
				.addBefore("packet_handler", channel, new PacketChannelDuplexHandler(player));
	}
}
