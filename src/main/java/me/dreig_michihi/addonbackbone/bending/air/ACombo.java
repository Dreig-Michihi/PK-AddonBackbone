package me.dreig_michihi.addonbackbone.bending.air;

import com.projectkorra.projectkorra.ability.AirAbility;
import me.dreig_michihi.addonbackbone.config.annotations.Combo;
import me.dreig_michihi.addonbackbone.config.annotations.Description;
import me.dreig_michihi.addonbackbone.config.annotations.Instructions;
import me.dreig_michihi.addonbackbone.util.PluginCombo;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Combo({"AirBlast" + Combo.SNEAK_DOWN, "AirSuction" + Combo.SNEAK_UP,})
@Description("Description for the test :3")
@Instructions("Sneak down on AirBlast, release sneak on AirSuction")
public class ACombo extends AirAbility implements PluginCombo {

	public ACombo(Player player) {
		super(player);
	}

	@Override
	public void progress() {

	}

	@Override
	public boolean isSneakAbility() {
		return false;
	}

	@Override
	public boolean isHarmlessAbility() {
		return false;
	}

	@Override
	public long getCooldown() {
		return 0;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public Object createNewComboInstance(Player player) {
		return null;
	}
}
