/**
 * 
 */
package mmb.DATA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Settings {
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
}
