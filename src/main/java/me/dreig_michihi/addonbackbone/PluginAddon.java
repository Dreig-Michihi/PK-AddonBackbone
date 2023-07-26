package me.dreig_michihi.addonbackbone;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.PassiveAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.event.BendingReloadEvent;
import me.dreig_michihi.addonbackbone.config.PluginConfig;
import me.dreig_michihi.addonbackbone.config.annotations.AssociatedAbility;
import me.dreig_michihi.addonbackbone.config.annotations.Comment;
import me.dreig_michihi.addonbackbone.config.annotations.Configurable;
import me.dreig_michihi.addonbackbone.config.annotations.Description;
import me.dreig_michihi.addonbackbone.config.annotations.Instructions;
import me.dreig_michihi.addonbackbone.util.PluginAbility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import sun.reflect.ReflectionFactory;

import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class PluginAddon extends JavaPlugin {
	public static PluginAddon plugin;

	private static final Properties properties;
	private static JarFile jar = null;

	static {
		try {
			properties = new Properties();
			properties.load(PluginAddon.class.getClassLoader().getResourceAsStream("plugin.yml"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String get(String key) {
		/*// Protect your authorship in abilities info with this crutch ;)
		if (key.substring(0, 6).equalsIgnoreCase("author")) {
			return "Dreig_Michihi";
		}*/

		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		} else {
			info("Can't get property \"" + key + "\" from plugin.yml!");
			throw new IllegalArgumentException();
		}
	}

	public static void info(String message) {
		Bukkit.getConsoleSender().sendMessage("[" + get("full-name") + "]: " + ChatColor.DARK_GREEN + message);
	}

	public static void debugInfo(String message) {
		if (get("debug-info").equalsIgnoreCase("true")) {
			Bukkit.getConsoleSender().sendMessage("[" + get("full-name") + "]: " + ChatColor.GOLD + message);
		}
	}

	@Override
	public void onEnable() {
		debugInfo("\nonEnable() start");
		plugin = this;
		try {
			jar = new JarFile(PluginAddon.class.getProtectionDomain().getCodeSource().getLocation().getFile());
			debugInfo("JarFile with code source: " + jar.getName());
			handleJar();
			Bukkit.getServer().getPluginManager().registerEvents(new BendingReloadListener(), PluginAddon.plugin);
			info("Plugin-addon made by " + get("author") + " has been successfully loaded!");
		} catch (IOException e) {
			info("""
					The plugin can't handle itself, so it can't be loaded.
					Addon-plugin's file should have read access.
					IOException -> RuntimeException:
					""" + e.getMessage());
			throw new RuntimeException(e);
		}
		CoreAbility.registerPluginAbilities(plugin, get("abilities-package-base"));
		debugInfo("\nonEnable() end");
	}

	@Override
	public void onDisable() {
		debugInfo("\nonDisable() start");
		HandlerList.unregisterAll(PluginAddon.plugin);
		info("[" + get("full-name") + "] "
				+ "Plugin-addon made by " + get("author") + " has been successfully stopped!");
		debugInfo("\nonDisable() end");
	}

	private static CoreAbility getAbility(Class clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		final ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
		final Constructor<?> objDef = Object.class.getDeclaredConstructor();
		final Constructor<?> intConstr = rf.newConstructorForSerialization(clazz, objDef);
		final CoreAbility ability = (CoreAbility) clazz.cast(intConstr.newInstance());
		return ability;
	}

	private static void handleJar() {
		debugInfo("\nhandleJar() start");
		final Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			final JarEntry entry = entries.nextElement();
			debugInfo("entry: " + entry.getName());
			if (!entry.getName().endsWith(".class") || entry.getName().contains("$")) {
				debugInfo("The entry is skipped, because its name doesn't end with \".class\" or contain the symbol \"$\".");
				continue;
			}
			final String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
			debugInfo("class name: " + className);
			if (!className.startsWith(get("abilities-package-base"))) {
				debugInfo("The entry is skipped, because class's name doesn't start with \"" + (get("abilities-package-base")) + "\"");
				continue;
			}
			Class<?> clazz;
			try {
				clazz = Class.forName(className);
				if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
					debugInfo("The entry is skipped, because the class is interface or abstract.");
					continue;
				}
				if (Listener.class.isAssignableFrom(clazz)) {
					debugInfo("The entry is a listener.");
					new BukkitRunnable(){
						@Override
						public void run() {
							boolean shouldRegister = true;
							if (clazz.isAnnotationPresent(AssociatedAbility.class)) {
								String abilClassName = className.substring(0, className.lastIndexOf(".") + 1)
										+ clazz.getAnnotation(AssociatedAbility.class).value();
								try {
									CoreAbility ability = getAbility(Class.forName(abilClassName));
									shouldRegister = ability.isEnabled();
								} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
								         InstantiationException | IllegalAccessException e) {
									debugInfo("Class \"" + abilClassName + "\" not found.");
									shouldRegister = false;
								}
							}
							if (shouldRegister) {
								try {
									Listener listener = (Listener) clazz.getConstructor().newInstance();
									Bukkit.getServer().getPluginManager().registerEvents(listener, PluginAddon.plugin);
									debugInfo("The listener \"" + listener.getClass().getName() + "\" was registered successfully!");
								} catch (InstantiationException | IllegalAccessException | InvocationTargetException |
								         NoSuchMethodException e) {
									debugInfo("The listener wasn't registered, because some shit happened.");
								}
							} else {
								debugInfo("The listener wasn't registered, because the associated ability is disabled.");
							}
						}
					}.runTaskLater(PluginAddon.plugin, 1);
				} else if (PluginAbility.class.isAssignableFrom(clazz) && CoreAbility.class.isAssignableFrom(clazz)) {
					debugInfo("The entry is a CoreAbility implementing PluginAbility.");
					final CoreAbility ability = getAbility(clazz);
					handleAbility(ability);
				}
			} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
			         InstantiationException | IllegalAccessException e) {
				info("Error occurred while checking the entry: " + e.getMessage());
				throw new RuntimeException(e);
			}
			debugInfo("");
		}
		debugInfo("\nhandleJar() end");
	}

	private static void handleAbility(CoreAbility ability) {
		debugInfo("\nhandleAbility(" + ability.getName() + ") start");
		Class<CoreAbility> clazz = (Class<CoreAbility>) ability.getClass();
		{
			Element el = ability.getElement();
			String element = (el instanceof Element.SubElement ? ((Element.SubElement) el).getParentElement() : el).getName() + ".";
			String suffix = ability instanceof ComboAbility ? ".Combo." : ability instanceof PassiveAbility ? ".Passive." : "";
			debugInfo("Checking for annotations.");
			if (clazz.isAnnotationPresent(Instructions.class)) {
				debugInfo("\"@Instructions\" annotation is present.");
				String path = "Abilities."
						+ element
						+ suffix
						+ ability.getName()
						+ ".Instructions";
				ConfigManager.languageConfig.get().addDefault
						(path, clazz.getAnnotation(Instructions.class).value());
				debugInfo("Instructions were added with path: " + path);
			} else {
				debugInfo("\"@Instructions\" annotation isn't present.");
			}
			if (clazz.isAnnotationPresent(Description.class)) {
				debugInfo("\"@Description\" annotation is present.");
				String path = "Abilities."
						+ element
						+ suffix
						+ ability.getName()
						+ ".Description";
				ConfigManager.languageConfig.get().addDefault
						(path, clazz.getAnnotation(Description.class).value());
				debugInfo("Description was added with path: " + path);
			} else {
				debugInfo("\"@Description\" annotation isn't present.");
			}
		}
		debugInfo("Checking fields.");
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Configurable.class)) {
				debugInfo("Field \"" + field.getName() + "\" is configurable!");
				debugInfo("\"@Configurable\" annotation is present.");
				String suffix = field.getAnnotation(Configurable.class).value();
				if (!suffix.isEmpty())
					suffix += ".";
				String fieldName = field.getName();
				fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.toLowerCase().substring(1);
				AnnotatedType type = field.getAnnotatedType();
				debugInfo("The field's type: " + type);
				String path = PluginConfig.getPath((PluginAbility) ability, suffix + fieldName);
				debugInfo("Trying to get the value from config with path: " + path);
				try {
					if (ConfigManager.defaultConfig.get().contains(path)) {
						Object value = ConfigManager.defaultConfig.get().get(path);
						debugInfo("Gotten value: " + value);
						if (value != null && field.trySetAccessible()) {
							debugInfo("Trying to set the value for the field.");
							String typeStr = type.toString().toLowerCase();
							if (typeStr.equals("string")) {
								field.set(null, value.toString());
							} else if (value instanceof Double) {
								switch (typeStr) {
									case "float" -> field.set(null, ((Double) value).floatValue());
									case "integer", "int" -> field.set(null, ((Double) value).intValue());
									case "long" -> field.set(null, ((Double) value).longValue());
									case "short" -> field.set(null, ((Double) value).shortValue());
									case "byte" -> field.set(null, ((Double) value).byteValue());
									default -> field.set(null, value);
								}
							} else {
								field.set(null, value);
							}
						} else {
							debugInfo("Can't set config value " + path + ": " + value);
						}
					} else {
						debugInfo("The path is not present in config.\n" +
								"Generating default config string.");
						if (field.trySetAccessible()) {
							ConfigManager.defaultConfig.get().addDefault(path, field.get(null));
							ConfigManager.defaultConfig.save();
						} else {
							debugInfo("Can't get default config value " + path);
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					info("Error occurred while getting or adding config variable on path: " + path +
							"\n" + e.getMessage());
					throw new RuntimeException(e);
				}
				if (field.isAnnotationPresent(Comment.class)) {
					debugInfo("\"@Comment\" annotation is present.");
					PluginConfig.addComment(path, field.getAnnotation(Comment.class).value());
				}
			}
			debugInfo("");
		}
		debugInfo("\nhandleAbility(" + ability.getName() + ") end");
	}

	private static class BendingReloadListener implements Listener {
		@EventHandler
		public void onBendingReload(BendingReloadEvent event) {
			PluginAddon.plugin.onDisable();
			Bukkit.getScheduler().runTask(PluginAddon.plugin, PluginAddon.plugin::onEnable);
		}
	}
}
