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
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import mmb.DATA.variables.BooleanVariable;
import mmb.DATA.variables.DataValueDouble;
import mmb.DATA.variables.DataValueInt;
import mmb.DATA.variables.ListenableValue;
import mmb.DATA.variables.Variable;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Settings {
	//Internals
	private Settings() {}
	private static final Debugger debug = new Debugger("SETTINGS");
	/**
	 * The settings file
	 */
	private static final Properties props = new Properties();
	
	//Initialization
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
		if(doTryLoad) try(InputStream in = new FileInputStream(settings)){
			props.load(in);
		} catch (Exception e) {
			debug.pstm(e, "Failed to read settings file");
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			saveSettings(settings);
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
	private static void saveSettings(File settings) {
		try(OutputStream out = new FileOutputStream(settings)){
			for(Entry<String, Setting> ent: settingModules.entrySet()) {
				String key = ent.getKey();
				String result = ent.getValue().save.get();
				debug.printl("Resetting a setting "+key+" with value "+result);
				props.setProperty(key, result);
			}
			for(Entry<Object, Object> ent: props.entrySet()) {
				debug.printl("Saving a setting "+ent.getKey()+" with value "+ent.getValue());
			}
			props.store(out, "MMB settings file");
			out.flush();
			debug.printl("Saved settings file");
		} catch (Exception e) {
			debug.pstm(e, "Failed to save settings");
		}
	}
	
	//Get/set
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
	
	//Setting modules
	/**
	 * Adds a string setting
	 * @param name the name of a setting
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
	
	public static void addSettingString(String name, String defalt, ListenableValue<String> variable) {
		addSettingString(name, defalt, variable::set, variable::get);
	}
	/**
	 * Adds a boolean setting
	 * @param name the name of a setting
	 * @param defalt the default value of the property
	 * @param onload method which handles setting loading. The method
	 * @param save method which provides strings for saving
	 */
	public static void addSettingBool(String name, boolean defalt, BooleanConsumer onload, BooleanSupplier save) {
		addSettingString(name, Boolean.toString(defalt), s -> onload.accept(Boolean.parseBoolean(s)), () -> Boolean.toString(save.getAsBoolean()));
	}
	/**
	 * Adds a boolean setting
	 * @param name the name of a setting
	 * @param defalt the default value of the property
	 * @param variable a boolean variable
	 */
	public static void addSettingBool(String name, boolean defalt, BooleanVariable variable) {
		addSettingBool(name, defalt, variable::setValue, variable::getValue);
	}
	/**
	 * Adds a boolean setting
	 * @param name the name of a setting
	 * @param defalt the default value of the property
	 * @param onload method which handles setting loading. The method
	 * @param save method which provides strings for saving
	 */
	public static void addSettingInt(String name, boolean defalt, IntConsumer onload, IntSupplier save) {
		addSettingString(name, Boolean.toString(defalt), s -> onload.accept(Integer.parseInt(s)), () -> Integer.toString(save.getAsInt()));
	}
	/**
	 * Adds a boolean setting
	 * @param name the name of a setting
	 * @param defalt the default value of the property
	 * @param onload method which handles setting loading. The method
	 * @param save method which provides strings for saving
	 */
	public static void addSettingDbl(String name, double defalt, DoubleConsumer onload, DoubleSupplier save) {
		addSettingString(name, Double.toString(defalt), s -> onload.accept(Double.parseDouble(s)), () -> Double.toString(save.getAsDouble()));
	}
	/**
	 * Adds a boolean setting
	 * @param name the name of a setting
	 * @param i the default value of the property
	 * @param onload method which handles setting loading. The method
	 * @param save method which provides strings for saving
	 */
	public static void addSettingDbl(String name, double i, DataValueDouble variable) {
		addSettingDbl(name, i, variable::set, variable::getDouble);
	}
	/**
	 * Adds a boolean setting
	 * @param name the name of a setting
	 * @param defalt the default value of the property
	 * @param onload method which handles setting loading. The method
	 * @param save method which provides strings for saving
	 */
	public static void addSettingInt(String name, int defalt, IntConsumer onload, IntSupplier save) {
		addSettingString(name, Integer.toString(defalt), s -> onload.accept(Integer.parseInt(s)), () -> Integer.toString(save.getAsInt()));
	}
	/**
	 * Adds an ineteger setting
	 * @param name the name of a setting
	 * @param i the default value of the property
	 * @param variable variable
	 */
	public static void addSettingInt(String name, int i, DataValueInt variable) {
		addSettingInt(name, i, variable::set, variable::getInt);
	}
	/**
	 * @param <T> type of value 
	 * @param name the name of a setting
	 * @param defalt the default value of the property
	 * @param onload method which handles setting loading. The method
	 * @param save method which provides strings for saving
	 * @param deser the function, which converts a string into a corresponding object
	 */
	public static <T> void addSettingExt(String name, T defalt, Consumer<T> onload, Supplier<T> save, Function<String, T> deser) {
		addSettingString(name, defalt.toString(), s -> onload.accept(deser.apply(s)), () -> save.get().toString());
	}
	/**
	 * @param <T> type of value 
	 * @param name the name of a setting
	 * @param defalt the default value of the property
	 * @param onload method which handles setting loading. The method
	 * @param save method which provides strings for saving
	 * @param deser the function, which converts a string into a corresponding object
	 */
	public static <T> void addSettingExt(String name, T defalt, Variable<T> variable, Function<String, T> deser) {
		addSettingExt(name, defalt, variable::set, variable::get, deser);
	}
	
	//Shared settings
	
}