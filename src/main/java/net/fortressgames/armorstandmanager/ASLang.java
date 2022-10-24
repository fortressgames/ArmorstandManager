package net.fortressgames.armorstandmanager;

import net.fortressgames.fortressapi.Lang;

public class ASLang {

	public static final String NO_AS_SELECTED = Lang.RED + "You need to select a armorstand first!";
	public static final String AS_LOCKED = Lang.RED + "Armorstand locked!";
	public static final String AS_MOVED = Lang.GREEN + "Armorstand moved!";
	public static final String AS_REMOVED = Lang.GREEN + "Armorstand removed!";

	public static final String INVALID_NUMBER = Lang.RED + "Value is not a number!";
	public static final String UNKNOWN_ID = Lang.RED + "Unknown id";

	public static String ASRender(String render) {
		return Lang.GREEN + "Armorstand render set to " + render + "!";
	}
	public static String ASToggle(String value) {
		return Lang.GREEN + "Armorstands toggled " + value + "!";
	}
}