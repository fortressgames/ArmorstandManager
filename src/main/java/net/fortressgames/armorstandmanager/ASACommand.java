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
import net.fortressgames.fortressapi.players.PlayerModule;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ASACommand extends CommandBase {

	public ASACommand() {
		super("asa", "armorstandmanager.command.asa");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

		if(args.length == 0) {
			sender.sendMessage(Lang.LINE);
			sender.sendMessage(ChatColor.WHITE + "/asa create " + ChatColor.of("#BA68C8") + "<name> " + ChatColor.GRAY + "- Create an animation");
			sender.sendMessage(ChatColor.WHITE + "/asa list " + ChatColor.GRAY + "- List all animations");
			sender.sendMessage(ChatColor.WHITE + "/asa addpos " + ChatColor.of("#BA68C8") + "<ticks> " + ChatColor.GRAY + "- Add a statue to a animation");
			sender.sendMessage(ChatColor.WHITE + "/asa removepos " + ChatColor.of("#BA68C8") + "<number> " + ChatColor.GRAY + "- Remove a statue from a animation");
			sender.sendMessage(ChatColor.WHITE + "/asa listpos " + ChatColor.GRAY + "- List all pos");
			sender.sendMessage(ChatColor.WHITE + "/asa setpos " + ChatColor.of("#BA68C8") + "<number> <ticks> " + ChatColor.GRAY + "- Override a current statue");
			sender.sendMessage(ChatColor.WHITE + "/asa movepos " + ChatColor.of("#BA68C8") + "<number> " + ChatColor.GRAY + "- Return the armorstand to a state");
			sender.sendMessage(ChatColor.WHITE + "/asa play " + ChatColor.of("#BA68C8") + "<id> " + ChatColor.GRAY + "- Play an animation");
			sender.sendMessage(ChatColor.WHITE + "/asa stop " + ChatColor.of("#BA68C8") + "<id> " + ChatColor.GRAY + "- Stop an animation");
			sender.sendMessage(ChatColor.WHITE + "/asa loop " + ChatColor.of("#BA68C8") + "<id> " + ChatColor.GRAY + "- Toggle looping");
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
				// loop
				//
				case "loop" -> {
					ArmorstandHolder ash = null;

					try {
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

						List<String> list = ArmorstandManager.getInstance().getConfig().getStringList("Animation-Loop");

						if(list.contains(String.valueOf(ash.getID()))) {
							list.remove(String.valueOf(ash.getID()));

							ash.setLoopAnimation(false);
							sender.sendMessage(ASLang.toggleLoop("FALSE"));
						} else {
							list.add(String.valueOf(ash.getID()));

							ash.setLoopAnimation(true);
							sender.sendMessage(ASLang.toggleLoop("TRUE"));
						}

						ArmorstandManager.getInstance().getConfig().set("Animation-Loop", list);
						ArmorstandManager.getInstance().saveConfig();

					} catch (IndexOutOfBoundsException e) {
						sender.sendMessage(ASLang.ERROR_INDEX_OUT_OF_BOUNDS);
					} catch (NumberFormatException e) {
						sender.sendMessage(ASLang.ERROR_NUMBER_FORMAT);
					}
				}

				//
				// list
				//
				case "list" -> {
					sender.sendMessage(Lang.LINE);
					sender.sendMessage(ChatColor.YELLOW + "Animations " + ChatColor.RED + "[" + ChatColor.DARK_RED + AnimationModule.getInstance().getAllAnimations().size() + ChatColor.RED + "]");

					sender.sendMessage(ChatColor.GOLD + AnimationModule.getInstance().getAllAnimations().toString());

					sender.sendMessage(Lang.LINE);
				}

				//
				// movepos
				//
				case "movepos" -> {
					if(armorstandHolder != null) {

						if(armorstandHolder.getAnimation() == null) {
							sender.sendMessage(ASLang.ANIMATION_UNKNOWN);
							return;
						}

						try {
							AnimationState animationState = armorstandHolder.getAnimation().getAnimationStates().get(Integer.parseInt(args[1]));

							armorstandHolder.getCustomArmorstand().setHeadPose(animationState.getCustomArmorstand().getHeadPose());
							armorstandHolder.getCustomArmorstand().setBodyPose(animationState.getCustomArmorstand().getBodyPose());
							armorstandHolder.getCustomArmorstand().setLeftLegPose(animationState.getCustomArmorstand().getLeftLegPose());
							armorstandHolder.getCustomArmorstand().setRightLegPose(animationState.getCustomArmorstand().getRightLegPose());
							armorstandHolder.getCustomArmorstand().setLeftArmPose(animationState.getCustomArmorstand().getLeftArmPose());
							armorstandHolder.getCustomArmorstand().setRightArmPose(animationState.getCustomArmorstand().getRightArmPose());
							armorstandHolder.getCustomArmorstand().setLocation(animationState.getCustomArmorstand().getLocation());

							armorstandHolder.getCustomArmorstand().setItemInMainHand(animationState.getCustomArmorstand().getItemInMainHand());
							armorstandHolder.getCustomArmorstand().setItemInOffHand(animationState.getCustomArmorstand().getItemInOffHand());
							armorstandHolder.getCustomArmorstand().setHelmet(animationState.getCustomArmorstand().getHelmet());
							armorstandHolder.getCustomArmorstand().setChestplate(animationState.getCustomArmorstand().getChestplate());
							armorstandHolder.getCustomArmorstand().setLeggings(animationState.getCustomArmorstand().getLeggings());
							armorstandHolder.getCustomArmorstand().setBoots(animationState.getCustomArmorstand().getBoots());

							armorstandHolder.getCustomArmorstand().setSmall(animationState.getCustomArmorstand().isSmall());
							armorstandHolder.getCustomArmorstand().setGlowing(animationState.getCustomArmorstand().isGlowing());
							armorstandHolder.getCustomArmorstand().setInvisible(animationState.getCustomArmorstand().isInvisible());
							armorstandHolder.getCustomArmorstand().setArms(animationState.getCustomArmorstand().isArms());
							armorstandHolder.getCustomArmorstand().setBasePlate(animationState.getCustomArmorstand().isBasePlate());
							armorstandHolder.getCustomArmorstand().setCustomNameVisible(animationState.getCustomArmorstand().isCustomNameVisible());

							armorstandHolder.getCustomArmorstand().setCustomName(animationState.getCustomArmorstand().getCustomName());

							PlayerModule.getInstance().getOnlinePlayers().forEach(armorstandHolder.getCustomArmorstand()::update);
						} catch (IndexOutOfBoundsException e) {
							sender.sendMessage(ASLang.ERROR_INDEX_OUT_OF_BOUNDS);
						} catch (NumberFormatException e) {
							sender.sendMessage(ASLang.ERROR_NUMBER_FORMAT);
						}

					} else {
						sender.sendMessage(ASLang.NO_AS_SELECTED);
					}
				}

				//
				// setpos
				//
				case "setpos" -> {
					if(armorstandHolder != null) {

						if(armorstandHolder.getAnimation() == null) {
							sender.sendMessage(ASLang.ANIMATION_UNKNOWN);
							return;
						}

						if(args.length >= 2) {
							try {
								AnimationState animationState = new AnimationState(new CustomArmorstand(armorstandHolder.getCustomArmorstand()));
								armorstandHolder.getAnimation().getAnimationStates().get(Integer.parseInt(args[1]))
										.setCustomArmorstand(animationState.getCustomArmorstand());
								sender.sendMessage(ASLang.ANIMATION_UPDATE);

								if(args.length == 3) {
									animationState.setTicks(Integer.parseInt(args[2]));
									sender.sendMessage(ASLang.ANIMATION_UPDATE_TICKS);
								}

								armorstandHolder.getAnimation().getAnimationStates().get(Integer.parseInt(args[1])).save(
										armorstandHolder.getAnimation().getFile(), Integer.parseInt(args[1])
								);
							} catch (IndexOutOfBoundsException e) {
								sender.sendMessage(ASLang.ERROR_INDEX_OUT_OF_BOUNDS);
							} catch (NumberFormatException e) {
								sender.sendMessage(ASLang.ERROR_NUMBER_FORMAT);
							}
						}

					} else {
						sender.sendMessage(ASLang.NO_AS_SELECTED);
					}
				}

				//
				// listpos
				//
				case "listpos" -> {
					if(armorstandHolder != null) {

						if(armorstandHolder.getAnimation() == null) {
							sender.sendMessage(ASLang.ANIMATION_UNKNOWN);
							return;
						}

						sender.sendMessage(Lang.LINE);
						sender.sendMessage(ChatColor.YELLOW + "Animations: " + ChatColor.GOLD + armorstandHolder.getAnimation().getName());

						for(int i = 0; i < armorstandHolder.getAnimation().getAnimationStates().size(); i++) {

							AnimationState animationState = armorstandHolder.getAnimation().getAnimationStates().get(i);

							sender.sendMessage(ChatColor.GREEN + "[" + i + "] " + ChatColor.GRAY + "Ticks: " + ChatColor.WHITE + animationState.getTicks());
						}

						sender.sendMessage(Lang.LINE);

					} else {
						sender.sendMessage(ASLang.NO_AS_SELECTED);
					}
				}

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
							sender.sendMessage(ASLang.ERROR_INDEX_OUT_OF_BOUNDS);
						} catch (NumberFormatException e) {
							sender.sendMessage(ASLang.ERROR_NUMBER_FORMAT);
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

						try {
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

						} catch (IndexOutOfBoundsException e) {
							sender.sendMessage(ASLang.ERROR_INDEX_OUT_OF_BOUNDS);
						} catch (NumberFormatException e) {
							sender.sendMessage(ASLang.ERROR_NUMBER_FORMAT);
						}
					}
				}

				//
				// play
				//
				case "play" -> {
					if(args.length == 2) {
						try {
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
								ash.getAnimation().play(ash);

							} else {
								sender.sendMessage(ASLang.ANIMATION_UNKNOWN);
							}

						} catch (IndexOutOfBoundsException e) {
							sender.sendMessage(ASLang.ERROR_INDEX_OUT_OF_BOUNDS);
						} catch (NumberFormatException e) {
							sender.sendMessage(ASLang.ERROR_NUMBER_FORMAT);
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

						try {
							if(armorstandHolder.getAnimation() == null) {
								sender.sendMessage(ASLang.ANIMATION_UNKNOWN);
								return;
							}

							AnimationState animationState = new AnimationState(new CustomArmorstand(armorstandHolder.getCustomArmorstand()));

							armorstandHolder.getAnimation().getAnimationStates().add(animationState);

							if(args.length == 2) {
								animationState.setTicks(Integer.parseInt(args[1]));
							}

							animationState.save(armorstandHolder.getAnimation().getFile(), armorstandHolder.getAnimation().getAnimationStates().size() -1);

							sender.sendMessage(ASLang.ANIMATION_ADD);

						} catch (IndexOutOfBoundsException e) {
							sender.sendMessage(ASLang.ERROR_INDEX_OUT_OF_BOUNDS);
						} catch (NumberFormatException e) {
							sender.sendMessage(ASLang.ERROR_NUMBER_FORMAT);
						}

					} else {
						sender.sendMessage(ASLang.NO_AS_SELECTED);
					}
				}
			}
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		if(args.length == 1) {
			return Arrays.asList("create", "addpos", "removepos", "listpos", "setpos", "movepos", "play", "stop", "loop", "reverse", "set", "clone");
		}

		return super.tabComplete(sender, alias, args);
	}
}