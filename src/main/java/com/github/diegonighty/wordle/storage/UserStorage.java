package com.github.diegonighty.wordle.storage;

import com.github.diegonighty.wordle.concurrent.Promise;
import com.github.diegonighty.wordle.user.User;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface UserStorage {

	void init();

	@Nullable
	Promise<User> findByName(String nickName);

	@Nullable
	User findById(UUID id);

	void update(User user, boolean onlyCache);

	void updateAndInvalidate(User user);

}
