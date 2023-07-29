package me.dreig_michihi.avatachtweaks.util;

import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager;
import com.projectkorra.projectkorra.ability.util.ComboUtil;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import me.dreig_michihi.avatachtweaks.config.PluginConfig;

import java.util.ArrayList;

public interface PluginCombo extends ComboAbility, PluginAbility {
	@Override
	default ArrayList<ComboManager.AbilityInformation> getCombination() {
		String path = PluginConfig.getPath(this, "Combination");
		return ComboUtil.generateCombinationFromList(this, ConfigManager.defaultConfig.get().getStringList(path));
	}
}
