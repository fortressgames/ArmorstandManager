package net.fortressgames.armorstandmanager;

import net.fortressgames.armorstandmanager.armorstands.AnimationModule;
import net.fortressgames.armorstandmanager.armorstands.AnimationState;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandHolder;
import net.fortressgames.armorstandmanager.armorstands.ArmorstandModule;
import net.fortressgames.armorstandmanager.users.User;
import net.fortressgames.armorstandmanager.users.UserModule;
import net.fortressgames.fortressapi.Lang;
import net.fortressgames.fortressapi.commands.CommandBase;
import net.fortressgames.fortressapi.entities.CustomArmorstand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class ASACommand extends CommandBase {

	public ASACommand() {
		super("asa", "armorstandmanager.command.asa");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

		if(args.length == 0) {
			sender.sendMessage(Lang.LINE);
			sender.sendMessage(ChatColor.WHITE + "/asa create " + ChatColor.of("#BA68C8") + "<name> " + ChatColor.GRAY + "- Create an animation");
			sender.sendMessage(ChatColor.WHITE + "/asa addpos " + ChatColor.of("#BA68C8") + "<ticks> " + ChatColor.GRAY + "- Add a statue to a animation");
			sender.sendMessage(ChatColor.WHITE + "/asa removepos " + ChatColor.of("#BA68C8") + "<number> " + ChatColor.GRAY + "- Remove a statue from a recording");
			//sender.sendMessage(ChatColor.WHITE + "/asa setpos " + ChatColor.of("#BA68C8") + "<number> <ticks> " + ChatColor.GRAY + "- Override a current statue");
			//sender.sendMessage(ChatColor.WHITE + "/asa movepos " + ChatColor.of("#BA68C8") + "<number> " + ChatColor.GRAY + "- Return the armorstand to a state");
			sender.sendMessage(ChatColor.WHITE + "/asa play " + ChatColor.of("#BA68C8") + "<id> " + ChatColor.GRAY + "- Play an animation");
			sender.sendMessage(ChatColor.WHITE + "/asa stop " + ChatColor.of("#BA68C8") + "<id> " + ChatColor.GRAY + "- Stop an animation");
			//sender.sendMessage(ChatColor.WHITE + "/asa loop " + ChatColor.of("#BA68C8") + "<name> " + ChatColor.GRAY + "- Toggle looping");
			//sender.sendMessage(ChatColor.WHITE + "/asa reverse " + ChatColor.of("#BA68C8") + "<name> " + ChatColor.GRAY + "- Toggle reverse");
			sender.sendMessage(ChatColor.WHITE + "/asa set " + ChatColor.of("#BA68C8") + "<name> " + ChatColor.GRAY + "- Set an armorstand animation");
			//sender.sendMessage(ChatColor.WHITE + "/asa clone " + ChatColor.of("#BA68C8") + "<name> <new-name>" + ChatColor.GRAY + "- Clone an animation");
			sender.sendMessage(Lang.LINE);
			return;
		}

		if(sender instanceof Player player) {
			//
			// Player commands
			//

			ArmorstandHolder armorstandHolder = null;

			if(ArmorstandModule.hasTargetSilent(player)) {
				User user = UserModule.getInstance().getUser(player);
				armorstandHolder = user.getTargetAS();
			}

			switch (args[0].toLowerCase()) {
				//
				// removepos
				//
				case "removepos" -> {
					if(armorstandHolder != null) {

						if(armorstandHolder.getAnimation() == null) {
							sender.sendMessage(ASLang.ANIMATION_UNKNOWN);
							return;
						}

						try {
							new File(armorstandHolder.getAnimation().getFile().getPath() + "/" + Integer.parseInt(args[1]) + ".yml").delete();

							for(int i = Integer.parseInt(args[1])+1; i < armorstandHolder.getAnimation().getAnimationStates().size(); i++) {

								new File(armorstandHolder.getAnimation().getFile().getPath() + "/" + i + ".yml").renameTo(
										new File(armorstandHolder.getAnimation().getFile().getPath() + "/" + (i-1) + ".yml")
								);
							}

							armorstandHolder.getAnimation().getAnimationStates().remove(Integer.parseInt(args[1]));
							sender.sendMessage(ASLang.ANIMATION_REMOVED);

						} catch (IndexOutOfBoundsException e) {
							sender.sendMessage(ASLang.ANIMATION_OUT_ERROR);
						}

					} else {
						sender.sendMessage(ASLang.NO_AS_SELECTED);
					}
				}

				//
				// stop
				//
				case "stop" -> {
					if(args.length == 2) {
						ArmorstandHolder ash = null;

						for(ArmorstandHolder armorstand : ArmorstandModule.getInstance().getCustomArmorstands()) {
							if(armorstand.getID() == Integer.parseInt(args[1])) {
								ash = armorstand;
								break;
							}
						}

						if(ash == null) {
							sender.sendMessage(ASLang.UNKNOWN_ID);
							return;
						}

						if(AnimationModule.getInstance().getRunning().containsKey(ash.getCustomArmorstand())) {
							AnimationModule.getInstance().getRunning().get(ash.getCustomArmorstand()).stop();
						}

						sender.sendMessage(ASLang.ANIMATION_STOPPED);
					}
				}

				//
				// play
				//
				case "play" -> {
					if(args.length == 2) {
						ArmorstandHolder ash = null;

						for(ArmorstandHolder armorstand : ArmorstandModule.getInstance().getCustomArmorstands()) {
							if(armorstand.getID() == Integer.parseInt(args[1])) {
								ash = armorstand;
								break;
							}
						}

						if(ash == null) {
							sender.sendMessage(ASLang.UNKNOWN_ID);
							return;
						}

						if(AnimationModule.getInstance().getRunning().containsKey(ash.getCustomArmorstand())) {
							sender.sendMessage(ASLang.ANIMATION_RUNNING);
							return;
						}

						if(ash.getAnimation() != null) {
							ash.getAnimation().play(ash.getCustomArmorstand());

						} else {
							sender.sendMessage(ASLang.ANIMATION_UNKNOWN);
						}
					}
				}

				//
				// create
				//
				case "create" -> {
					if(args.length == 2) {
						AnimationModule.getInstance().create(args[1].toLowerCase());
						sender.sendMessage(ASLang.ANIMATION_CREATE);
					}
				}

				//
				// set
				//
				case "set" -> {
					if(args.length == 2 && armorstandHolder != null) {
						armorstandHolder.setAnimation(AnimationModule.getInstance().getAnimation(args[1].toLowerCase()));
						armorstandHolder.saveAnimation();
						sender.sendMessage(ASLang.ANIMATION_SET);

					} else {
						sender.sendMessage(ASLang.NO_AS_SELECTED);
					}
				}

				//
				// addpos
				//
				case "addpos" -> {
					if(armorstandHolder != null) {

						if(armorstandHolder.getAnimation() == null) {
							sender.sendMessage(ASLang.ANIMATION_UNKNOWN);
							return;
						}

						AnimationState animationState = new AnimationState(new CustomArmorstand(armorstandHolder.getCustomArmorstand()));

						AnimationModule.getInstance().getAnimation(armorstandHolder.getAnimation().getName()).getAnimationStates().add(animationState);

						if(args.length == 2) {
							animationState.setTicks(Integer.parseInt(args[1]));
						}

						animationState.save(armorstandHolder.getAnimation().getFile(), armorstandHolder.getAnimation().getAnimationStates().size() -1);

						sender.sendMessage(ASLang.ANIMATION_ADD);

					} else {
						sender.sendMessage(ASLang.NO_AS_SELECTED);
					}
				}
			}

			return;
		}



		/*switch (args[0].toLowerCase()) {
			case "setpos" -> {

			}
			case "movepos" -> {

			}
			case "stop" -> {

			}
			case "loop" -> {

			}
			case "reverse" -> {

			}
			case "clone" -> {

			}
		}*/
	}
}