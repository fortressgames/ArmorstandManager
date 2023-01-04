package net.fortressgames.armorstandmanager.armorstands;

import lombok.Getter;
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
import java.util.ArrayList;
import java.util.List;

public class ArmorstandModule {

	private static ArmorstandModule instance;
	@Getter private final List<ArmorstandHolder> customArmorstands = new ArrayList<>();

	private int ASNumberID = 0;

	public ArmorstandModule() {
		if(ArmorstandManager.getInstance().getConfig().contains("ASNumberID")) {
			ASNumberID = ArmorstandManager.getInstance().getConfig().getInt("ASNumberID");
		}
	}

	public static ArmorstandModule getInstance() {
		if(instance == null) {
			instance = new ArmorstandModule();
		}
		return instance;
	}

	public int getNextID() {
		this.ASNumberID++;
		ArmorstandManager.getInstance().getConfig().set("ASNumberID", this.ASNumberID);
		ArmorstandManager.getInstance().saveConfig();
		return ASNumberID;
	}

	public void add(ArmorstandHolder armorstandHolder) {
		this.customArmorstands.add(armorstandHolder);

		armorstandHolder.save();
	}

	public void remove(ArmorstandHolder armorstandHolder) {
		this.customArmorstands.remove(armorstandHolder);

		armorstandHolder.deleteFile();
		Bukkit.getOnlinePlayers().forEach(armorstandHolder.getCustomArmorstand()::remove);
	}

	public static boolean hasTarget(Player player) {
		if(UserModule.getInstance().getUser(player).getTargetAS() != null) {
			return true;
		}

		player.sendMessage(ASLang.NO_AS_SELECTED);
		return false;
	}

	public static boolean hasTargetSilent(Player player) {
		return UserModule.getInstance().getUser(player).getTargetAS() != null;
	}

	public void load() {
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
						Integer.parseInt(file.getName().replace(".yml", "")),
						config.getInt("RenderDistance"));

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

				if(config.contains("Animation")) {
					armorstandHolder.setAnimation(AnimationModule.getInstance().getAnimation(config.getString("Animation")));
				}

				if(ArmorstandManager.getInstance().getConfig().getStringList("Animation-Loop").contains(String.valueOf(armorstandHolder.getID()))) {
					armorstandHolder.setLoopAnimation(true);
				}

				ArmorstandModule.getInstance().getCustomArmorstands().add(armorstandHolder);

				if(ArmorstandManager.getInstance().getConfig().getStringList("Animation-RunOnStart").contains(String.valueOf(armorstandHolder.getID()))) {
					armorstandHolder.getAnimation().play(armorstandHolder, false);
				}
			});
		}
	}
}