package com.github.diegonighty.wordle.packets;

import org.bukkit.Bukkit;

public class PacketHandlerFactory {

	public static final String SERVER_VERSION = Bukkit.getServer().getBukkitVersion()
			.replace("-SNAPSHOT", "")
			.replace("-", "_")
			.replace('.', '_');

	private static final String CLASS_NAME = PacketHandlerFactory.class.getPackage().getName()
			+ ".PacketHandler" + SERVER_VERSION;

	public static final int SERVER_VERSION_INT = Integer.parseInt(
			SERVER_VERSION
					.replace("1_", "")
					.replaceAll("_R\\d", "")
	);

	public static PacketHandler createNewPacketHandler() {
		try {
			Class<?> clazz = Class.forName(CLASS_NAME);
			Object packetHandler = clazz.newInstance();

			if (!(packetHandler instanceof PacketHandler)) {
				throw new IllegalStateException("Invalid PacketHandler: '"
						+ CLASS_NAME + "'. It doesn't implement " + PacketHandler.class);
			}

			return (PacketHandler) packetHandler;
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("PacketHandler not found: '" + CLASS_NAME + '.');
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Failed to instantiate PacketHandler", e);
		}
	}

}
