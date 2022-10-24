package net.fortressgames.armorstandmanager.listeners;

import net.fortressgames.armorstandmanager.armorstands.ArmorstandHolder;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandModule;
import net.fortressgames.fortressapi.entities.CustomArmorstand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.EulerAngle;

public class SpawnArmorstandListener implements Listener {

	@EventHandler
	public void place(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if(e.getHand() == EquipmentSlot.OFF_HAND) return;
		if(e.getClickedBlock() == null) return;

		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(player.getInventory().getItemInMainHand().getType().equals(Material.ARMOR_STAND)) {
				e.setCancelled(true);

				ArmorstandHolder armorstandHolder = new ArmorstandHolder(new CustomArmorstand(
						new Location(player.getWorld(),
								e.getClickedBlock().getX() + 0.5,
								e.getClickedBlock().getY() +1,
								e.getClickedBlock().getZ() + 0.5,
								(Math.round(player.getLocation().getYaw() / 90) * 90) + 180,
								0)
				), ArmorstandModule.getInstance().getNextID(), 20);

				armorstandHolder.getCustomArmorstand().setHeadPose(new EulerAngle(0, 0, 0));
				armorstandHolder.getCustomArmorstand().setBodyPose(new EulerAngle(0, 0, 0));
				armorstandHolder.getCustomArmorstand().setLeftArmPose(new EulerAngle(0, 0, 0));
				armorstandHolder.getCustomArmorstand().setRightArmPose(new EulerAngle(0, 0, 0));
				armorstandHolder.getCustomArmorstand().setLeftLegPose(new EulerAngle(0, 0, 0));
				armorstandHolder.getCustomArmorstand().setRightLegPose(new EulerAngle(0, 0, 0));

				ArmorstandModule.getInstance().add(armorstandHolder);
			}
		}
	}

	@EventHandler
	public void spawn(EntitySpawnEvent e) {
		if(e.getEntity() instanceof ArmorStand) {
			e.setCancelled(true);
		}
	}
}