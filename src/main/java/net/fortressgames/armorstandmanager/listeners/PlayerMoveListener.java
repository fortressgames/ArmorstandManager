package net.fortressgames.armorstandmanager.listeners;

import net.fortressgames.armorstandmanager.ArmorstandManager;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandModule;
import net.fortressgames.armorstandmanager.users.User;
import net.fortressgames.armorstandmanager.users.UserModule;
import net.fortressgames.fortressapi.events.PlayerMoveTaskEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoveListener implements Listener {

	@EventHandler
	public void move(PlayerMoveTaskEvent e) {
		Player player = e.getPlayer();
		User user = UserModule.getInstance().getUser(player);

		if(!ArmorstandManager.getInstance().isToggle()) return;
		if(user == null) return;

		user.getSpawned().removeIf(armorstandHolder -> {

			if(armorstandHolder.getRenderDistance() == -1) return false;

			if(player.getLocation().distance(armorstandHolder.getCustomArmorstand().getLocation()) > armorstandHolder.getRenderDistance()) {
				armorstandHolder.getCustomArmorstand().remove(player);
				return true;
			}

			return false;
		});

		// Spawn
		ArmorstandModule.getInstance().getCustomArmorstands().forEach(armorstandHolder -> {
			if(armorstandHolder.getRenderDistance() == -1) {

				if(!user.getSpawned().contains(armorstandHolder)) {
					armorstandHolder.getCustomArmorstand().spawn(player);
					user.getSpawned().add(armorstandHolder);
				}

			} else if(player.getLocation().distance(armorstandHolder.getCustomArmorstand().getLocation()) <= armorstandHolder.getRenderDistance()) {

				if(!user.getSpawned().contains(armorstandHolder)) {
					armorstandHolder.getCustomArmorstand().spawn(player);
					user.getSpawned().add(armorstandHolder);
				}
			}
		});
	}
}