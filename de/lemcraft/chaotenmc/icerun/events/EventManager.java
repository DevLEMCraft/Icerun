package de.lemcraft.chaotenmc.icerun.events;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import de.lemcraft.chaotenmc.icerun.core.Core;

public class EventManager {

	public static boolean roundStatus = false;
	public static boolean iceDespawnAllowance = false;

	public static void registerListener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, Core.getPlugin());
	}

	public static void gameStart() {
		for (Player gamers : Core.playersInLobby) {
			if (gamers.getLocation().getWorld().getName().equalsIgnoreCase(Core.queueLocation.getWorld().getName())) {
				EventManager.roundStatus = true;
				gamers.sendMessage(Core.getPrefix() + "Die Runde fängt an..");
				Sign sign = InteractEvent.playSign;
				sign.setLine(2, "In gange..");
				sign.update();
				InteractEvent.playSign = sign;
				for (int i = 0; i < 15; i++) {
					int time = i + 1;
					int finalI = i;
					Bukkit.getScheduler().scheduleSyncDelayedTask(de.lemcraft.chaotenmc.icerun.core.Core.getPlugin(),
							new Runnable() {
								public void run() {
									switch (finalI) {
									case 0:
										return;
									case 1:
										return;
									case 2:
										return;
									case 3:
										int currentGamers = Core.playersInLobby.size();

										switch (currentGamers) {
										case 0:
											System.out.println("ERROR at line 45 EventManager");
											break;
										case 1:
											Core.playersInLobby.get(0).teleport(Core.positionOne);
											break;
										case 2:
											Core.playersInLobby.get(0).teleport(Core.positionOne);
											Core.playersInLobby.get(1).teleport(Core.positionTwo);
											break;
										case 3:
											Core.playersInLobby.get(0).teleport(Core.positionOne);
											Core.playersInLobby.get(1).teleport(Core.positionTwo);
											Core.playersInLobby.get(2).teleport(Core.positionThree);
											break;
										case 4:
											Core.playersInLobby.get(0).teleport(Core.positionOne);
											Core.playersInLobby.get(1).teleport(Core.positionTwo);
											Core.playersInLobby.get(2).teleport(Core.positionThree);
											Core.playersInLobby.get(3).teleport(Core.positionFour);
											break;
										}
										
										for(Player gamers : Core.playersInLobby) {
											gamers.setLevel(0);
										}

										for (int i = 0; i < 15; i++) {
											int time = i + 1;
											int finalI = i;
											Bukkit.getScheduler().scheduleSyncDelayedTask(
													de.lemcraft.chaotenmc.icerun.core.Core.getPlugin(), new Runnable() {
														public void run() {
															switch (finalI) {
															case 0:
																return;
															case 1:
																return;
															case 2:
																return;
															case 10:
																gamers.sendMessage(
																		Core.getPrefix() + "Das Eis schmilzt..");
																iceDespawnAllowance = true;
																return;
															}
														}
													}, i * 10);
										}
										return;
									}
								}
							}, i * 10);
				}
			}
		}
	}
	public static void startCountdown() {
		for (int i = 60; i >= 0; i--) {
		    int time = i;
		    Bukkit.getScheduler().scheduleSyncDelayedTask(de.lemcraft.chaotenmc.icerun.core.Core.getPlugin(),
		        new Runnable() {
		            public void run() {
		                if (EventManager.roundStatus == false) {
		                    switch (time) {
		                        case 0:
		                            for (Player gamers : Core.playersInLobby) {
		                                gamers.setLevel(0); // 0 seconds remaining
		                            }
		                            Sign sign = InteractEvent.playSign;
		                            sign.setLine(2, "In gange..");
		                            sign.update();
		                            InteractEvent.playSign = sign;
		                            gameStart();
		                            return;
		                        default:
		                            for (Player gamers : Core.playersInLobby) {
		                                gamers.setLevel(time); // Show remaining seconds as level
		                            }
		                            return;
		                    }
		                }
		            }
		        }, (60 - i) * 20); // Delay in ticks (20 ticks = 1 second)
		}
	}

	public static void gameStop() {
		Core.reload();
	}
}
