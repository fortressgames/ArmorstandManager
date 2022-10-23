package net.fortressgames.armorstandmanager.listeners;

import net.fortressgames.armorstandmanager.ArmorstandManager;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandModule;
import net.fortressgames.armorstandmanager.users.User;
import net.fortressgames.armorstandmanager.users.UserModule;
import net.fortressgames.fortressapi.events.PlayerMoveTaskEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class PlayerMoveListener implements Listener {

	@EventHandler
	public void move(PlayerMoveTaskEvent e) {
		Player player = e.getPlayer();
		User user = UserModule.getInstance().getUser(player);

		new ArrayList<>(user.getSpawned()).forEach(armorstandHolder -> {
			if(armorstandHolder.getRenderDistance() == -1) {
				user.getSpawned().remove(armorstandHolder);
			}
		});

		if(!player.isOnline()) return;

		if(!ArmorstandManager.getInstance().isToggle()) {
			user.getSpawned().clear();
			return;
		}

		ArmorstandModule.getInstance().getRenderList().forEach(armorstandHolder -> {

			if(!player.getWorld().equals(armorstandHolder.getCustomArmorstand().getLocation().getWorld())) return;

			double dis = player.getLocation().distance(armorstandHolder.getCustomArmorstand().getLocation());

			if(dis <= armorstandHolder.getRenderDistance()) {

				if(user.getSpawned().contains(armorstandHolder)) return;

				armorstandHolder.getCustomArmorstand().spawn(player);
				user.getSpawned().add(armorstandHolder);
			} else {

				armorstandHolder.getCustomArmorstand().remove(player);
				user.getSpawned().remove(armorstandHolder);
			}
		});
	}
}