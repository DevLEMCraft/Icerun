package de.lemcraft.chaotenmc.icerun.core;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import de.lemcraft.chaotenmc.icerun.commands.CommandManager;
import de.lemcraft.chaotenmc.icerun.commands.Icerun;
import de.lemcraft.chaotenmc.icerun.events.EventManager;
import de.lemcraft.chaotenmc.icerun.events.InteractEvent;
import de.lemcraft.chaotenmc.icerun.events.PlayerMoveEvent;
import de.lemcraft.chaotenmc.icerun.file.FileWriter;
import de.lemcraft.chaotenmc.icerun.main.Main;

public class Core {

	private static final String PREFIX = "§3§lChaotenMC §8§l> §7";

	public static String getPrefix() {
		return PREFIX;
	}

	public static Location lobbyLocation = new Location(Bukkit.getWorld("world"), 0, 0, 0, 0, 0);
	public static Location queueLocation = new Location(Bukkit.getWorld("world"), 0, 0, 0, 0, 0);

	public static Location positionOne = new Location(Bukkit.getWorld("world"), 0, 0, 0, 0, 0);
	public static Location positionTwo = new Location(Bukkit.getWorld("world"), 0, 0, 0, 0, 0);
	public static Location positionThree = new Location(Bukkit.getWorld("world"), 0, 0, 0, 0, 0);
	public static Location positionFour = new Location(Bukkit.getWorld("world"), 0, 0, 0, 0, 0);

	public static ArrayList<Player> playersInLobby = new ArrayList<Player>();

	private static Main plugin = null;

	public static Main getPlugin() {
		return plugin;
	}

	public static FileWriter getConfig() {
		return new FileWriter(getPlugin().getDataFolder().getAbsolutePath(), "config.yml");
	}

	public static void onEnable() {
		plugin = Main.getInstance();

		configSetup();
		register();

		loadConfig();

		System.out.println("Das Plugin wurde aktiviert.");
	}

	public static void onDisable() {
		reload();
	}

	public static void reload() {
		EventManager.iceDespawnAllowance = false;
		EventManager.roundStatus = false;

		Sign sign = InteractEvent.playSign;
		sign.setLine(2, "(0/4)");
		sign.update();
		InteractEvent.playSign = sign;

		for (Block blocks : PlayerMoveEvent.removedBlocks) {
			blocks.setType(Material.PACKED_ICE);
		}
		for (Player gamers : playersInLobby) {
			gamers.teleport(lobbyLocation);
			gamers.sendMessage(Core.getPrefix() + "Die Runde wurde vorzeitig beendet.");
		}

		playersInLobby.clear();
		PlayerMoveEvent.removedBlocks.clear();
	}

	private static void register() {
		EventManager.registerListener(new InteractEvent());
		EventManager.registerListener(new PlayerMoveEvent());
		CommandManager.registerCommand("icerun", new Icerun());
		CommandManager.registerCommand("ir", new Icerun());
	}

	public static void playerLeft() {
		System.out.println("Done1");
		String line1 = InteractEvent.playSign.getLine(0); // Icerun
		String line2 = InteractEvent.playSign.getLine(1); //
		String line3 = InteractEvent.playSign.getLine(2); // (?/4)
		String line4 = InteractEvent.playSign.getLine(3); // 

		String line3Changed = line3.toString().replace('(', ')').replace(')', ' ').replaceAll(" ", "");
		String[] currentData = line3Changed.split("/");
		String currentPlayers = currentData[0];
		String maxPlayers = currentData[1];

		Sign sign = InteractEvent.playSign;

		sign.setLine(2, "(" + (Integer.parseInt(currentPlayers) - 1) + "/" + maxPlayers + ")");

		sign.update();

		InteractEvent.playSign = sign;

		System.out.println("Done1");
	}
	
	public static void sendMessageViaWebhook(String title, String message) {
		String cleanedLine = message.replaceAll("§a", "").replaceAll("§b", "").replaceAll("§c", "")
				.replaceAll("§d", "").replaceAll("§e", "").replaceAll("§f", "").replaceAll("§k", "")
				.replaceAll("§m", "").replaceAll("§n", "").replaceAll("§l", "").replaceAll("§o", "")
				.replaceAll("§r", "").replaceAll("§0", "").replaceAll("§1", "").replaceAll("§1", "")
				.replaceAll("§2", "").replaceAll("§3", "").replaceAll("§4", "").replaceAll("§5", "")
				.replaceAll("§6", "").replaceAll("§7", "").replaceAll("§8", "").replaceAll("§9", "");

		try {
			URL url = new URL("https://discord.com/api/webhooks/1158779652672860231/UsyG_VEE--WSMJz1DwPzeyDnuCFedbqWyRXhsglkCiG2ubIHKj3MJrEKOtILTDCMM0n9");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");

			System.out.println("LEMApi> Discord Message via Webhook was prepared to send");
			
			String json = "{\"content\": \" :ice_cube: **" + title + "** :ice_cube: \\n- " + cleanedLine.replace("\n", "\\n") + "\"}";

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(json);
			writer.flush();

			int responseCode = connection.getResponseCode();
			if (responseCode != 204) {
				throw new Exception("Failed to send message to Discord. Response code: " + responseCode);
			}
			System.out.println("LEMApi> Discord Message via Webhook was sent");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void configSetup() {
		FileWriter config = new FileWriter(getPlugin().getDataFolder().getAbsolutePath(), "config.yml");

		config.setDefaultValue("icerun.lobbyLocation", lobbyLocation);
		config.setDefaultValue("icerun.queueLocation", queueLocation);

		config.setDefaultValue("icerun.spawnPoints.one", positionOne);
		config.setDefaultValue("icerun.spawnPoints.two", positionTwo);
		config.setDefaultValue("icerun.spawnPoints.three", positionThree);
		config.setDefaultValue("icerun.spawnPoints.four", positionFour);
		
		config.setDefaultValue("icerun.settings.deathY", 0);

		if (config.valueExist("icerun.plugin.madeby")) {
			config.setValue("icerun.plugin.madeby", "Dev_LEMCraft");
		} else {
			config.setDefaultValue("icerun.plugin.madeby", "Dev_LEMCraft");
		}

		config.save();
	}

	private static void loadConfig() {
		FileWriter config = new FileWriter(getPlugin().getDataFolder().getAbsolutePath(), "config.yml");

		lobbyLocation = (Location) config.getObject("icerun.lobbyLocation");
		queueLocation = (Location) config.getObject("icerun.queueLocation");

		positionOne = (Location) config.getObject("icerun.spawnPoints.one");
		positionTwo = (Location) config.getObject("icerun.spawnPoints.two");
		positionThree = (Location) config.getObject("icerun.spawnPoints.three");
		positionFour = (Location) config.getObject("icerun.spawnPoints.four");
	}

}
