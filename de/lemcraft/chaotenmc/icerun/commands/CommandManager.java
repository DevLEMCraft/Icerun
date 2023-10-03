package de.lemcraft.chaotenmc.icerun.commands;

import org.bukkit.command.CommandExecutor;

import de.lemcraft.chaotenmc.icerun.core.Core;

public class CommandManager {

	public static final String permissionPrefix = "CMC.icerun.command.";
	
	public static void registerCommand(String commandPrefix, CommandExecutor command) {
		Core.getPlugin().getCommand(commandPrefix).setExecutor(command);
	}
	
}
