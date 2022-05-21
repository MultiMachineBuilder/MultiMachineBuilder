/**
 * 
 */
package mmb.DATA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Settings {
	private Settings() {}
	private static final Debugger debug = new Debugger("SETTINGS");
	/**
	 * The settings file
	 */
	private static final Properties props = new Properties();
	/**
	 * @param key key
	 * @return the value in this property list with the specified key value
	 * @see java.util.Properties#getProperty(java.lang.String)
	 */
	public static String get(String key) {
		return props.getProperty(key);
	}
	/**
	 * @param key key
	 * @param value value
	 * @return the previous value of the specified key in this property list, or {@code null} if it did not have one.
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	public static Object set(String key, String value) {
		return props.setProperty(key, value);
	}
	private static boolean hasCreated = false;
	public static void loadSettings() {
		boolean doTryLoad = true;
		if(hasCreated) return;
		File settings = new File("settings.properties");
		try {
			if(settings.createNewFile()) {
				debug.printl("Created settings file");
				doTryLoad = false;
			}
		} catch (IOException e) {
			debug.pstm(e, "Failed to create settings file");
			doTryLoad = false;
		}
		createDefaultSettings();
		if(doTryLoad) try(InputStream in = new FileInputStream(settings)){
			props.load(in);
		} catch (Exception e) {
			debug.pstm(e, "Failed to read settings file");
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try(OutputStream out = new FileOutputStream(settings)){
				props.store(out, "MMB settings file");
				debug.printl("Saved settings file");
			} catch (Exception e) {
				debug.pstm(e, "Failed to save settings");
			}
		}));
		for(Entry<String, Setting> ent: settingModules.entrySet()) {
			String key = ent.getKey();
			Setting setting = ent.getValue();
			String val = get(key);
			if(val == null) {
				setting.onload.accept(setting.defalt);
				set(key, setting.defalt);
			}
			else setting.onload.accept(val);
		}
		hasCreated = true;
	}
	private static void createDefaultSettings() {
		props.setProperty("fullscreen", "false");
	}
	/**
	 * @param key key
	 * @param def default value for boolean
	 * @return retrieved boolean value, or default if unable to retrieve or process
	 */
	public static boolean getBool(String key, boolean def) {
		String result = props.getProperty(key);
		if(result == null) return def;
		switch(result.toLowerCase()) {
		case "true":
			return true;
		case "false":
			return false;
		default:
			return def;
		}
	}
	/**
	 * @param key key
	 * @return retrieved boolean value, or null if unable to retrieve or process
	 */
	public static @Nullable Boolean getOptionalBool(String key) {
		String result = props.getProperty(key);
		if(result == null) return null;
		return Boolean.valueOf(result);
	}
	
	//Setting modules
	private static final Map<String, Setting> settingModules0 = new HashMap<>();
	public static final Map<String, Setting> settingModules = Collections.unmodifiableMap(settingModules0);
	public static class Setting{
		public final Consumer<@Nonnull String> onload;
		public final Supplier<String> save;
		public final String defalt;
		public Setting(Consumer<@Nonnull String> onload, Supplier<String> save, String defalt) {
			super();
			this.onload = onload;
			this.save = save;
			this.defalt = defalt;
		}
	}
	
	/**
	 * @param name
	 * @param defalt the default value of the property
	 * @param onload method which handles setting loading. The method
	 * @param save method which provides strings for saving
	 */
	public static void addSettingString(String name, String defalt, Consumer<@Nonnull String> onload, Supplier<String> save) {
		settingModules0.put(name, new Setting(onload, save, defalt));
		if(hasCreated) {
			String val = get(name);
			if(val == null) {
				onload.accept(defalt);
				set(name, defalt);
			}
			else onload.accept(val);
		}
	}
}
