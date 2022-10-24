package net.fortressgames.armorstandmanager.inventories;

import net.fortressgames.armorstandmanager.ArmorstandManager;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandHolder;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandModule;
import net.fortressgames.armorstandmanager.listeners.AnvilListener;
import net.fortressgames.armorstandmanager.users.User;
import net.fortressgames.armorstandmanager.users.UserModule;
import net.fortressgames.armorstandmanager.utils.Type;
import net.fortressgames.fortressapi.gui.InventoryMenu;
import net.fortressgames.fortressapi.gui.InventoryRows;
import net.fortressgames.fortressapi.gui.ItemBuilder;
import net.fortressgames.fortressapi.players.PlayerModule;
import net.fortressgames.fortressapi.utils.Enchantment;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmorstandEditInv extends InventoryMenu {

	private BukkitTask armor;
	private final ArmorstandHolder armorstandHolder;

	public ArmorstandEditInv(Player player, ArmorstandHolder armorstandHolder) {
		super(player, InventoryRows.ROW6, ChatColor.BLUE + ChatColor.BOLD.toString() + "Armorstand: " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', armorstandHolder.getCustomArmorstand().getCustomName()));

		this.armorstandHolder = armorstandHolder;
		User user = UserModule.getInstance().getUser(player);

		List<Integer> slots = new ArrayList<>(Arrays.asList(52, 46, 48, 53, 36, 37, 38, 45, 47, 0, 2, 3, 12, 18, 20, 21, 27, 29, 30, 31, 32, 33, 34, 35, 13, 22, 23, 24, 25, 4, 26));
		for(Integer slot : slots) {
			this.setItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build(), slot, true);
		}

		setCanMove(true);

		this.setItem(armorstandHolder.getCustomArmorstand().getHelmet(), 1);
		this.setItem(armorstandHolder.getCustomArmorstand().getChestplate(), 10);
		this.setItem(armorstandHolder.getCustomArmorstand().getLeggings(), 19);
		this.setItem(armorstandHolder.getCustomArmorstand().getBoots(), 28);
		this.setItem(armorstandHolder.getCustomArmorstand().getItemInMainHand(), 9);
		this.setItem(armorstandHolder.getCustomArmorstand().getItemInOffHand(), 11);

		boolean silent = armorstandHolder.getCustomArmorstand().isSilent();
		armorstandHolder.getCustomArmorstand().setSilent(true);

		armor = Bukkit.getScheduler().runTaskTimerAsynchronously(ArmorstandManager.getInstance(), () -> {
			if(!player.getOpenInventory().getTopInventory().equals(getInventory())) {
				armor.cancel();
				armorstandHolder.getCustomArmorstand().setSilent(silent);
			}

			Bukkit.getScheduler().runTask(ArmorstandManager.getInstance(), () -> {
				armorstandHolder.getCustomArmorstand().setHelmet(getInventory().getItem(1));
				armorstandHolder.getCustomArmorstand().setChestplate(getInventory().getItem(10));
				armorstandHolder.getCustomArmorstand().setLeggings(getInventory().getItem(19));
				armorstandHolder.getCustomArmorstand().setBoots(getInventory().getItem(28));
				armorstandHolder.getCustomArmorstand().setItemInMainHand(getInventory().getItem(9));
				armorstandHolder.getCustomArmorstand().setItemInOffHand(getInventory().getItem(11));

				PlayerModule.getInstance().getOnlinePlayers().forEach(armorstandHolder.getCustomArmorstand()::update);

				armorstandHolder.saveItems();
			});

		}, 0, 3);

		setPoseSlots(Type.HEAD, Material.CHAINMAIL_HELMET, "Head", 5);
		setPoseSlots(Type.BODY, Material.GOLDEN_CHESTPLATE, "Body", 6);
		setPoseSlots(Type.RIGHT_ARM, Material.DIAMOND_PICKAXE, "Right Arm", 7);
		setPoseSlots(Type.LEFT_ARM, Material.STICK, "Left Arm", 8);
		setPoseSlots(Type.LEFT_LEG, Material.DIAMOND_BOOTS, "Left Leg", 15);
		setPoseSlots(Type.RIGHT_LEG, Material.GOLDEN_BOOTS, "Right Leg", 14);
		setPoseSlots(Type.LOCATION, Material.COMPASS, "Location", 16);
		setPoseSlots(Type.ROTATION, Material.OAK_BOAT, "Rotation", 17);

		this.setItem(toggle(!armorstandHolder.getCustomArmorstand().isInvisible(), Material.GLASS_BOTTLE, "visible"), 39, inventoryClickEvent -> {
			armorstandHolder.getCustomArmorstand().setInvisible(!armorstandHolder.getCustomArmorstand().isInvisible());

			PlayerModule.getInstance().getOnlinePlayers().forEach(armorstandHolder.getCustomArmorstand()::update);
			armorstandHolder.saveAttributes();

			this.updateItem(toggle(!armorstandHolder.getCustomArmorstand().isInvisible(), Material.GLASS_BOTTLE, "visible"), inventoryClickEvent.getSlot());
		}, true);

		this.setItem(toggle(!armorstandHolder.getCustomArmorstand().isBasePlate(), Material.GRAY_CARPET, "baseplate"), 40, inventoryClickEvent -> {
			armorstandHolder.getCustomArmorstand().setBasePlate(!armorstandHolder.getCustomArmorstand().isBasePlate());

			PlayerModule.getInstance().getOnlinePlayers().forEach(armorstandHolder.getCustomArmorstand()::update);
			armorstandHolder.saveAttributes();

			this.updateItem(toggle(!armorstandHolder.getCustomArmorstand().isBasePlate(), Material.GRAY_CARPET, "baseplate"), inventoryClickEvent.getSlot());
		}, true);

		this.setItem(toggle(armorstandHolder.getCustomArmorstand().isArms(), Material.TOTEM_OF_UNDYING, "arms"), 42, inventoryClickEvent -> {
			armorstandHolder.getCustomArmorstand().setArms(!armorstandHolder.getCustomArmorstand().isArms());

			PlayerModule.getInstance().getOnlinePlayers().forEach(armorstandHolder.getCustomArmorstand()::update);
			armorstandHolder.saveAttributes();

			this.updateItem(toggle(armorstandHolder.getCustomArmorstand().isArms(), Material.TOTEM_OF_UNDYING, "arms"), inventoryClickEvent.getSlot());
		}, true);

		this.setItem(toggle(armorstandHolder.getCustomArmorstand().isSmall(), Material.ARMOR_STAND, "small"), 43, inventoryClickEvent -> {
			armorstandHolder.getCustomArmorstand().setSmall(!armorstandHolder.getCustomArmorstand().isSmall());

			PlayerModule.getInstance().getOnlinePlayers().forEach(armorstandHolder.getCustomArmorstand()::update);
			armorstandHolder.saveAttributes();

			this.updateItem(toggle(armorstandHolder.getCustomArmorstand().isSmall(), Material.ARMOR_STAND, "small"), inventoryClickEvent.getSlot());
		}, true);

		this.setItem(toggle(armorstandHolder.getCustomArmorstand().isCustomNameVisible(), Material.NAME_TAG, "show name"), 41, inventoryClickEvent -> {
			armorstandHolder.getCustomArmorstand().setCustomNameVisible(!armorstandHolder.getCustomArmorstand().isCustomNameVisible());

			PlayerModule.getInstance().getOnlinePlayers().forEach(armorstandHolder.getCustomArmorstand()::update);
			armorstandHolder.saveAttributes();

			this.updateItem(toggle(armorstandHolder.getCustomArmorstand().isCustomNameVisible(), Material.NAME_TAG, "show name"), inventoryClickEvent.getSlot());
		}, true);

		this.setItem(toggle(armorstandHolder.isLock(), Material.OAK_DOOR, "lock"), 44, inventoryClickEvent -> {
			armorstandHolder.setLock(!armorstandHolder.isLock());

			armorstandHolder.saveLock();

			this.updateItem(toggle(armorstandHolder.isLock(), Material.OAK_DOOR, "lock"), inventoryClickEvent.getSlot());
		}, true);

		this.setItem(setName(armorstandHolder), 49, inventoryClickEvent -> {
			getPlayer().closeInventory();

			String name = "Enter name here!";
			if(armorstandHolder.getCustomArmorstand().getCustomName() != null) name = armorstandHolder.getCustomArmorstand().getCustomName();
			new AnvilGUI.Builder()
					.onComplete((target, text) -> {
						armorstandHolder.getCustomArmorstand().setCustomName(AnvilListener.lastText.get(target));

						PlayerModule.getInstance().getOnlinePlayers().forEach(armorstandHolder.getCustomArmorstand()::update);
						armorstandHolder.saveName();

						new ArmorstandEditInv(player, armorstandHolder).openInventory();
						return AnvilGUI.Response.close();
					})
					.title(ChatColor.BLUE + ChatColor.BOLD.toString() + "Enter name")
					.text(name)
					.itemLeft(new ItemStack(Material.PAPER))
					.plugin(ArmorstandManager.getInstance())
					.open(player);
		}, true);

		this.setItem(target(), 50, inventoryClickEvent -> {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 100, 1);

			user.setTargetAS(armorstandHolder);

			player.closeInventory();
			player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "Selected!");
		}, true);

		this.setItem(info(), 51, true);
	}

	/**
	 * Set pose item slot
	 */
	private void setPoseSlots(Type type, Material material, String pos, int slot) {
		this.setItem(pose(type, material, pos), slot, inventoryClickEvent -> {
			changeType(type);
			this.updateItem(pose(type, material, pos), inventoryClickEvent.getSlot());
		}, true);
	}

	private void changeType(Type type) {
		User user = UserModule.getInstance().getUser(getPlayer());

		user.setType(type);
		getPlayer().closeInventory();
		user.setTargetAS(armorstandHolder);

		// Arrows
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

	private ItemStack info() {
		return new ItemBuilder(Material.KNOWLEDGE_BOOK).name(ChatColor.GOLD + "Info").lore(
				ChatColor.GRAY + "ID: " + ChatColor.WHITE + armorstandHolder.getID(),
				ChatColor.GRAY + "Render distance: " + ChatColor.WHITE + armorstandHolder.getRenderDistance(),
				ChatColor.GRAY + "Name: " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', armorstandHolder.getCustomArmorstand().getCustomName()),
				ChatColor.GRAY + "X: " + ChatColor.WHITE + armorstandHolder.getCustomArmorstand().getLocation().getX(),
				ChatColor.GRAY + "Y: " + ChatColor.WHITE + armorstandHolder.getCustomArmorstand().getLocation().getY(),
				ChatColor.GRAY + "Z: " + ChatColor.WHITE + armorstandHolder.getCustomArmorstand().getLocation().getZ(),
				ChatColor.GRAY + "YAW: " + ChatColor.WHITE + armorstandHolder.getCustomArmorstand().getLocation().getYaw(),
				ChatColor.GRAY + "PITCH: " + ChatColor.WHITE + armorstandHolder.getCustomArmorstand().getLocation().getPitch()
		).build();
	}

	private ItemStack target() {
		User user = UserModule.getInstance().getUser(getPlayer());

		if(ArmorstandModule.hasTargetSilent(getPlayer()) && user.getTargetAS().equals(armorstandHolder)) {
			return new ItemBuilder(Material.TARGET).name(ChatColor.GOLD + "Select armorstand").lore(
					ChatColor.GREEN + "SELECTED"
			).enchant(Enchantment.UNBREAKING, 1).build();

		} else {
			return new ItemBuilder(Material.TARGET).name(ChatColor.GOLD + "Select armorstand").lore(
					ChatColor.RED + "NOT selected"
			).build();
		}
	}

	private ItemStack toggle(Boolean value, Material material, String name) {

		if(value) {
			return new ItemBuilder(material).name(ChatColor.GOLD + "Set " + name)
					.lore(ChatColor.GRAY + "Left or right click to toggle this!",
							" ", ChatColor.YELLOW + "Value: " + ChatColor.GREEN + "[TRUE]")
					.enchant(Enchantment.UNBREAKING, 1).build();
		} else {
			return new ItemBuilder(material).name(ChatColor.GOLD + "Set " + name)
					.lore(ChatColor.GRAY + "Left or right click to toggle this!",
							" ", ChatColor.YELLOW + "Value: " + ChatColor.RED + "[FALSE]").build();
		}
	}

	private ItemStack setName(ArmorstandHolder armorstand) {
		return new ItemBuilder(Material.NAME_TAG).name(ChatColor.GOLD + "Set name")
				.lore(ChatColor.GRAY + "Left or right click to change name!", " ", ChatColor.YELLOW + "Current name: " + armorstand.getCustomArmorstand().getCustomName()).build();
	}
}