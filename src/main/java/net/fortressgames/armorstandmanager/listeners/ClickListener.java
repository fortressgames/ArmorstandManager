package net.fortressgames.armorstandmanager.listeners;

import lombok.SneakyThrows;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandHolder;
import net.fortressgames.armorstandmanager.users.User;
import net.fortressgames.armorstandmanager.users.UserModule;
import net.fortressgames.fortressapi.players.PlayerModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.EulerAngle;

public class ClickListener implements Listener {

	@EventHandler
	public void breakBlock(BlockBreakEvent e) {
		Player player = e.getPlayer();

		if(player.getInventory().getItemInMainHand().getType().equals(Material.SPECTRAL_ARROW)) e.setCancelled(true);
	}

	@EventHandler
	public void click(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		User user = UserModule.getInstance().getUser(player);

		if(e.getHand() == EquipmentSlot.OFF_HAND) return;
		if(user.getTargetAS() == null) return;

		double move;

		if(player.getInventory().getItemInMainHand().getType().equals(Material.SPECTRAL_ARROW)) {
			move = 0.1;
		} else {
			return;
		}

		if(!player.getInventory().getItemInMainHand().getItemMeta().hasEnchants()) return;

		if(player.isSneaking()) move = move / 10;

		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR)) {
			move -= (move * 2);
		}

		ArmorstandHolder armorstandHolder = user.getTargetAS();
		movePart(player, armorstandHolder, move, false, false);
	}

	@SneakyThrows
	public static void movePart(Player player, ArmorstandHolder as, double move, boolean clean, boolean set) {
		User user = UserModule.getInstance().getUser(player);

		double x = 0;
		double y = 0;
		double z = 0;

		switch (user.getType()) {
			case HEAD -> {
				x = as.getCustomArmorstand().getHeadPose().getX();
				y = as.getCustomArmorstand().getHeadPose().getY();
				z = as.getCustomArmorstand().getHeadPose().getZ();
			}
			case BODY -> {
				x = as.getCustomArmorstand().getBodyPose().getX();
				y = as.getCustomArmorstand().getBodyPose().getY();
				z = as.getCustomArmorstand().getBodyPose().getZ();
			}
			case RIGHT_ARM -> {
				x = as.getCustomArmorstand().getRightArmPose().getX();
				y = as.getCustomArmorstand().getRightArmPose().getY();
				z = as.getCustomArmorstand().getRightArmPose().getZ();
			}
			case LEFT_ARM -> {
				x = as.getCustomArmorstand().getLeftArmPose().getX();
				y = as.getCustomArmorstand().getLeftArmPose().getY();
				z = as.getCustomArmorstand().getLeftArmPose().getZ();
			}
			case RIGHT_LEG -> {
				x = as.getCustomArmorstand().getRightLegPose().getX();
				y = as.getCustomArmorstand().getRightLegPose().getY();
				z = as.getCustomArmorstand().getRightLegPose().getZ();
			}
			case LEFT_LEG -> {
				x = as.getCustomArmorstand().getLeftLegPose().getX();
				y = as.getCustomArmorstand().getLeftLegPose().getY();
				z = as.getCustomArmorstand().getLeftLegPose().getZ();
			}
			case LOCATION -> {
				x = as.getCustomArmorstand().getLocation().getX();
				y = as.getCustomArmorstand().getLocation().getY();
				z = as.getCustomArmorstand().getLocation().getZ();
			}
			case ROTATION -> x = as.getCustomArmorstand().getLocation().getYaw();
		}

		if(player.getInventory().getHeldItemSlot() >= 4) return;

		switch (player.getInventory().getHeldItemSlot()) {
			case 0 -> {
				if(set) {
					x = move;
				} else {
					x += move;
				}
			}
			case 1 -> {
				if(set) {
					y = move;
				} else {
					y += move;
				}
			}
			case 2 -> {
				if(set) {
					z = move;
				} else {
					z += move;
				}
			}
		}

		switch(user.getType()) {
			case HEAD -> as.getCustomArmorstand().setHeadPose(new EulerAngle(x, y, z));
			case BODY -> as.getCustomArmorstand().setBodyPose(new EulerAngle(x, y, z));
			case RIGHT_ARM -> as.getCustomArmorstand().setRightArmPose(new EulerAngle(x, y, z));
			case LEFT_ARM -> as.getCustomArmorstand().setLeftArmPose(new EulerAngle(x, y, z));
			case RIGHT_LEG -> as.getCustomArmorstand().setRightLegPose(new EulerAngle(x, y, z));
			case LEFT_LEG -> as.getCustomArmorstand().setLeftLegPose(new EulerAngle(x, y, z));
			case LOCATION -> {
				as.getCustomArmorstand().setLocation(new Location(as.getCustomArmorstand().getLocation().getWorld(), x, y, z,
						as.getCustomArmorstand().getLocation().getYaw(),
						as.getCustomArmorstand().getLocation().getPitch()));
			}
			case ROTATION -> {
				if(clean) {
					as.getCustomArmorstand().setLocation(new Location(as.getCustomArmorstand().getLocation().getWorld(), as.getCustomArmorstand().getLocation().getX(), as.getCustomArmorstand().getLocation().getY(), as.getCustomArmorstand().getLocation().getZ(),
							(float) x,
							as.getCustomArmorstand().getLocation().getPitch()));
				} else {
					as.getCustomArmorstand().setLocation(new Location(as.getCustomArmorstand().getLocation().getWorld(), as.getCustomArmorstand().getLocation().getX(), as.getCustomArmorstand().getLocation().getY(), as.getCustomArmorstand().getLocation().getZ(),
							((float) x * 2),
							as.getCustomArmorstand().getLocation().getPitch()));
				}
			}
		}

		as.savePose();
		as.saveLocation();

		PlayerModule.getInstance().getOnlinePlayers().forEach(as.getCustomArmorstand()::update);
	}
}