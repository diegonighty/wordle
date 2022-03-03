package com.github.diegonighty.wordle.command;

import com.github.diegonighty.wordle.game.GameService;
import com.github.diegonighty.wordle.gui.WordleGUIProvider;
import com.github.diegonighty.wordle.user.User;
import com.github.diegonighty.wordle.user.UserService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WordleGUICommand extends Command {

	private final WordleGUIProvider guiProvider;

	private final GameService gameService;
	private final UserService userService;

	public WordleGUICommand(WordleGUIProvider guiProvider, GameService gameService, UserService userService) {
		super("wordle");

		this.guiProvider = guiProvider;
		this.gameService = gameService;
		this.userService = userService;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}

		Player player = (Player) sender;
		User user = gameService.findUserById(player.getUniqueId());

		userService.updateGame(user);
		guiProvider.open(player, user);
		return true;
	}

}
