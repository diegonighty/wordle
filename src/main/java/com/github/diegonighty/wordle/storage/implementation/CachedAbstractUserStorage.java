package com.github.diegonighty.wordle.storage.implementation;

import com.github.diegonighty.wordle.concurrent.Promise;
import com.github.diegonighty.wordle.storage.UserStorage;
import com.github.diegonighty.wordle.user.User;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CachedAbstractUserStorage implements UserStorage {

	private Map<UUID, User> cache = new ConcurrentHashMap<>();

	@Override
	public @Nullable Promise<User> findByName(String nickName) {
		return Promise.supplyAsync(() -> {
			User user = findByNameInStorage(nickName);

			if (user != null) {
				cache.put(user.getId(), user);
			}

			return user;
		});
	}

	@Override
	public @Nullable User findById(UUID id) {
		User user = cache.get(id);

		if (user == null) {
			user = findByIdInStorage(id);

			if (user != null) {
				cache.put(user.getId(), user);
			}
		}

		return user;
	}


	@Override
	public void update(User user, boolean onlyCache) {
		cache.put(user.getId(), user);

		if (!onlyCache) {
			updateInStorage(user);
		}
	}

	@Override
	public void updateAndInvalidate(User user) {
		cache.remove(user.getId());

		updateInStorage(user);
	}

	protected abstract @Nullable User findByNameInStorage(String playerName);
	protected abstract @Nullable User findByIdInStorage(UUID id);
	protected abstract void updateInStorage(User user);

}
