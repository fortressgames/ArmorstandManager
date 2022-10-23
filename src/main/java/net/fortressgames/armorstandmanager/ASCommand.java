package net.fortressgames.armorstandmanager;

import lombok.SneakyThrows;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandHolder;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandModule;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandUtils;
import net.fortressgames.armorstandmanager.listeners.ClickListener;
import net.fortressgames.armorstandmanager.users.User;
import net.fortressgames.armorstandmanager.users.UserModule;
import net.fortressgames.fortressapi.Lang;
import net.fortressgames.fortressapi.commands.CommandBase;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ASCommand extends CommandBase {

	public ASCommand() {
		super("as", "armorstandmanager.command.as");
	}

	@Override
	@SneakyThrows
	public void execute(CommandSender sender, String[] args) {

		if(sender instanceof Player player) {
			User user = UserModule.getInstance().getUser(player);

			if(args.length == 0) {
				player.sendMessage(Lang.LINE);
				player.sendMessage(ChatColor.WHITE + "/" + getLabel() + " list" + ChatColor.GRAY + " - List all armorstands");
				player.sendMessage(ChatColor.WHITE + "/" + getLabel() + " toggle" + ChatColor.GRAY + " - Toggle armorstands off/on");
				player.sendMessage(ChatColor.WHITE + "/" + getLabel() + " render " + ChatColor.of("#BA68C8") + "<view>" + ChatColor.GRAY + " - Change armorstand render view");
				player.sendMessage(ChatColor.WHITE + "/" + getLabel() + " kill " + ChatColor.of("#BA68C8") + "<id>" + ChatColor.GRAY + " - Delete armorstand");
				player.sendMessage(ChatColor.WHITE + "/" + getLabel() + " add " +  ChatColor.of("#BA68C8") + "<value>" + ChatColor.GRAY + " - Add armorstand value (arrow in hand)");
				player.sendMessage(ChatColor.WHITE + "/" + getLabel() + " set " + ChatColor.of("#BA68C8") + "<value>" + ChatColor.GRAY + " - Set armorstand value (arrow in hand)");
				player.sendMessage(ChatColor.WHITE + "/" + getLabel() + " moveto" + ChatColor.GRAY + " - Move armorstand to player");
				player.sendMessage(Lang.LINE);
				return;
			}

			if(args[0].equalsIgnoreCase("moveto")) {
				if(ArmorstandUtils.hasTarget(player)) {
					ArmorstandHolder armorstandHolder = user.getTargetAS();
					YamlConfiguration config = YamlConfiguration.loadConfiguration(armorstandHolder.getFile());

					armorstandHolder.getCustomArmorstand().setLocation(player.getLocation());
					Bukkit.getOnlinePlayers().forEach(armorstandHolder.getCustomArmorstand()::update);

					config.set("Location.World", armorstandHolder.getCustomArmorstand().getLocation().getWorld().getName());
					config.set("Location.X", armorstandHolder.getCustomArmorstand().getLocation().getX());
					config.set("Location.Y", armorstandHolder.getCustomArmorstand().getLocation().getY());
					config.set("Location.Z", armorstandHolder.getCustomArmorstand().getLocation().getZ());
					config.set("Location.Yaw", armorstandHolder.getCustomArmorstand().getLocation().getYaw());
					config.set("Location.Pitch", armorstandHolder.getCustomArmorstand().getLocation().getPitch());

					config.save(armorstandHolder.getFile());

					player.sendMessage(ChatColor.GREEN + "Moved!");
				}
				return;
			}

			if(args[0].equalsIgnoreCase("set")) {
				if(ArmorstandUtils.hasTarget(player)) {
					ArmorstandHolder armorstandHolder = user.getTargetAS();

					double value;
					try {
						value = Double.parseDouble(args[1]);
					} catch (IllegalArgumentException e) {
						player.sendMessage(ChatColor.RED + "Value is not a number!");
						return;
					}

					ClickListener.movePart(player, armorstandHolder, value, true, true);
					player.sendMessage(ChatColor.GREEN + "Moved!");
				}
				return;
			}

			if(args[0].equalsIgnoreCase("add") && args.length == 2) {
				if(ArmorstandUtils.hasTarget(player)) {
					ArmorstandHolder armorstandHolder = user.getTargetAS();

					double value;
					try {
						value = Double.parseDouble(args[1]);
					} catch (IllegalArgumentException e) {
						player.sendMessage(ChatColor.RED + "Value is not a number!");
						return;
					}

					ClickListener.movePart(player, armorstandHolder, value, true, false);
					player.sendMessage(ChatColor.GREEN + "Moved!");
				}
				return;
			}

			if(args[0].equalsIgnoreCase("kill") && args.length == 2) {
				kill(sender, Integer.parseInt(args[1]));
				return;
			}

			if(args[0].equalsIgnoreCase("render") && args.length == 2) {
				if(ArmorstandUtils.hasTarget(player)) {
					ArmorstandHolder armorstandHolder = user.getTargetAS();
					YamlConfiguration config = YamlConfiguration.loadConfiguration(armorstandHolder.getFile());

					armorstandHolder.setRenderDistance(Integer.parseInt(args[1]));

					if(args[1].equalsIgnoreCase("-1")) {
						ArmorstandModule.getInstance().getRenderList().remove(armorstandHolder);
					}

					config.set("RenderDistance", armorstandHolder.getRenderDistance());
					config.save(armorstandHolder.getFile());

					Bukkit.getOnlinePlayers().forEach(pp -> {
						armorstandHolder.getCustomArmorstand().remove(pp);
						armorstandHolder.getCustomArmorstand().spawn(pp);
					});

					player.sendMessage(ChatColor.GREEN + "Armorstand render set to " + args[1]);
				}
				return;
			}

			if(args[0].equalsIgnoreCase("toggle")) {
				if(ArmorstandManager.getInstance().isToggle()) {
					ArmorstandManager.getInstance().setToggle(false);

					Bukkit.getOnlinePlayers().forEach(pp ->
							ArmorstandModule.getInstance().getCustomArmorstands().forEach(armorstandHolder -> armorstandHolder.getCustomArmorstand().remove(pp)));

					player.sendMessage(ChatColor.GREEN + "Armorstands toggled off!");
				} else {
					ArmorstandManager.getInstance().setToggle(true);

					Bukkit.getOnlinePlayers().forEach(pp -> {
						List<ArmorstandHolder> armorstands = new ArrayList<>(ArmorstandModule.getInstance().getCustomArmorstands());
						armorstands.removeAll(ArmorstandModule.getInstance().getRenderList());

						for(ArmorstandHolder customArmorstand : armorstands) {
							customArmorstand.getCustomArmorstand().spawn(pp);
						}
					});

					player.sendMessage(ChatColor.GREEN + "Armorstands toggled on!");
				}
				return;
			}

			if(args[0].equalsIgnoreCase("list")) {
				player.sendMessage(ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + "                                                                                      ");
				player.sendMessage(ChatColor.of("#4FDA19") + ChatColor.BOLD.toString() + "Armorstands: (" + ArmorstandModule.getInstance().getCustomArmorstands().size() + ")");

				List<String> list = new ArrayList<>();
				for(ArmorstandHolder armorstand : ArmorstandModule.getInstance().getCustomArmorstands()) {
					if((list.size() & 1) == 0 ) {
						list.add(ChatColor.of("#FBC02D").toString() +
								armorstand.getID() + ":" + ChatColor.translateAlternateColorCodes('&', armorstand.getCustomArmorstand().getCustomName()) +ChatColor.of("#90A4AE"));
					} else {
						list.add(ChatColor.of("#FFF59D").toString() +
								armorstand.getID() + ":" + ChatColor.translateAlternateColorCodes('&', armorstand.getCustomArmorstand().getCustomName()) + ChatColor.of("#90A4AE"));
					}
				}

				player.sendMessage(net.md_5.bungee.api.ChatColor.of("#90A4AE") + list.toString());

				player.sendMessage(ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + "                                                                                      ");
				return;
			}
		}
	}

	private void kill(CommandSender sender, int id) {
		ArmorstandHolder armorstandHolder = null;
		for(ArmorstandHolder armorstand : ArmorstandModule.getInstance().getCustomArmorstands()) {
			if(armorstand.getID() == id) {
				armorstandHolder = armorstand;
				break;
			}
		}

		if(armorstandHolder == null) {
			sender.sendMessage(ChatColor.RED + "Unknown id");
			return;
		}

		ArmorstandModule.getInstance().remove(armorstandHolder);
		sender.sendMessage(ChatColor.GREEN + "Deleted!");
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		if(args.length == 1) {
			return Arrays.asList("list", "toggle", "render", "kill", "animation", "set", "add", "moveto");
		}

		return super.tabComplete(sender, alias, args);
	}
}