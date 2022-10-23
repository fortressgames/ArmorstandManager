package net.fortressgames.armorstandmanager.armorstands;

import lombok.Getter;
import net.fortressgames.armorstandmanager.ArmorstandManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class ArmorstandModule {

	private static ArmorstandModule instance;

	@Getter private final List<ArmorstandHolder> customArmorstands = new ArrayList<>();
	@Getter private final List<ArmorstandHolder> renderList = new ArrayList<>();

	private int ASNumberID = 0;

	public ArmorstandModule() {
		if(ArmorstandManager.getInstance().getConfig().contains("ASNumberID")) {
			ASNumberID = ArmorstandManager.getInstance().getConfig().getInt("ASNumberID");
		}
	}

	public int getNextID() {
		this.ASNumberID++;
		ArmorstandManager.getInstance().getConfig().set("ASNumberID", this.ASNumberID);
		ArmorstandManager.getInstance().saveConfig();
		return ASNumberID;
	}

	public void add(ArmorstandHolder armorstandHolder) {
		this.customArmorstands.add(armorstandHolder);

		ArmorstandUtils.save(armorstandHolder);
	}

	public void addRender(ArmorstandHolder armorstandHolder) {
		this.customArmorstands.add(armorstandHolder);
		this.renderList.add(armorstandHolder);

		ArmorstandUtils.save(armorstandHolder);
	}

	public void remove(ArmorstandHolder armorstandHolder) {
		this.customArmorstands.remove(armorstandHolder);
		this.renderList.remove(armorstandHolder);

		armorstandHolder.getFile().delete();

		Bukkit.getOnlinePlayers().forEach(armorstandHolder.getCustomArmorstand()::remove);
	}

	public static ArmorstandModule getInstance() {
		if(instance == null) {
			instance = new ArmorstandModule();
		}

		return instance;
	}
}