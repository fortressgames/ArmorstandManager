package net.fortressgames.armorstandmanager.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import java.util.HashMap;

public class AnvilListener implements Listener {

	public static HashMap<Player, String> lastText = new HashMap<>();

	@EventHandler
	public void anvil(PrepareAnvilEvent e) {
		if(e.getResult() == null) return;
		if(e.getResult().getItemMeta() == null) return;
		String text = e.getResult().getItemMeta().getDisplayName();

		if(!lastText.containsKey((Player) e.getView().getPlayer())) {
			lastText.put((Player) e.getView().getPlayer(), text);
			return;
		}

		lastText.replace((Player) e.getView().getPlayer(), text);
	}
}