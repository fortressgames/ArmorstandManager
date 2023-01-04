package net.fortressgames.armorstandmanager.armorstands;

import net.fortressgames.fortressapi.FortressRunnable;
import net.fortressgames.fortressapi.entities.CustomArmorstand;
import net.fortressgames.fortressapi.players.PlayerModule;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class AnimationTask extends FortressRunnable {

	// the current moving armorstand
	private final ArmorstandHolder armorstandHolder;
	private final CustomArmorstand customArmorstand;
	private final Animation animation;

	private int frame;
	private int ticks;

	private final boolean reverse;

	public AnimationTask(ArmorstandHolder armorstandHolder, Animation animation, boolean reverse) {
		this.armorstandHolder = armorstandHolder;
		this.customArmorstand = armorstandHolder.getCustomArmorstand();
		this.animation = animation;
		this.reverse = reverse;

		old = new CustomArmorstand(customArmorstand);

		if(reverse) {
			frame = animation.getAnimationStates().size() -1;
		}
	}

	private CustomArmorstand old;

	@Override
	public void run() {

		// end
		if(reverse) {
			if(frame < 0) {

				if(armorstandHolder.isLoopAnimation()) {
					frame = animation.getAnimationStates().size() -1;
					return;
				}

				stop();
				return;
			}

		} else {
			if(frame >= animation.getAnimationStates().size()) {

				if(armorstandHolder.isLoopAnimation()) {
					frame = 0;
					return;
				}

				stop();
				return;
			}
		}

		// target state
		AnimationState animationState = animation.getAnimationStates().get(frame);

		Vector locationVector = getVector(animationState.getCustomArmorstand().getLocation(), old.getLocation(), animationState);
		customArmorstand.setLocation(new Location(customArmorstand.getLocation().getWorld(),
				animationState.getCustomArmorstand().getLocation().getX() -(locationVector.getX() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getLocation().getY() -(locationVector.getY() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getLocation().getZ() -(locationVector.getZ() * (animationState.getTicks() - ticks))
		));

		//
		// body parts
		//
		Vector headVector = getVector(animationState.getCustomArmorstand().getHeadPose(), old.getHeadPose(), animationState);
		customArmorstand.setHeadPose(new EulerAngle(
				animationState.getCustomArmorstand().getHeadPose().getX() -(headVector.getX() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getHeadPose().getY() -(headVector.getY() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getHeadPose().getZ() -(headVector.getZ() * (animationState.getTicks() - ticks))
		));

		Vector bodyVector = getVector(animationState.getCustomArmorstand().getBodyPose(), old.getBodyPose(), animationState);
		customArmorstand.setBodyPose(new EulerAngle(
				animationState.getCustomArmorstand().getBodyPose().getX() -(bodyVector.getX() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getBodyPose().getY() -(bodyVector.getY() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getBodyPose().getZ() -(bodyVector.getZ() * (animationState.getTicks() - ticks))
		));

		Vector rightLegVector = getVector(animationState.getCustomArmorstand().getRightLegPose(), old.getRightLegPose(), animationState);
		customArmorstand.setRightLegPose(new EulerAngle(
				animationState.getCustomArmorstand().getRightLegPose().getX() -(rightLegVector.getX() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getRightLegPose().getY() -(rightLegVector.getY() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getRightLegPose().getZ() -(rightLegVector.getZ() * (animationState.getTicks() - ticks))
		));

		Vector leftLegVector = getVector(animationState.getCustomArmorstand().getLeftLegPose(), old.getLeftLegPose(), animationState);
		customArmorstand.setLeftLegPose(new EulerAngle(
				animationState.getCustomArmorstand().getLeftLegPose().getX() -(leftLegVector.getX() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getLeftLegPose().getY() -(leftLegVector.getY() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getLeftLegPose().getZ() -(leftLegVector.getZ() * (animationState.getTicks() - ticks))
		));

		Vector rightArmVector = getVector(animationState.getCustomArmorstand().getRightArmPose(), old.getRightArmPose(), animationState);
		customArmorstand.setRightArmPose(new EulerAngle(
				animationState.getCustomArmorstand().getRightArmPose().getX() -(rightArmVector.getX() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getRightArmPose().getY() -(rightArmVector.getY() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getRightArmPose().getZ() -(rightArmVector.getZ() * (animationState.getTicks() - ticks))
		));

		Vector leftArmVector = getVector(animationState.getCustomArmorstand().getLeftArmPose(), old.getLeftArmPose(), animationState);
		customArmorstand.setLeftArmPose(new EulerAngle(
				animationState.getCustomArmorstand().getLeftArmPose().getX() -(leftArmVector.getX() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getLeftArmPose().getY() -(leftArmVector.getY() * (animationState.getTicks() - ticks)),
				animationState.getCustomArmorstand().getLeftArmPose().getZ() -(leftArmVector.getZ() * (animationState.getTicks() - ticks))
		));

		//
		// Armor
		//
		customArmorstand.setItemInMainHand(animationState.getCustomArmorstand().getItemInMainHand());
		customArmorstand.setItemInOffHand(animationState.getCustomArmorstand().getItemInOffHand());
		customArmorstand.setHelmet(animationState.getCustomArmorstand().getHelmet());
		customArmorstand.setChestplate(animationState.getCustomArmorstand().getChestplate());
		customArmorstand.setLeggings(animationState.getCustomArmorstand().getLeggings());
		customArmorstand.setBoots(animationState.getCustomArmorstand().getBoots());

		//
		// Other
		//
		customArmorstand.setSmall(animationState.getCustomArmorstand().isSmall());
		customArmorstand.setGlowing(animationState.getCustomArmorstand().isGlowing());
		customArmorstand.setInvisible(animationState.getCustomArmorstand().isInvisible());
		customArmorstand.setArms(animationState.getCustomArmorstand().isArms());
		customArmorstand.setBasePlate(animationState.getCustomArmorstand().isBasePlate());
		customArmorstand.setCustomNameVisible(animationState.getCustomArmorstand().isCustomNameVisible());

		customArmorstand.setCustomName(animationState.getCustomArmorstand().getCustomName());

		PlayerModule.getInstance().getOnlinePlayers().forEach(customArmorstand::update);

		ticks++;

		if(ticks > animationState.getTicks()) {
			if(reverse) {
				frame--;
			} else {
				frame++;
			}
			ticks = 0;
			old = new CustomArmorstand(customArmorstand);
		}
	}

	private Vector getVector(EulerAngle base, EulerAngle old, AnimationState animationState) {
		double LAX = getValue(base.getX(), old.getX(), animationState.getTicks());
		double LAY = getValue(base.getY(), old.getY(), animationState.getTicks());
		double LAZ = getValue(base.getZ(), old.getZ(), animationState.getTicks());

		return new Vector(LAX, LAY, LAZ);
	}

	private Vector getVector(Location base, Location old, AnimationState animationState) {
		double LAX = getValue(base.getX(), old.getX(), animationState.getTicks());
		double LAY = getValue(base.getY(), old.getY(), animationState.getTicks());
		double LAZ = getValue(base.getZ(), old.getZ(), animationState.getTicks());

		return new Vector(LAX, LAY, LAZ);
	}

	private double getValue(double state, double last, int ticks) {
		if(ticks == 0) {
			return 0;
		}

		double i = Math.abs(last - state) / ticks;

		if(state > last) {
			return i;
		} else {
			return -i;
		}
	}

	public void stop() {
		cancel();
		AnimationModule.getInstance().getRunning().remove(customArmorstand);
	}
}