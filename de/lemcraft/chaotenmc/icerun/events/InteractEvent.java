package de.lemcraft.chaotenmc.icerun.events;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.lemcraft.chaotenmc.icerun.core.Core;

public class InteractEvent implements Listener {

	public static Sign playSign = null;

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock() != null && event.getClickedBlock().getType() != null
					&& event.getClickedBlock().getType() == Material.OAK_WALL_SIGN) {
				if (event.getClickedBlock().getState() instanceof Sign) {
					Sign sign = (Sign) event.getClickedBlock().getState();
					String line1 = sign.getLine(0); // Icerun
					String line2 = sign.getLine(1); //
					String line3 = sign.getLine(2); // (?/4)
					String line4 = sign.getLine(3); //

					if (line1 != null && line3 != null && line4 != null) {
						if (line1.equalsIgnoreCase("Icerun")) {
							playSign = (Sign) event.getClickedBlock().getState();
							String line3Changed = line3.toString().replace('(', ')').replace(')', ' ').replaceAll(" ",
									"");
							String[] currentData = line3Changed.split("/");
							String currentPlayers = currentData[0];
							String maxPlayers = currentData[1];

							if (currentPlayers.equalsIgnoreCase(maxPlayers)) {
								player.sendMessage(Core.getPrefix()
										+ "Die Lobby ist aktuell schon voll. Warte bis ein Platz wieder frei wird.");
							} else {
								if (Core.playersInLobby.contains(player)) {
									player.sendMessage(Core.getPrefix()
											+ "Du bist bereits in der Warteschlange. Warte bis die Runde anfängt oder benutz §e'/icerun quit' §7zum verlassen der Warteschlange.");
								} else {
									Core.playersInLobby.add(player);
									player.teleport(Core.queueLocation);
									player.sendMessage(Core.getPrefix()
											+ "Du bist jetzt in der Warteschlange. Warte bis die Runde anfängt.");
									playSign.setLine(2,
											"(" + (Integer.parseInt(currentPlayers) + 1) + "/" + maxPlayers + ")");
									playSign.update();
									playSign = (Sign) event.getClickedBlock().getState();
									if (playSign.getLine(2).equalsIgnoreCase("(4/4)")) {
										EventManager.gameStart();
									} else if ((Integer.parseInt(currentPlayers) + 1) == 2) {
										EventManager.startCountdown();
									}
								}
							}
						}
					}
					sign.update();
					playSign.update();
				}
			}
		}
	}

}
