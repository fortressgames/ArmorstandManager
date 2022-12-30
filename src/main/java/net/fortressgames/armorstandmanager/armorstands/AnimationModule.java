package net.fortressgames.armorstandmanager.armorstands;

import lombok.Getter;
import net.fortressgames.armorstandmanager.ArmorstandManager;
import net.fortressgames.fortressapi.entities.CustomArmorstand;

import java.io.File;
import java.util.HashMap;

public class AnimationModule {

	private static AnimationModule instance;
	private final HashMap<String, Animation> animations = new HashMap<>();
	@Getter private final HashMap<CustomArmorstand, AnimationTask> running = new HashMap<>();

	public static AnimationModule getInstance() {
		if(instance == null) {
			instance = new AnimationModule();
		}
		return instance;
	}

	public Animation getAnimation(String name) {
		return this.animations.get(name);
	}

	public void create(String name) {
		this.animations.put(name, new Animation(name));
	}

	public void load() {
		for(File file : new File(ArmorstandManager.getInstance().getDataFolder() + "/Animations").listFiles()) {
			String name = file.getName().replace(".yml", "");

			this.animations.put(name, new Animation(name));
		}
	}
}