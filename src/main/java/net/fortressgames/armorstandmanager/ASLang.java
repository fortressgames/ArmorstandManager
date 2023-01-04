package net.fortressgames.armorstandmanager;

import net.fortressgames.fortressapi.Lang;

public class ASLang {

	public static final String NO_AS_SELECTED = Lang.RED + "You need to select a armorstand first!";
	public static final String AS_LOCKED = Lang.RED + "Armorstand locked!";
	public static final String AS_MOVED = Lang.GREEN + "Armorstand moved!";
	public static final String AS_REMOVED = Lang.GREEN + "Armorstand removed!";

	public static final String UNKNOWN_ID = Lang.RED + "Unknown id";

	public static final String ANIMATION_CREATE = Lang.GREEN + "Animation created!";
	public static final String ANIMATION_SET = Lang.GREEN + "Animation set!";
	public static final String ANIMATION_ADD = Lang.GREEN + "Pose added!";
	public static final String ANIMATION_REMOVED = Lang.GREEN + "Pose removed!";
	public static final String ANIMATION_STOPPED = Lang.GREEN + "Animation stopped!";
	public static final String ANIMATION_UNKNOWN = Lang.RED + "Armorstand does not have an animation!";
	public static final String ANIMATION_RUNNING = Lang.RED + "Armorstand animation already running!";
	public static final String ANIMATION_UPDATE = Lang.GREEN + "Animation state updated!";
	public static final String ANIMATION_UPDATE_TICKS = Lang.GREEN + "Animation ticks updated!";

	public static String ASRender(String render) {
		return Lang.GREEN + "Armorstand render set to " + render + "!";
	}
	public static String ASToggle(String value) {
		return Lang.GREEN + "Armorstands toggled " + value + "!";
	}
	public static String toggleLoop(String value) {
		return Lang.GREEN + "Loop toggled " + value + "!";
	}


	public static final String ERROR_NUMBER_FORMAT = Lang.RED + "Input error. Input needs to be a number!";
	public static final String ERROR_INDEX_OUT_OF_BOUNDS = Lang.RED + "Input error. Cannot find that number!";
}