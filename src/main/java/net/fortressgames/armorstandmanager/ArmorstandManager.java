package net.fortressgames.armorstandmanager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandHolder;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandModule;
import net.fortressgames.armorstandmanager.inventories.ArmorstandEditInv;
import net.fortressgames.armorstandmanager.listeners.AnvilListener;
import net.fortressgames.armorstandmanager.listeners.ClickListener;
import net.fortressgames.armorstandmanager.listeners.PlayerMoveListener;
import net.fortressgames.armorstandmanager.listeners.SpawnArmorstandListener;
import net.fortressgames.armorstandmanager.users.UserModule;
import net.fortressgames.fortressapi.commands.CommandModule;
import net.fortressgames.fortressapi.players.PlayerModule;
import net.fortressgames.fortressapi.utils.ConsoleMessage;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
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
	}

	/**
	 * Called when the plugin is first loaded by Spigot
	 */
	@Override
	public void onEnable() {
		instance = this;

		Bukkit.getScheduler().runTaskAsynchronously(this, ArmorstandModule::load);

		// Listeners
		this.getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
		this.getServer().getPluginManager().registerEvents(new AnvilListener(), this);
		this.getServer().getPluginManager().registerEvents(new SpawnArmorstandListener(), this);
		this.getServer().getPluginManager().registerEvents(new ClickListener(), this);
		this.getServer().getPluginManager().registerEvents(UserModule.getInstance(), this);

		PlayerModule.getInstance().getOnlinePlayers().forEach(UserModule.getInstance()::addUser);

		// Commands
		CommandModule.registerCommand(new ASCommand());

		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {

			@SneakyThrows
			public void onPacketReceiving(PacketEvent e) {
				if(e.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
					PacketContainer packet = e.getPacket();

					if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.ARROW) ||
							e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SPECTRAL_ARROW)) return;

					if(!e.getPlayer().hasPermission("as.use")) return;

					for(ArmorstandHolder armorstandHolder : new ArrayList<>(ArmorstandModule.getInstance().getCustomArmorstands())) {
						if(armorstandHolder.getCustomArmorstand().getId() == packet.getIntegers().read(0)) {
							PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity) packet.getHandle();

							Field valueB = useEntity.getClass().getDeclaredField("b");
							valueB.setAccessible(true);
							Object objectValueB = valueB.get(useEntity);

							try {
								objectValueB.getClass().getDeclaredField("a");
								//LEFT CLICK
								Bukkit.getScheduler().runTask(instance, () -> new ArmorstandEditInv(e.getPlayer(), armorstandHolder).openInventory());

							} catch (NoSuchFieldException ee) {
								//RIGHT CLICK

								if(e.getPlayer().getGameMode() == GameMode.CREATIVE) {

									if(armorstandHolder.isLock()) {
										e.getPlayer().sendMessage(ASLang.AS_LOCKED);
										return;
									}

									ArmorstandModule.getInstance().remove(armorstandHolder);
									e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 100, 1);
								}
							}
						}
					}
				}
			}
		});

		getLogger().info(ConsoleMessage.GREEN + "Version: " + getDescription().getVersion() + " Enabled!" + ConsoleMessage.RESET);
	}

	/**
	 * Called when the server is restarted or stopped
	 */
	@Override
	public void onDisable() {
		getLogger().info(ConsoleMessage.RED + "Version: " + getDescription().getVersion() + " Disabled!" + ConsoleMessage.RESET);
	}
}