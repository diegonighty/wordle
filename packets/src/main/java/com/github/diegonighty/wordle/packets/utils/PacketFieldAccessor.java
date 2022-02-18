package com.github.diegonighty.wordle.packets.utils;

import java.lang.reflect.Field;

public class PacketFieldAccessor {

	private static Field WINDOW_ID_FIELD;

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

}
