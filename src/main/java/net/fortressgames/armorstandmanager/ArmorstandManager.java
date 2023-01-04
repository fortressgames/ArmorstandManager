package net.fortressgames.armorstandmanager;

import lombok.Getter;
import lombok.Setter;
import net.fortressgames.armorstandmanager.armorstands.AnimationModule;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandModule;
import net.fortressgames.armorstandmanager.listeners.AnvilListener;
import net.fortressgames.armorstandmanager.listeners.ClickListener;
import net.fortressgames.armorstandmanager.listeners.PlayerMoveListener;
import net.fortressgames.armorstandmanager.listeners.SpawnArmorstandListener;
import net.fortressgames.armorstandmanager.users.UserModule;
import net.fortressgames.fortressapi.commands.CommandModule;
import net.fortressgames.fortressapi.players.PlayerModule;
import net.fortressgames.fortressapi.utils.ConsoleMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class ArmorstandManager extends JavaPlugin {

	@Getter	private static ArmorstandManager instance;
	@Setter	@Getter private boolean toggle = true;

	/**
	 * Called when plugin first loads by spigot and is called before onEnable
	 */
	@Override
	public void onLoad() {
		// Create Default folder
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		File as = new File(getDataFolder() + "/Armorstands");
		if(!as.exists()) {
			as.mkdir();
		}
		File asa = new File(getDataFolder() + "/Animations");
		if(!asa.exists()) {
			asa.mkdir();
		}

		if(!getConfig().contains("Animation-Loop")) {
			getConfig().set("Animation-Loop", new ArrayList<>());
		}
		if(!getConfig().contains("Animation-RunOnStart")) {
			getConfig().set("Animation-RunOnStart", new ArrayList<>());
		}

		saveConfig();
	}

	/**
	 * Called when the plugin is first loaded by Spigot
	 */
	@Override
	public void onEnable() {
		instance = this;

		Bukkit.getScheduler().runTaskAsynchronously(this, AnimationModule.getInstance()::load);
		Bukkit.getScheduler().runTaskAsynchronously(this, ArmorstandModule.getInstance()::load);

		// Listeners
		this.getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
		this.getServer().getPluginManager().registerEvents(new AnvilListener(), this);
		this.getServer().getPluginManager().registerEvents(new SpawnArmorstandListener(), this);
		this.getServer().getPluginManager().registerEvents(new ClickListener(), this);
		this.getServer().getPluginManager().registerEvents(UserModule.getInstance(), this);

		PlayerModule.getInstance().getOnlinePlayers().forEach(UserModule.getInstance()::addUser);

		// Commands
		CommandModule.registerCommand(new ASCommand());
		CommandModule.registerCommand(new ASACommand());

		getLogger().info(ConsoleMessage.GREEN + "Version: " + getDescription().getVersion() + " Enabled!" + ConsoleMessage.RESET);
	}

	/**
	 * Called when the server is restarted or stopped
	 */
	@Override
	public void onDisable() {
		PlayerModule.getInstance().getOnlinePlayers().forEach(pp -> {
			UserModule.getInstance().getUser(pp).getSpawned().forEach(armorstandHolder -> armorstandHolder.getCustomArmorstand().remove(pp));
		});

		getLogger().info(ConsoleMessage.RED + "Version: " + getDescription().getVersion() + " Disabled!" + ConsoleMessage.RESET);
	}
}

//TODO (BUGS)
// animation gets lost after reload sometimes
// playing normal then reverse adds a delay before stating the first frame