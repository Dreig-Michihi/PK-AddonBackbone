package me.dreig_michihi.addonbackbone.util;

import com.projectkorra.projectkorra.ability.Ability;
import com.projectkorra.projectkorra.ability.AddonAbility;
import me.dreig_michihi.addonbackbone.PluginAddon;

public interface PluginAbility extends AddonAbility, Ability {

	@Override
	default void load() {
		PluginAddon.info(getName() + " by " + getAuthor() + " has been loaded!");
	}

	@Override
	default void stop() {
		PluginAddon.info(getName() + " by " + getAuthor() + " has been stopped!");
	}

	@Override
	default String getAuthor() {
		return PluginAddon.get("author");
	}

	@Override
	default String getVersion() {
		return PluginAddon.get("full-name");
	}

	@Override
	default String getName() {
		return this.getClass().getSimpleName();
	}
}
