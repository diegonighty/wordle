package com.github.diegonighty.wordle.packets.utils;

import java.lang.reflect.Field;

public class PacketFieldAccessor {

	private static Field WINDOW_ID_FIELD;
	private static Field ACTIVE_CONTAINER_FIELD;

	public static synchronized int getWindowID(Object packet, String field) {
		if (WINDOW_ID_FIELD == null) {
			try {
				WINDOW_ID_FIELD = packet.getClass().getDeclaredField(field);
				WINDOW_ID_FIELD.setAccessible(true);

			} catch (NoSuchFieldException e) {
				throw new RuntimeException(
						"Packet field " + field + " doesnt exists in " + packet.getClass().getName() + " class"
				);
			}
		}

		try {
			return WINDOW_ID_FIELD.getInt(packet);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unable to access packet field");
		}
	}

	public static synchronized Object activeContainer(Object entityHuman, String field) {
		if (ACTIVE_CONTAINER_FIELD == null) {
			try {
				ACTIVE_CONTAINER_FIELD = entityHuman.getClass().getDeclaredField(field);
				ACTIVE_CONTAINER_FIELD.setAccessible(true);

			} catch (NoSuchFieldException e) {
				throw new RuntimeException(
						"EntityHuman field " + field + " doesnt exists in " + entityHuman.getClass().getName() + " class"
				);
			}
		}

		try {
			return ACTIVE_CONTAINER_FIELD.get(entityHuman);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unable to access packet field");
		}
	}

}
