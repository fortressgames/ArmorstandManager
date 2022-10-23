package net.fortressgames.armorstandmanager.armorstands;

import lombok.Getter;
import lombok.Setter;
import net.fortressgames.armorstandmanager.ArmorstandManager;
import net.fortressgames.fortressapi.entities.CustomArmorstand;

import java.io.File;

public class ArmorstandHolder {

	@Getter private final File file;
	@Getter private final CustomArmorstand customArmorstand;
	@Getter private final int ID;

	@Setter	@Getter private boolean lock;
	@Setter	@Getter private double renderDistance = -1;

	public ArmorstandHolder(CustomArmorstand customArmorstand, int ID, int renderDistance) {
		this.customArmorstand = customArmorstand;
		this.ID = ID;
		this.renderDistance = renderDistance;
		this.file = new File(ArmorstandManager.getInstance().getDataFolder() + "/Armorstands/" + ID + ".yml");
	}
}