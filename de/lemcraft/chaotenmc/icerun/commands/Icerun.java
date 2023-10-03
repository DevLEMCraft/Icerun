package de.lemcraft.chaotenmc.icerun.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.lemcraft.chaotenmc.icerun.core.Core;
import de.lemcraft.chaotenmc.icerun.events.EventManager;
import de.lemcraft.chaotenmc.icerun.file.FileWriter;

public class Icerun implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			return Arrays.asList("quit", "forcestart", "reload", "seticespawn");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("seticespawn")) {
			return Arrays.asList("lobby", "queue", "1", "2", "3", "4");
		} else {
			return null;
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			String commandOne = args[0].toLowerCase();
			switch (commandOne) {
			case "quit":
				if (Core.playersInLobby.contains(player)) {
					Core.playersInLobby.remove(player);

					Core.playerLeft();

					player.teleport(Core.lobbyLocation);

					player.sendMessage(Core.getPrefix() + "Du hast die Warteschlange verlassen.");

				} else {
					player.sendMessage(Core.getPrefix() + "Du bist in keiner Warteschlange.");
				}
				break;
			case "forcestart":
				if (player.hasPermission(CommandManager.permissionPrefix + "forcestart")) {
					if (Core.playersInLobby.contains(player)) {
						if (EventManager.roundStatus == true) {
							player.sendMessage(Core.getPrefix() + "Die Runde hat bereits begonnen.");
						} else {
							for (Player gamers : Core.playersInLobby) {
								gamers.sendMessage(
										Core.getPrefix() + "Die Runde wurde von einem §eAdmin §7früher gestartet.");
							}
							EventManager.gameStart();
						}
					} else {
						player.sendMessage(Core.getPrefix() + "Dazu musst du in einer Warteschlange sein.");
					}
				} else {
					player.sendMessage(Core.getPrefix() + "Dazu benötigst du die Permission §e'"
							+ CommandManager.permissionPrefix + "forcestart'§7.");
				}
				break;
			case "reload":
				if (player.hasPermission(CommandManager.permissionPrefix + "reload")) {
					Core.reload();
					player.sendMessage(Core.getPrefix() + "Alles wurde neu geladen.");
				} else {
					player.sendMessage(Core.getPrefix() + "Dazu benötigst du die Permission §e'"
							+ CommandManager.permissionPrefix + "reload'§7.");
				} {

			}
				break;
			case "seticespawn":
				if (args.length == 2) {
					String spawnID = args[1];
					Location location = player.getLocation();
					if (player.hasPermission(CommandManager.permissionPrefix + "seticespawn")) {
						FileWriter config = new FileWriter(Core.getPlugin().getDataFolder().getAbsolutePath(),
								"config.yml");
						switch (spawnID) {
						case "lobby":
							config.setValue("icerun.lobbyLocation", location);
							config.save();
							break;
						case "queue":
							config.setValue("icerun.queueLocation", location);
							config.save();
							break;
						case "1":
							config.setValue("icerun.spawnPoints.one", location);
							config.save();
							break;
						case "2":
							config.setValue("icerun.spawnPoints.two", location);
							config.save();
							break;
						case "3":
							config.setValue("icerun.spawnPoints.three", location);
							config.save();
							break;
						case "4":
							config.setValue("icerun.spawnPoints.four", location);
							config.save();
							break;
						}
						player.sendMessage(Core.getPrefix() + "Standort §e" + spawnID + " §7wurde gesetzt");
					} else {
						player.sendMessage(Core.getPrefix() + "Dazu benötigst du die Permission §e'"
								+ CommandManager.permissionPrefix + "reload'§7.");
					}
				}
				break;
			default:
				player.sendMessage(Core.getPrefix() + "Bitte benutze den Command richtig.");
			}
		} else {
			sender.sendMessage(Core.getPrefix() + "Dazu musst du ein Spieler sein.");
		}
		return false;
	}

}
