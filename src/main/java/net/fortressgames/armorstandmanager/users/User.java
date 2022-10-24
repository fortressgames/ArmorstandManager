package net.fortressgames.armorstandmanager.users;

import lombok.Getter;
import lombok.Setter;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandHolder;
import net.fortressgames.armorstandmanager.utils.Type;

import java.util.ArrayList;
import java.util.List;

public class User {

	@Setter @Getter private Type type = Type.HEAD;
	@Setter @Getter	private ArmorstandHolder targetAS;

	@Getter private final List<ArmorstandHolder> spawned = new ArrayList<>();
}