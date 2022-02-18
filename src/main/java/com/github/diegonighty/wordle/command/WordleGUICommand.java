package com.github.diegonighty.wordle.command;

import com.github.diegonighty.wordle.game.GameService;
import com.github.diegonighty.wordle.gui.WordleGUIProvider;
import com.github.diegonighty.wordle.packets.PacketHandler;
import com.github.diegonighty.wordle.user.User;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WordleGUICommand extends Command {

	private final WordleGUIProvider guiProvider;
	private final GameService gameService;

	public WordleGUICommand(WordleGUIProvider guiProvider, GameService gameService) {
		super("wordle");

		this.guiProvider = guiProvider;
		this.gameService = gameService;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}

		Player player = (Player) sender;
		User user = gameService.findUserById(player.getUniqueId());

		player.openInventory(guiProvider.buildFor(user));
		return true;
	}

	public static class TestCommand extends Command {

		private final PacketHandler packetHandler;

		public TestCommand(PacketHandler packetHandler) {
			super("test");
			this.packetHandler = packetHandler;
		}

		@Override
		public boolean execute(CommandSender sender, String commandLabel, String[] args) {
			Player player = (Player) sender;

			byte window = (byte) Integer.parseInt(args[0]);
			packetHandler.setFakeItem(player, window, Integer.parseInt(args[1]), new ItemStack(Material.valueOf(args[2])));

			return false;
		}
	}

}
