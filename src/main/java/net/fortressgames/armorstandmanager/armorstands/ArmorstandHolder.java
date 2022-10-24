package net.fortressgames.armorstandmanager.armorstands;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.fortressgames.armorstandmanager.ArmorstandManager;
import net.fortressgames.fortressapi.entities.CustomArmorstand;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ArmorstandHolder {

	private final File file;
	private final YamlConfiguration configuration;
	@Getter private final CustomArmorstand customArmorstand;
	@Getter private final int ID;

	@Setter	@Getter private boolean lock;
	@Setter	@Getter private double renderDistance;

	public ArmorstandHolder(CustomArmorstand customArmorstand, int ID, int renderDistance) {
		this.customArmorstand = customArmorstand;
		this.ID = ID;
		this.renderDistance = renderDistance;
		this.file = new File(ArmorstandManager.getInstance().getDataFolder() + "/Armorstands/" + ID + ".yml");
		this.configuration = YamlConfiguration.loadConfiguration(file);
	}

	public void deleteFile() {
		file.delete();
	}

	public void save() {
		saveItems();
		saveLocation();
		savePose();
		saveID();
		saveRender();
		saveLock();
		saveAttributes();
		saveName();
	}

	@SneakyThrows
	public void saveItems() {
		configuration.set("Items.MainHand", customArmorstand.getItemInMainHand());
		configuration.set("Items.OffHand", customArmorstand.getItemInOffHand());
		configuration.set("Items.Helmet", customArmorstand.getHelmet());
		configuration.set("Items.Chestplate", customArmorstand.getChestplate());
		configuration.set("Items.Leggings", customArmorstand.getLeggings());
		configuration.set("Items.Boots", customArmorstand.getBoots());

		configuration.save(file);
	}

	@SneakyThrows
	public void savePose() {
		configuration.set("HeadPos.X", customArmorstand.getHeadPose().getX());
		configuration.set("HeadPos.Y", customArmorstand.getHeadPose().getY());
		configuration.set("HeadPos.Z", customArmorstand.getHeadPose().getZ());

		configuration.set("BodyPos.X", customArmorstand.getBodyPose().getX());
		configuration.set("BodyPos.Y", customArmorstand.getBodyPose().getY());
		configuration.set("BodyPos.Z", customArmorstand.getBodyPose().getZ());

		configuration.set("LeftArmPos.X", customArmorstand.getLeftArmPose().getX());
		configuration.set("LeftArmPos.Y", customArmorstand.getLeftArmPose().getY());
		configuration.set("LeftArmPos.Z", customArmorstand.getLeftArmPose().getZ());

		configuration.set("RightArmPos.X", customArmorstand.getRightArmPose().getX());
		configuration.set("RightArmPos.Y", customArmorstand.getRightArmPose().getY());
		configuration.set("RightArmPos.Z", customArmorstand.getRightArmPose().getZ());

		configuration.set("LeftLegPos.X", customArmorstand.getLeftLegPose().getX());
		configuration.set("LeftLegPos.Y", customArmorstand.getLeftLegPose().getY());
		configuration.set("LeftLegPos.Z", customArmorstand.getLeftLegPose().getZ());

		configuration.set("RightLegPos.X", customArmorstand.getRightLegPose().getX());
		configuration.set("RightLegPos.Y", customArmorstand.getRightLegPose().getY());
		configuration.set("RightLegPos.Z", customArmorstand.getRightLegPose().getZ());

		configuration.save(file);
	}

	@SneakyThrows
	public void saveLocation() {
		configuration.set("Location.World", customArmorstand.getLocation().getWorld().getName());
		configuration.set("Location.X", customArmorstand.getLocation().getX());
		configuration.set("Location.Y", customArmorstand.getLocation().getY());
		configuration.set("Location.Z", customArmorstand.getLocation().getZ());
		configuration.set("Location.Yaw", customArmorstand.getLocation().getYaw());
		configuration.set("Location.Pitch", customArmorstand.getLocation().getPitch());

		configuration.save(file);
	}

	@SneakyThrows
	public void saveRender() {
		configuration.set("RenderDistance", renderDistance);

		configuration.save(file);
	}

	@SneakyThrows
	public void saveID() {
		configuration.set("ID", ID);

		configuration.save(file);
	}

	@SneakyThrows
	public void saveLock() {
		configuration.set("Lock", lock);

		configuration.save(file);
	}

	@SneakyThrows
	public void saveName() {
		configuration.set("CustomName", customArmorstand.getCustomName());

		configuration.save(file);
	}

	@SneakyThrows
	public void saveAttributes() {
		configuration.set("BasePlate", customArmorstand.isBasePlate());
		configuration.set("Invisible", customArmorstand.isInvisible());
		configuration.set("Arms", customArmorstand.isArms());
		configuration.set("Small", customArmorstand.isSmall());
		configuration.set("CustomNameVisible", customArmorstand.isCustomNameVisible());
		configuration.set("Silent", customArmorstand.isSilent());

		configuration.save(file);
	}
}