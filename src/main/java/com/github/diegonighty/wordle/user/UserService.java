package com.github.diegonighty.wordle.user;

import com.github.diegonighty.wordle.game.GameService;
import com.github.diegonighty.wordle.game.WordlePlayer;
import com.github.diegonighty.wordle.statistic.WordlePlayerStatistic;
import com.github.diegonighty.wordle.storage.UserStorage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class UserService {

	private final UserStorage userStorage;
	private final GameService gameService;

	public UserService(UserStorage userStorage, GameService gameService) {
		this.userStorage = userStorage;
		this.gameService = gameService;
	}

	public void handleJoin(UUID userID, String name) {
		User user = userStorage.findById(userID);

		if (user == null) {
			user = new User(
					userID,
					name,
					new WordlePlayerStatistic(false, 0, 0,0, 0),
					new WordlePlayer(userID, gameService.getActualGame().getId(), new ArrayList<>())
			);
		}

		userStorage.update(user, false);
	}

	public void handleQuit(Player player) {
		userStorage.updateAndInvalidate(userStorage.findById(player.getUniqueId()));
	}

}
