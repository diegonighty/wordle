package com.github.diegonighty.wordle.user;

import com.github.diegonighty.wordle.concurrent.Promise;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserDataHandlerListener implements Listener {

	private final UserService userService;

	public UserDataHandlerListener(UserService userService) {
		this.userService = userService;
	}

	@EventHandler
	public void handleLogin(AsyncPlayerPreLoginEvent event) {
		if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
			return;
		}

		userService.handleJoin(event.getUniqueId(), event.getName());
	}

	@EventHandler
	public void handleQuit(PlayerQuitEvent event) {
		Promise.runAsync(() -> userService.handleQuit(event.getPlayer()));
	}

}
