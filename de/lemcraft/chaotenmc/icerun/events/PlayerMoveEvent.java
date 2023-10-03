package de.lemcraft.chaotenmc.icerun.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.lemcraft.chaotenmc.icerun.core.Core;

public class PlayerMoveEvent implements Listener {

	public static ArrayList<Block> removedBlocks = new ArrayList<Block>();

	// Ice de-/respawner
	@EventHandler
	public void onMove(org.bukkit.event.player.PlayerMoveEvent event) {
		event.getPlayer().getLocation().getWorld().setStorm(false);
		event.getPlayer().getLocation().getWorld().setThundering(false);
		event.getPlayer().getLocation().getWorld().setTime(12000);
		if (EventManager.roundStatus == true && EventManager.iceDespawnAllowance == true) {
			Player player = event.getPlayer();
			Location location = player.getLocation();
			Block block = location.add(0, -1, 0).getBlock();
			if (player.getLocation().getBlockY() <= de.lemcraft.chaotenmc.icerun.core.Core.getConfig()
					.getInt("icerun.settings.deathY")) {
				player.teleport(de.lemcraft.chaotenmc.icerun.core.Core.lobbyLocation);
				de.lemcraft.chaotenmc.icerun.core.Core.playersInLobby.remove(player);
				for (Player gamers : de.lemcraft.chaotenmc.icerun.core.Core.playersInLobby) {
					gamers.sendMessage(de.lemcraft.chaotenmc.icerun.core.Core.getPrefix() + "Der Spieler §e"
							+ player.getName() + "§7 hat zu viel Eis gegessen.");
				}
				if (de.lemcraft.chaotenmc.icerun.core.Core.playersInLobby.size() == 1) {
					Player winner = de.lemcraft.chaotenmc.icerun.core.Core.playersInLobby.get(0);
					Bukkit.broadcastMessage(de.lemcraft.chaotenmc.icerun.core.Core.getPrefix() + "Der Spieler §e"
							+ winner.getName() + "§7 hat die Runde gewonnen.");
					Core.playersInLobby.clear();
					EventManager.gameStop();
					winner.teleport(Core.lobbyLocation);
					Core.sendMessageViaWebhook("Sieger",
							"Der Spieler **" + winner.getName() + "** hat die Runde Icerun gewonnen.");
				}
			}
			if (block.getType() == Material.PACKED_ICE) {
				for (int i = 0; i < 3; i++) {
					int time = i + 1;
					int finalI = i;
					Bukkit.getScheduler().scheduleSyncDelayedTask(de.lemcraft.chaotenmc.icerun.core.Core.getPlugin(),
							new Runnable() {
								public void run() {
									switch (finalI) {
									case 0:
										return;
									case 1:
										block.setType(Material.STRUCTURE_VOID);
										removedBlocks.add(block);
										return;
									}
								}
							}, i * 10);
				}
			}
		}
	}

}
