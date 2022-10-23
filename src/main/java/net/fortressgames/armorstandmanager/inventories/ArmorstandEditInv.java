package net.fortressgames.armorstandmanager.inventories;

import net.fortressgames.armorstandmanager.ArmorstandManager;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandHolder;
import net.fortressgames.armorstandmanager.listeners.AnvilListener;
import net.fortressgames.armorstandmanager.users.User;
import net.fortressgames.armorstandmanager.users.UserModule;
import net.fortressgames.armorstandmanager.utils.Type;
import net.fortressgames.fortressapi.gui.InventoryMenu;
import net.fortressgames.fortressapi.gui.InventoryRows;
import net.fortressgames.fortressapi.gui.ItemBuilder;
import net.fortressgames.fortressapi.utils.Enchantment;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmorstandEditInv extends InventoryMenu {

	private BukkitTask armor;

	public ArmorstandEditInv(Player player, ArmorstandHolder armorstand) {
		super(player, InventoryRows.ROW6, ChatColor.BLUE + ChatColor.BOLD.toString() + "Armorstand: " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', armorstand.getCustomArmorstand().getCustomName()));

		File file = new File(ArmorstandManager.getInstance().getDataFolder() + "/Armorstands/" + armorstand.getID() + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		User user = UserModule.getInstance().getUser(player);

		List<Integer> slots = new ArrayList<>(Arrays.asList(46, 48, 53, 36, 37, 38, 45, 47, 0, 2, 3, 12, 18, 20, 21, 27, 29, 30, 31, 32, 33, 34, 35, 13, 22, 23, 24, 25, 4, 26));
		for(Integer slot : slots) {
			this.setItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build(), slot, true);
		}

		setCanMove(true);

		this.setItem(armorstand.getCustomArmorstand().getHelmet(), 1);
		this.setItem(armorstand.getCustomArmorstand().getChestplate(), 10);
		this.setItem(armorstand.getCustomArmorstand().getLeggings(), 19);
		this.setItem(armorstand.getCustomArmorstand().getBoots(), 28);
		this.setItem(armorstand.getCustomArmorstand().getItemInMainHand(), 9);
		this.setItem(armorstand.getCustomArmorstand().getItemInOffHand(), 11);

		boolean silent = armorstand.getCustomArmorstand().isSilent();
		armorstand.getCustomArmorstand().setSilent(true);

		armor = Bukkit.getScheduler().runTaskTimerAsynchronously(ArmorstandManager.getInstance(), () -> {
			if(!player.getOpenInventory().getTopInventory().equals(getInventory())) {
				armor.cancel();
				armorstand.getCustomArmorstand().setSilent(silent);
			}

			Bukkit.getScheduler().runTask(ArmorstandManager.getInstance(), () -> {
				armorstand.getCustomArmorstand().setHelmet(getInventory().getItem(1));
				armorstand.getCustomArmorstand().setChestplate(getInventory().getItem(10));
				armorstand.getCustomArmorstand().setLeggings(getInventory().getItem(19));
				armorstand.getCustomArmorstand().setBoots(getInventory().getItem(28));
				armorstand.getCustomArmorstand().setItemInMainHand(getInventory().getItem(9));
				armorstand.getCustomArmorstand().setItemInOffHand(getInventory().getItem(11));

				Bukkit.getOnlinePlayers().forEach(armorstand.getCustomArmorstand()::updateArmor);

				config.set("Items.MainHand", armorstand.getCustomArmorstand().getItemInMainHand());
				config.set("Items.OffHand", armorstand.getCustomArmorstand().getItemInOffHand());
				config.set("Items.Helmet", armorstand.getCustomArmorstand().getHelmet());
				config.set("Items.Chestplate", armorstand.getCustomArmorstand().getChestplate());
				config.set("Items.Leggings", armorstand.getCustomArmorstand().getLeggings());
				config.set("Items.Boots", armorstand.getCustomArmorstand().getBoots());
				try {
					config.save(file);
				} catch (IOException ignored) {}
			});

		}, 0, 3);

		this.setItem(pose(Type.HEAD, Material.CHAINMAIL_HELMET, "Head"), 5, inventoryClickEvent -> {

			user.setType(Type.HEAD);
			player.closeInventory();

			user.setTargetAS(armorstand);

			setArrows();
			this.updateItem(pose(Type.HEAD, Material.CHAINMAIL_HELMET, "Head"), 5);
		}, true);

		this.setItem(pose(Type.BODY, Material.GOLDEN_CHESTPLATE, "Body"), 6, inventoryClickEvent -> {

			user.setType(Type.BODY);
			player.closeInventory();

			user.setTargetAS(armorstand);

			setArrows();
			this.updateItem(pose(Type.BODY, Material.GOLDEN_CHESTPLATE, "Body"), 6);
		}, true);

		this.setItem(pose(Type.RIGHT_ARM, Material.DIAMOND_PICKAXE, "Right Arm"), 7, inventoryClickEvent -> {

			user.setType(Type.RIGHT_ARM);
			player.closeInventory();

			user.setTargetAS(armorstand);

			setArrows();
			this.updateItem(pose(Type.RIGHT_ARM, Material.DIAMOND_PICKAXE, "Right Arm"), 7);
		}, true);

		this.setItem(pose(Type.LEFT_ARM, Material.STICK, "Left Arm"), 8, inventoryClickEvent -> {

			user.setType(Type.LEFT_ARM);
			player.closeInventory();

			user.setTargetAS(armorstand);

			setArrows();
			this.updateItem(pose(Type.LEFT_ARM, Material.STICK, "Left Arm"), 8);
		}, true);

		this.setItem(pose(Type.LEFT_LEG, Material.DIAMOND_BOOTS, "Left Leg"), 15, inventoryClickEvent -> {

			user.setType(Type.LEFT_LEG);
			player.closeInventory();

			user.setTargetAS(armorstand);

			setArrows();
			this.updateItem(pose(Type.LEFT_LEG, Material.DIAMOND_BOOTS, "Left Leg"), 15);
		}, true);

		this.setItem(pose(Type.RIGHT_LEG, Material.GOLDEN_BOOTS, "Right Leg"), 14, inventoryClickEvent -> {

			user.setType(Type.RIGHT_LEG);
			player.closeInventory();

			user.setTargetAS(armorstand);

			setArrows();
			this.updateItem(pose(Type.RIGHT_LEG, Material.GOLDEN_BOOTS, "Right Leg"), 14);
		}, true);

		this.setItem(pose(Type.LOCATION, Material.COMPASS, "Location"), 16, inventoryClickEvent -> {

			user.setType(Type.LOCATION);
			player.closeInventory();

			user.setTargetAS(armorstand);

			setArrows();
			this.updateItem(pose(Type.LOCATION, Material.COMPASS, "Location"), 16);
		}, true);

		this.setItem(pose(Type.ROTATION, Material.OAK_BOAT, "Rotation"), 17, inventoryClickEvent -> {

			user.setType(Type.ROTATION);
			player.closeInventory();

			user.setTargetAS(armorstand);

			setArrows();

			this.updateItem(pose(Type.ROTATION, Material.OAK_BOAT, "Rotation"), 17);
		}, true);

		this.setItem(toggle(!armorstand.getCustomArmorstand().isInvisible(), Material.GLASS_BOTTLE, "visible"), 39, inventoryClickEvent -> {
			armorstand.getCustomArmorstand().setInvisible(!armorstand.getCustomArmorstand().isInvisible());

			Bukkit.getOnlinePlayers().forEach(armorstand.getCustomArmorstand()::update);
			config.set("Invisible", armorstand.getCustomArmorstand().isInvisible());
			try {
				config.save(file);
			} catch (IOException ignored) {}

			this.updateItem(toggle(!armorstand.getCustomArmorstand().isInvisible(), Material.GLASS_BOTTLE, "visible"), 39);
		}, true);

		this.setItem(toggle(!armorstand.getCustomArmorstand().isBasePlate(), Material.GRAY_CARPET, "baseplate"), 40, inventoryClickEvent -> {
			armorstand.getCustomArmorstand().setBasePlate(!armorstand.getCustomArmorstand().isBasePlate());

			Bukkit.getOnlinePlayers().forEach(armorstand.getCustomArmorstand()::update);
			config.set("BasePlate", armorstand.getCustomArmorstand().isBasePlate());
			try {
				config.save(file);
			} catch (IOException ignored) {}

			this.updateItem(toggle(!armorstand.getCustomArmorstand().isBasePlate(), Material.GRAY_CARPET, "baseplate"), 40);
		}, true);

		this.setItem(toggle(armorstand.getCustomArmorstand().isArms(), Material.TOTEM_OF_UNDYING, "arms"), 42, inventoryClickEvent -> {
			armorstand.getCustomArmorstand().setArms(!armorstand.getCustomArmorstand().isArms());

			Bukkit.getOnlinePlayers().forEach(armorstand.getCustomArmorstand()::update);
			config.set("Arms", armorstand.getCustomArmorstand().isArms());
			try {
				config.save(file);
			} catch (IOException ignored) {}

			this.updateItem(toggle(armorstand.getCustomArmorstand().isArms(), Material.TOTEM_OF_UNDYING, "arms"), 42);
		}, true);

		this.setItem(toggle(armorstand.getCustomArmorstand().isSmall(), Material.ARMOR_STAND, "small"), 43, inventoryClickEvent -> {
			armorstand.getCustomArmorstand().setSmall(!armorstand.getCustomArmorstand().isSmall());

			Bukkit.getOnlinePlayers().forEach(armorstand.getCustomArmorstand()::update);
			config.set("Small", armorstand.getCustomArmorstand().isSmall());
			try {
				config.save(file);
			} catch (IOException ignored) {}

			this.updateItem(toggle(armorstand.getCustomArmorstand().isSmall(), Material.ARMOR_STAND, "small"), 43);
		}, true);

		this.setItem(toggle(armorstand.getCustomArmorstand().isCustomNameVisible(), Material.NAME_TAG, "show name"), 41, inventoryClickEvent -> {
			armorstand.getCustomArmorstand().setCustomNameVisible(!armorstand.getCustomArmorstand().isCustomNameVisible());

			Bukkit.getOnlinePlayers().forEach(armorstand.getCustomArmorstand()::update);
			config.set("CustomNameVisible", armorstand.getCustomArmorstand().isCustomNameVisible());
			try {
				config.save(file);
			} catch (IOException ignored) {}

			this.updateItem(toggle(armorstand.getCustomArmorstand().isCustomNameVisible(), Material.NAME_TAG, "show name"), 41);
		}, true);

		this.setItem(toggle(armorstand.isLock(), Material.OAK_DOOR, "lock"), 44, inventoryClickEvent -> {
			armorstand.setLock(!armorstand.isLock());

			config.set("Lock", armorstand.isLock());
			try {
				config.save(file);
			} catch (IOException ignored) {}

			this.updateItem(toggle(armorstand.isLock(), Material.OAK_DOOR, "lock"), 44);
		}, true);

		this.setItem(setName(armorstand), 49, inventoryClickEvent -> {
			getPlayer().closeInventory();

			String name = "Enter name here!";
			if(armorstand.getCustomArmorstand().getCustomName() != null) name = armorstand.getCustomArmorstand().getCustomName();
			new AnvilGUI.Builder()
					.onComplete((target, text) -> {
						armorstand.getCustomArmorstand().setCustomName(AnvilListener.lastText.get(target));

						Bukkit.getOnlinePlayers().forEach(armorstand.getCustomArmorstand()::updateName);
						config.set("CustomName", armorstand.getCustomArmorstand().getCustomName());
						try {
							config.save(file);
						} catch (IOException ignored) {}

						new ArmorstandEditInv(player, armorstand).openInventory();
						return AnvilGUI.Response.close();
					})
					.title(ChatColor.BLUE + ChatColor.BOLD.toString() + "Enter name")
					.text(name)
					.itemLeft(new ItemStack(Material.PAPER))
					.plugin(ArmorstandManager.getInstance())
					.open(player);
		}, true);

		this.setItem(target(armorstand), 50, inventoryClickEvent -> {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 100, 1);

			user.setTargetAS(armorstand);

			player.closeInventory();
			player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "Selected!");
		}, true);

		this.setItem(info(armorstand), 51, true);
	}

	private void setArrows() {
		getPlayer().getInventory().setItem(0, new ItemBuilder(Material.SPECTRAL_ARROW).enchant(Enchantment.UNBREAKING, 1)
				.name(ChatColor.AQUA + ChatColor.BOLD.toString() + "X: " + ChatColor.GOLD + "Move 0.01 | Move 0.001").build());
		getPlayer().getInventory().setItem(1, new ItemBuilder(Material.SPECTRAL_ARROW).enchant(Enchantment.UNBREAKING, 1)
				.name(ChatColor.AQUA + ChatColor.BOLD.toString() + "Y: " + ChatColor.GOLD + "Move 0.01 | Move 0.001").build());
		getPlayer().getInventory().setItem(2, new ItemBuilder(Material.SPECTRAL_ARROW).enchant(Enchantment.UNBREAKING, 1)
				.name(ChatColor.AQUA + ChatColor.BOLD.toString() + "Z: " + ChatColor.GOLD + "Move 0.01 | Move 0.001").build());
	}

	private ItemStack pose(Type type, Material material, String pos) {
		User user = UserModule.getInstance().getUser(getPlayer());

		if(user.getType().equals(type)) {
			return new ItemBuilder(material).enchant(Enchantment.UNBREAKING, 1).name(ChatColor.GOLD + pos +" Pose").build();
		} else {
			return new ItemBuilder(material).name(ChatColor.GOLD + pos + " Pose").build();
		}
	}

	private ItemStack info(ArmorstandHolder customArmorstand) {
		return new ItemBuilder(Material.KNOWLEDGE_BOOK).name(ChatColor.GOLD + "Info").lore(
				ChatColor.GRAY + "ID: " + ChatColor.WHITE + customArmorstand.getID(),
				ChatColor.GRAY + "Render distance: " + ChatColor.WHITE + customArmorstand.getRenderDistance(),
				ChatColor.GRAY + "Name: " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', customArmorstand.getCustomArmorstand().getCustomName()),
				ChatColor.GRAY + "X: " + ChatColor.WHITE + customArmorstand.getCustomArmorstand().getLocation().getX(),
				ChatColor.GRAY + "Y: " + ChatColor.WHITE + customArmorstand.getCustomArmorstand().getLocation().getY(),
				ChatColor.GRAY + "Z: " + ChatColor.WHITE + customArmorstand.getCustomArmorstand().getLocation().getZ(),
				ChatColor.GRAY + "YAW: " + ChatColor.WHITE + customArmorstand.getCustomArmorstand().getLocation().getYaw(),
				ChatColor.GRAY + "PITCH: " + ChatColor.WHITE + customArmorstand.getCustomArmorstand().getLocation().getPitch()
		).build();
	}

	private ItemStack target(ArmorstandHolder customArmorstand) {
		User user = UserModule.getInstance().getUser(getPlayer());

		if(user.getTargetAS().equals(customArmorstand)) {
			return new ItemBuilder(Material.TARGET).name(ChatColor.GOLD + "Select armorstand").lore(
					ChatColor.GREEN + "SELECTED"
			).enchant(Enchantment.UNBREAKING, 1).build();

		} else {
			return new ItemBuilder(Material.TARGET).name(ChatColor.GOLD + "Select armorstand").lore(
					ChatColor.RED + "NOT selected"
			).build();
		}
	}

	private ItemStack toggle(Boolean value, Material material, String type) {

		if(value) {
			return new ItemBuilder(material).name(ChatColor.GOLD + "Set " + type)
					.lore(ChatColor.GRAY + "Left or right click to toggle this!",
							" ", ChatColor.YELLOW + "Value: " + ChatColor.GREEN + "[TRUE]")
					.enchant(Enchantment.UNBREAKING, 1).build();
		} else {
			return new ItemBuilder(material).name(ChatColor.GOLD + "Set " + type)
					.lore(ChatColor.GRAY + "Left or right click to toggle this!",
							" ", ChatColor.YELLOW + "Value: " + ChatColor.RED + "[FALSE]").build();
		}
	}

	private ItemStack setName(ArmorstandHolder armorstand) {
		return new ItemBuilder(Material.NAME_TAG).name(ChatColor.GOLD + "Set name")
				.lore(ChatColor.GRAY + "Left or right click to change name!", " ", ChatColor.YELLOW + "Current name: " + armorstand.getCustomArmorstand().getCustomName()).build();
	}
}