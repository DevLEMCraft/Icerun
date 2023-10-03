package de.lemcraft.chaotenmc.icerun.main;

import org.bukkit.plugin.java.JavaPlugin;

import de.lemcraft.chaotenmc.icerun.core.Core;

public class Main extends JavaPlugin {

	private static Main INSTANCE;

	public static Main getInstance() {
		return INSTANCE;
	}

	public void onDisable() {
		Core.onDisable();
	}

	public void onEnable() {
		INSTANCE = this;
		Core.onEnable();
	}

}
