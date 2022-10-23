package net.fortressgames.armorstandmanager.armorstands;

import lombok.SneakyThrows;
import net.fortressgames.armorstandmanager.ASLang;
import net.fortressgames.armorstandmanager.ArmorstandManager;
import net.fortressgames.armorstandmanager.users.UserModule;
import net.fortressgames.fortressapi.entities.CustomArmorstand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

import java.io.File;

public class ArmorstandUtils {

	public static boolean hasTarget(Player player) {
		if(UserModule.getInstance().getUser(player).getTargetAS() != null) {
			return true;
		}

		player.sendMessage(ASLang.NO_AS_SELECTED);
		return false;
	}

	public static void load() {
		for(File file : new File(ArmorstandManager.getInstance().getDataFolder() + "/Armorstands").listFiles()) {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

			Location location = new Location(Bukkit.getWorld(config.getString("Location.World")),
					config.getDouble("Location.X"),
					config.getDouble("Location.Y"),
					config.getDouble("Location.Z"),
					config.getInt("Location.Yaw"),
					config.getInt("Location.Pitch"));

			Bukkit.getScheduler().runTask(ArmorstandManager.getInstance(), () -> {
				ArmorstandHolder armorstandHolder = new ArmorstandHolder(new CustomArmorstand(location),
						config.getInt("RenderDistance"),
						Integer.parseInt(file.getName().replace(".yml", "")));

				armorstandHolder.setLock(config.getBoolean("Lock"));

				armorstandHolder.getCustomArmorstand().setItemInMainHand(config.getItemStack("Items.MainHand"));
				armorstandHolder.getCustomArmorstand().setItemInOffHand(config.getItemStack("Items.OffHand"));
				armorstandHolder.getCustomArmorstand().setHelmet(config.getItemStack("Items.Helmet"));
				armorstandHolder.getCustomArmorstand().setChestplate(config.getItemStack("Items.Chestplate"));
				armorstandHolder.getCustomArmorstand().setLeggings(config.getItemStack("Items.Leggings"));
				armorstandHolder.getCustomArmorstand().setBoots(config.getItemStack("Items.Boots"));

				armorstandHolder.getCustomArmorstand().setHeadPose(new EulerAngle(
						config.getDouble("HeadPos.X"),
						config.getDouble("HeadPos.Y"),
						config.getDouble("HeadPos.Z")));

				armorstandHolder.getCustomArmorstand().setBodyPose(new EulerAngle(
						config.getDouble("BodyPos.X"),
						config.getDouble("BodyPos.Y"),
						config.getDouble("BodyPos.Z")));

				armorstandHolder.getCustomArmorstand().setLeftArmPose(new EulerAngle(
						config.getDouble("LeftArmPos.X"),
						config.getDouble("LeftArmPos.Y"),
						config.getDouble("LeftArmPos.Z")));

				armorstandHolder.getCustomArmorstand().setRightArmPose(new EulerAngle(
						config.getDouble("RightArmPos.X"),
						config.getDouble("RightArmPos.Y"),
						config.getDouble("RightArmPos.Z")));

				armorstandHolder.getCustomArmorstand().setLeftLegPose(new EulerAngle(
						config.getDouble("LeftLegPos.X"),
						config.getDouble("LeftLegPos.Y"),
						config.getDouble("LeftLegPos.Z")));

				armorstandHolder.getCustomArmorstand().setRightLegPose(new EulerAngle(
						config.getDouble("RightLegPos.X"),
						config.getDouble("RightLegPos.Y"),
						config.getDouble("RightLegPos.Z")));

				armorstandHolder.getCustomArmorstand().setBasePlate(config.getBoolean("BasePlate"));
				armorstandHolder.getCustomArmorstand().setInvisible(config.getBoolean("Invisible"));
				armorstandHolder.getCustomArmorstand().setArms(config.getBoolean("Arms"));
				armorstandHolder.getCustomArmorstand().setSmall(config.getBoolean("Small"));
				armorstandHolder.getCustomArmorstand().setGlowing(config.getBoolean("Glowing"));
				armorstandHolder.getCustomArmorstand().setNoGravity(config.getBoolean("NoGravity"));
				armorstandHolder.getCustomArmorstand().setCustomNameVisible(config.getBoolean("CustomNameVisible"));
				armorstandHolder.getCustomArmorstand().setSilent(config.getBoolean("Silent"));

				if(config.contains("CustomName")) {
					armorstandHolder.getCustomArmorstand().setCustomName(config.getString("CustomName"));
				}

				ArmorstandModule.getInstance().getCustomArmorstands().add(armorstandHolder);

				if(armorstandHolder.getRenderDistance() != -1) {
					ArmorstandModule.getInstance().getRenderList().add(armorstandHolder);
				}
			});
		}
	}

	@SneakyThrows
	public static void save(ArmorstandHolder armorstand) {
		File file = armorstand.getFile();
		file.createNewFile();
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("ID", armorstand.getID());
		config.set("RenderDistance", armorstand.getRenderDistance());
		config.set("Lock", armorstand.isLock());

		config.set("Location.World", armorstand.getCustomArmorstand().getLocation().getWorld().getName());
		config.set("Location.X", armorstand.getCustomArmorstand().getLocation().getX());
		config.set("Location.Y", armorstand.getCustomArmorstand().getLocation().getY());
		config.set("Location.Z", armorstand.getCustomArmorstand().getLocation().getZ());
		config.set("Location.Yaw", armorstand.getCustomArmorstand().getLocation().getYaw());
		config.set("Location.Pitch", armorstand.getCustomArmorstand().getLocation().getPitch());

		config.set("Items.MainHand", armorstand.getCustomArmorstand().getItemInMainHand());
		config.set("Items.OffHand", armorstand.getCustomArmorstand().getItemInOffHand());
		config.set("Items.Helmet", armorstand.getCustomArmorstand().getHelmet());
		config.set("Items.Chestplate", armorstand.getCustomArmorstand().getChestplate());
		config.set("Items.Leggings", armorstand.getCustomArmorstand().getLeggings());
		config.set("Items.Boots", armorstand.getCustomArmorstand().getBoots());

		config.set("HeadPos.X", armorstand.getCustomArmorstand().getHeadPose().getX());
		config.set("HeadPos.Y", armorstand.getCustomArmorstand().getHeadPose().getY());
		config.set("HeadPos.Z", armorstand.getCustomArmorstand().getHeadPose().getZ());

		config.set("BodyPos.X", armorstand.getCustomArmorstand().getBodyPose().getX());
		config.set("BodyPos.Y", armorstand.getCustomArmorstand().getBodyPose().getY());
		config.set("BodyPos.Z", armorstand.getCustomArmorstand().getBodyPose().getZ());

		config.set("LeftArmPos.X", armorstand.getCustomArmorstand().getLeftArmPose().getX());
		config.set("LeftArmPos.Y", armorstand.getCustomArmorstand().getLeftArmPose().getY());
		config.set("LeftArmPos.Z", armorstand.getCustomArmorstand().getLeftArmPose().getZ());

		config.set("RightArmPos.X", armorstand.getCustomArmorstand().getRightArmPose().getX());
		config.set("RightArmPos.Y", armorstand.getCustomArmorstand().getRightArmPose().getY());
		config.set("RightArmPos.Z", armorstand.getCustomArmorstand().getRightArmPose().getZ());

		config.set("LeftLegPos.X", armorstand.getCustomArmorstand().getLeftLegPose().getX());
		config.set("LeftLegPos.Y", armorstand.getCustomArmorstand().getLeftLegPose().getY());
		config.set("LeftLegPos.Z", armorstand.getCustomArmorstand().getLeftLegPose().getZ());

		config.set("RightLegPos.X", armorstand.getCustomArmorstand().getRightLegPose().getX());
		config.set("RightLegPos.Y", armorstand.getCustomArmorstand().getRightLegPose().getY());
		config.set("RightLegPos.Z", armorstand.getCustomArmorstand().getRightLegPose().getZ());

		config.set("BasePlate", armorstand.getCustomArmorstand().isBasePlate());
		config.set("Invisible", armorstand.getCustomArmorstand().isInvisible());
		config.set("Arms", armorstand.getCustomArmorstand().isArms());
		config.set("Small", armorstand.getCustomArmorstand().isSmall());
		config.set("Glowing", armorstand.getCustomArmorstand().isGlowing());
		config.set("NoGravity", armorstand.getCustomArmorstand().isNoGravity());
		config.set("CustomNameVisible", armorstand.getCustomArmorstand().isCustomNameVisible());
		config.set("Silent", armorstand.getCustomArmorstand().isSilent());

		config.set("CustomName", armorstand.getCustomArmorstand().getCustomName());

		config.save(file);
	}
}