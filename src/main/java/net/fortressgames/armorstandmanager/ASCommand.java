package net.fortressgames.armorstandmanager;

import lombok.SneakyThrows;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandHolder;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandModule;
import net.fortressgames.armorstandmanager.listeners.ClickListener;
import net.fortressgames.armorstandmanager.users.User;
import net.fortressgames.armorstandmanager.users.UserModule;
import net.fortressgames.fortressapi.Lang;
import net.fortressgames.fortressapi.commands.CommandBase;
import net.fortressgames.fortressapi.players.PlayerModule;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
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

			//
			// MOVE TO
			//
			if(args[0].equalsIgnoreCase("moveto")) {
				if(ArmorstandModule.hasTarget(player)) {
					ArmorstandHolder armorstandHolder = user.getTargetAS();

					armorstandHolder.getCustomArmorstand().setLocation(player.getLocation());
					PlayerModule.getInstance().getOnlinePlayers().forEach(armorstandHolder.getCustomArmorstand()::update);
					armorstandHolder.saveLocation();

					player.sendMessage(ASLang.AS_MOVED);
				}
				return;
			}

			//
			// SET
			//
			if(args[0].equalsIgnoreCase("set")) {
				if(ArmorstandModule.hasTarget(player)) {
					ArmorstandHolder armorstandHolder = user.getTargetAS();

					double value;
					try {
						value = Double.parseDouble(args[1]);
					} catch (IllegalArgumentException e) {
						player.sendMessage(ASLang.ERROR_NUMBER_FORMAT);
						return;
					}

					ClickListener.movePart(player, armorstandHolder, value, true, true);
					player.sendMessage(ASLang.AS_MOVED);
				}
				return;
			}

			//
			// ADD
			//
			if(args[0].equalsIgnoreCase("add") && args.length == 2) {
				if(ArmorstandModule.hasTarget(player)) {
					ArmorstandHolder armorstandHolder = user.getTargetAS();

					double value;
					try {
						value = Double.parseDouble(args[1]);
					} catch (IllegalArgumentException e) {
						player.sendMessage(ASLang.ERROR_NUMBER_FORMAT);
						return;
					}

					ClickListener.movePart(player, armorstandHolder, value, true, false);
					player.sendMessage(ASLang.AS_MOVED);
				}
				return;
			}

			//
			// KILL
			//
			if(args[0].equalsIgnoreCase("kill") && args.length == 2) {
				ArmorstandHolder armorstandHolder = null;

				for(ArmorstandHolder armorstand : ArmorstandModule.getInstance().getCustomArmorstands()) {
					if(armorstand.getID() == Integer.parseInt(args[1])) {
						armorstandHolder = armorstand;
						break;
					}
				}

				if(armorstandHolder == null) {
					sender.sendMessage(ASLang.UNKNOWN_ID);
					return;
				}

				ArmorstandModule.getInstance().remove(armorstandHolder);
				sender.sendMessage(ASLang.AS_REMOVED);
				return;
			}

			//
			// RENDER
			//
			if(args[0].equalsIgnoreCase("render") && args.length == 2) {
				if(ArmorstandModule.hasTarget(player)) {
					ArmorstandHolder armorstandHolder = user.getTargetAS();

					armorstandHolder.setRenderDistance(Integer.parseInt(args[1]));
					armorstandHolder.saveRender();

					PlayerModule.getInstance().getOnlinePlayers().forEach(pp -> {
						armorstandHolder.getCustomArmorstand().remove(pp);
						armorstandHolder.getCustomArmorstand().spawn(pp);
					});

					player.sendMessage(ASLang.ASRender(args[1]));
				}
				return;
			}

			//
			// TOGGLE
			//
			if(args[0].equalsIgnoreCase("toggle")) {
				if(ArmorstandManager.getInstance().isToggle()) {
					ArmorstandManager.getInstance().setToggle(false);
					player.sendMessage(ASLang.ASToggle("off"));

					PlayerModule.getInstance().getOnlinePlayers().forEach(pp -> {

						ArmorstandModule.getInstance().getCustomArmorstands().forEach(armorstandHolder -> armorstandHolder.getCustomArmorstand().remove(pp));
						UserModule.getInstance().getUser(pp).getSpawned().clear();
					});

				} else {
					ArmorstandManager.getInstance().setToggle(true);
					player.sendMessage(ASLang.ASToggle("on"));
				}
				return;
			}

			//
			// LIST
			//
			if(args[0].equalsIgnoreCase("list")) {
				player.sendMessage(Lang.LINE);
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

				player.sendMessage(Lang.LINE);
				return;
			}
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		if(args.length == 1) {
			return Arrays.asList("list", "toggle", "render", "kill", "set", "add", "moveto");
		}

		return super.tabComplete(sender, alias, args);
	}
}