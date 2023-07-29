package me.dreig_michihi.addonbackbone.config;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.PassiveAbility;
import commonslang3.projectkorra.lang3.StringUtils;
import me.dreig_michihi.addonbackbone.PluginAddon;
import me.dreig_michihi.addonbackbone.util.PluginAbility;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ListIterator;

public class PluginConfig {
	public static String getPath(PluginAbility ability, String postfix) {
		return "ExtraAbilities." +
				ability.getAuthor() + "." +
				PluginAddon.get("name") + "." +
				ability.getElement().getName() + "." +
				(ability instanceof ComboAbility ? "Combo." : ability instanceof PassiveAbility ? "Passive." : "") +
				ability.getName() + "." +
				postfix;
		//ExtraAbilities.<Author>.<Element>.[Combo.|Passive.]<AbilityName>.[Postfix.]<ConfigValue>: value
	}

	public static void addComment(String configPath, String commentary) {
		PluginAddon.debugInfo("Adding comment.");
		boolean inline = !commentary.contains("\n");
		String comment = commentary.replace("\n", "\n# ");
		PluginAddon.debugInfo(configPath + "\n" + commentary + "\ninline: " + inline);
		Bukkit.getScheduler().runTask(PluginAddon.plugin, () -> {
			try {
				Path path = ProjectKorra.plugin.getDataFolder().toPath().resolve("config.yml");
				List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
				if (!inline)
					lines.add(0, "");
				ListIterator<String> configPathSeparated = List.of(configPath.split("\\.")).listIterator();
				ListIterator<String> iter = lines.listIterator();
				String stringFinder = configPathSeparated.next();
				while (iter.hasNext()) {
					String curr = iter.next();
					if (curr.trim().startsWith(stringFinder)) {
						if (configPathSeparated.hasNext())
							stringFinder = configPathSeparated.next();
						else {
							if (inline) {
								if (!curr.contains("#")) {
									iter.set(curr + " # " + comment);
								}
							} else {
								iter.previous();
								String prev = iter.previous();
								iter.next();
								int whitespaces = StringUtils.countMatches(curr, " ") - 1;
								String commentWithWhitespaces = ("# " + comment).replace("#", " ".repeat(whitespaces) + "#");
								if (!prev.trim().startsWith("#")) {
									iter.add(commentWithWhitespaces);
								}
								iter.next();
							}
							break;
						}
					}
				}
				if (!inline)
					lines.remove(0);
				Files.write(path, lines, StandardCharsets.UTF_8);
			} catch (IOException e) {
				PluginAddon.info("Error occurred while trying to add a config comment.");
			}
		});
	}
}
