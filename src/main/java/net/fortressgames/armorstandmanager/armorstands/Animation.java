package net.fortressgames.armorstandmanager.armorstands;

import lombok.Getter;
import lombok.SneakyThrows;
import net.fortressgames.armorstandmanager.ArmorstandManager;
import net.fortressgames.fortressapi.entities.CustomArmorstand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.EulerAngle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Animation {

	@Getter private final String name;
	@Getter private final File file;

	@Getter private final List<AnimationState> animationStates = new ArrayList<>();//TODO save this and load it

	@SneakyThrows
	public Animation(String name) {
		this.name = name;

		this.file = new File(ArmorstandManager.getInstance().getDataFolder() + "/Animations/" + name);
		if(!file.exists()) file.mkdir();

		load();
	}

	public void play(ArmorstandHolder armorstandHolder) {
		AnimationTask animationTask = new AnimationTask(armorstandHolder, this);
		animationTask.runTaskTimer(ArmorstandManager.getInstance(), TimeUnit.MILLISECONDS, 1);

		AnimationModule.getInstance().getRunning().put(armorstandHolder.getCustomArmorstand(), animationTask);
	}

	public void load() {
		for(File file : file.listFiles()) {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

			Location location = new Location(Bukkit.getWorld(config.getString("Location.World")),
					config.getDouble("Location.X"),
					config.getDouble("Location.Y"),
					config.getDouble("Location.Z"),
					config.getInt("Location.Yaw"),
					config.getInt("Location.Pitch"));

			Bukkit.getScheduler().runTask(ArmorstandManager.getInstance(), () -> {
				CustomArmorstand customArmorstand = new CustomArmorstand(location);
				AnimationState animationState = new AnimationState(customArmorstand);
				animationState.setTicks(config.getInt("ticks"));

				animationStates.add(animationState);

				customArmorstand.setItemInMainHand(config.getItemStack("Items.MainHand"));
				customArmorstand.setItemInOffHand(config.getItemStack("Items.OffHand"));
				customArmorstand.setHelmet(config.getItemStack("Items.Helmet"));
				customArmorstand.setChestplate(config.getItemStack("Items.Chestplate"));
				customArmorstand.setLeggings(config.getItemStack("Items.Leggings"));
				customArmorstand.setBoots(config.getItemStack("Items.Boots"));

				customArmorstand.setHeadPose(new EulerAngle(
						config.getDouble("HeadPos.X"),
						config.getDouble("HeadPos.Y"),
						config.getDouble("HeadPos.Z")));

				customArmorstand.setBodyPose(new EulerAngle(
						config.getDouble("BodyPos.X"),
						config.getDouble("BodyPos.Y"),
						config.getDouble("BodyPos.Z")));

				customArmorstand.setLeftArmPose(new EulerAngle(
						config.getDouble("LeftArmPos.X"),
						config.getDouble("LeftArmPos.Y"),
						config.getDouble("LeftArmPos.Z")));

				customArmorstand.setRightArmPose(new EulerAngle(
						config.getDouble("RightArmPos.X"),
						config.getDouble("RightArmPos.Y"),
						config.getDouble("RightArmPos.Z")));

				customArmorstand.setLeftLegPose(new EulerAngle(
						config.getDouble("LeftLegPos.X"),
						config.getDouble("LeftLegPos.Y"),
						config.getDouble("LeftLegPos.Z")));

				customArmorstand.setRightLegPose(new EulerAngle(
						config.getDouble("RightLegPos.X"),
						config.getDouble("RightLegPos.Y"),
						config.getDouble("RightLegPos.Z")));

				customArmorstand.setBasePlate(config.getBoolean("BasePlate"));
				customArmorstand.setInvisible(config.getBoolean("Invisible"));
				customArmorstand.setArms(config.getBoolean("Arms"));
				customArmorstand.setSmall(config.getBoolean("Small"));
				customArmorstand.setGlowing(config.getBoolean("Glowing"));
				customArmorstand.setCustomNameVisible(config.getBoolean("CustomNameVisible"));

				if(config.contains("CustomName")) {
					customArmorstand.setCustomName(config.getString("CustomName"));
				}
			});
		}
	}
}