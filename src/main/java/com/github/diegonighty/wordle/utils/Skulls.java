package com.github.diegonighty.wordle.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

import static com.github.diegonighty.wordle.packets.PacketHandlerFactory.SERVER_VERSION_INT;

public class Skulls {

	public static final Material MATERIAL;
	private static final Field PROFILE_FIELD;

	static {
		if (SERVER_VERSION_INT < 13) {
			MATERIAL = Material.SKULL_ITEM;
		} else {
			MATERIAL = Material.valueOf("PLAYER_HEAD");
		}

		ItemStack itemStack = new ItemStack(MATERIAL);
		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

		try {
			PROFILE_FIELD = skullMeta.getClass().getDeclaredField("profile");
		} catch (NoSuchFieldException e) {
			throw new IllegalStateException("Cannot get the SkullMeta profile field!", e);
		}
	}

	public static FluentItem create(String url) {
		ItemStack itemStack = new ItemStack(MATERIAL);

		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);

		byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());

		gameProfile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		boolean accessible = PROFILE_FIELD.isAccessible();
		PROFILE_FIELD.setAccessible(true);

		try {
			PROFILE_FIELD.set(skullMeta, gameProfile);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			PROFILE_FIELD.setAccessible(accessible);
		}

		itemStack.setItemMeta(skullMeta);
		return FluentItem.create(itemStack);
	}

}
