package net.fortressgames.armorstandmanager.armorstands;

import net.fortressgames.fortressapi.FortressRunnable;
import net.fortressgames.fortressapi.entities.CustomArmorstand;
import net.fortressgames.fortressapi.players.PlayerModule;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;

public class AnimationTask extends FortressRunnable {

	private final CustomArmorstand customArmorstand;
	private final Animation animation;

	private int frame;
	private int ticks;

	public AnimationTask(CustomArmorstand customArmorstand, Animation animation) {
		this.customArmorstand = customArmorstand;
		this.animation = animation;

		old = new CustomArmorstand(customArmorstand);
	}

	private CustomArmorstand old;

	@Override
	public void run() {
		// end
		if(frame >= animation.getAnimationStates().size()) {
			stop();
			return;
		}

		// target state
		AnimationState animationState = animation.getAnimationStates().get(frame);

		// get value to add/remove each tick
		double locX = getValue(animationState.getCustomArmorstand().getLocation().getX(), old.getLocation().getX(), animationState.getTicks());
		double locY = getValue(animationState.getCustomArmorstand().getLocation().getY(), old.getLocation().getY(), animationState.getTicks());
		double locZ = getValue(animationState.getCustomArmorstand().getLocation().getZ(), old.getLocation().getZ(), animationState.getTicks());

		customArmorstand.setLocation(new Location(customArmorstand.getLocation().getWorld(),
				customArmorstand.getLocation().getX() + locX,
				customArmorstand.getLocation().getY() + locY,
				customArmorstand.getLocation().getZ() + locZ
		));

		// get value to add/remove each tick
		double headX = getValue(animationState.getCustomArmorstand().getHeadPose().getX(), old.getHeadPose().getX(), animationState.getTicks());
		double headY = getValue(animationState.getCustomArmorstand().getHeadPose().getY(), old.getHeadPose().getY(), animationState.getTicks());
		double headZ = getValue(animationState.getCustomArmorstand().getHeadPose().getZ(), old.getHeadPose().getZ(), animationState.getTicks());

		customArmorstand.setHeadPose(new EulerAngle(
				customArmorstand.getHeadPose().getX() + headX,
				customArmorstand.getHeadPose().getY() + headY,
				customArmorstand.getHeadPose().getZ() + headZ
		));

		// get value to add/remove each tick
		double BodyX = getValue(animationState.getCustomArmorstand().getBodyPose().getX(), old.getBodyPose().getX(), animationState.getTicks());
		double BodyY = getValue(animationState.getCustomArmorstand().getBodyPose().getY(), old.getBodyPose().getY(), animationState.getTicks());
		double BodyZ = getValue(animationState.getCustomArmorstand().getBodyPose().getZ(), old.getBodyPose().getZ(), animationState.getTicks());

		customArmorstand.setBodyPose(new EulerAngle(
				customArmorstand.getBodyPose().getX() + BodyX,
				customArmorstand.getBodyPose().getY() + BodyY,
				customArmorstand.getBodyPose().getZ() + BodyZ
		));

		// get value to add/remove each tick
		double RLX = getValue(animationState.getCustomArmorstand().getRightLegPose().getX(), old.getRightLegPose().getX(), animationState.getTicks());
		double RLY = getValue(animationState.getCustomArmorstand().getRightLegPose().getY(), old.getRightLegPose().getY(), animationState.getTicks());
		double RLZ = getValue(animationState.getCustomArmorstand().getRightLegPose().getZ(), old.getRightLegPose().getZ(), animationState.getTicks());

		customArmorstand.setRightLegPose(new EulerAngle(
				customArmorstand.getRightLegPose().getX() + RLX,
				customArmorstand.getRightLegPose().getY() + RLY,
				customArmorstand.getRightLegPose().getZ() + RLZ
		));

		// get value to add/remove each tick
		double LLX = getValue(animationState.getCustomArmorstand().getLeftLegPose().getX(), old.getLeftLegPose().getX(), animationState.getTicks());
		double LLY = getValue(animationState.getCustomArmorstand().getLeftLegPose().getY(), old.getLeftLegPose().getY(), animationState.getTicks());
		double LLZ = getValue(animationState.getCustomArmorstand().getLeftLegPose().getZ(), old.getLeftLegPose().getZ(), animationState.getTicks());

		customArmorstand.setLeftLegPose(new EulerAngle(
				customArmorstand.getLeftLegPose().getX() + LLX,
				customArmorstand.getLeftLegPose().getY() + LLY,
				customArmorstand.getLeftLegPose().getZ() + LLZ
		));

		// get value to add/remove each tick
		double RAX = getValue(animationState.getCustomArmorstand().getRightArmPose().getX(), old.getRightArmPose().getX(), animationState.getTicks());
		double RAY = getValue(animationState.getCustomArmorstand().getRightArmPose().getY(), old.getRightArmPose().getY(), animationState.getTicks());
		double RAZ = getValue(animationState.getCustomArmorstand().getRightArmPose().getZ(), old.getRightArmPose().getZ(), animationState.getTicks());

		customArmorstand.setRightArmPose(new EulerAngle(
				customArmorstand.getRightArmPose().getX() + RAX,
				customArmorstand.getRightArmPose().getY() + RAY,
				customArmorstand.getRightArmPose().getZ() + RAZ
		));

		// get value to add/remove each tick
		double LAX = getValue(animationState.getCustomArmorstand().getLeftArmPose().getX(), old.getLeftArmPose().getX(), animationState.getTicks());
		double LAY = getValue(animationState.getCustomArmorstand().getLeftArmPose().getY(), old.getLeftArmPose().getY(), animationState.getTicks());
		double LAZ = getValue(animationState.getCustomArmorstand().getLeftArmPose().getZ(), old.getLeftArmPose().getZ(), animationState.getTicks());

		customArmorstand.setLeftArmPose(new EulerAngle(
				customArmorstand.getLeftArmPose().getX() + LAX,
				customArmorstand.getLeftArmPose().getY() + LAY,
				customArmorstand.getLeftArmPose().getZ() + LAZ
		));

		customArmorstand.setItemInMainHand(animationState.getCustomArmorstand().getItemInMainHand());
		customArmorstand.setItemInOffHand(animationState.getCustomArmorstand().getItemInOffHand());
		customArmorstand.setHelmet(animationState.getCustomArmorstand().getHelmet());
		customArmorstand.setChestplate(animationState.getCustomArmorstand().getChestplate());
		customArmorstand.setLeggings(animationState.getCustomArmorstand().getLeggings());
		customArmorstand.setBoots(animationState.getCustomArmorstand().getBoots());

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
			frame++;
			ticks = 0;
			old = new CustomArmorstand(customArmorstand);
		}
	}

	private double getValue(double state, double last, int ticks) {
		double t = ticks;
		if(ticks == 0) {
			t = 0.00001;
		}

		double i = Math.abs(last - state) / t;

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