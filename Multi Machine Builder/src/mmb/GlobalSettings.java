/**
 * 
 */
package mmb;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import mmb.DATA.Settings;
import mmb.DATA.variables.ListenableDouble;
import mmb.DATA.variables.ListenableInt;
import mmb.DATA.variables.ListenableValue;
import mmb.DATA.variables.ListenerBooleanVariable;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class GlobalSettings {
	private GlobalSettings() {}
	/** The country used for translation */
	@Nonnull public static final ListenableValue<String> country = new ListenableValue<>("US");
	/** The game's UI language */
	@Nonnull public static final ListenableValue<String> lang = new ListenableValue<>("en");
	/** Should blocks be logged for execution time? */
	@Nonnull public static final ListenerBooleanVariable logExcessiveTime = new ListenerBooleanVariable();
	/** The accuracy of circles rendered using OpenGL*/
	@Nonnull public static final ListenableInt circleAccuracy = new ListenableInt(32);
	/** The UI scale mutiplier*/
	@Nonnull public static final ListenableDouble uiScale = new ListenableDouble(1);
	@Nonnull private static final Debugger debug = new Debugger("SETTINGS LIST");
	
	/** @return the locale object for current language and country */
	public static Locale locale() {
		return new Locale(lang.get(), country.get());
	}
	
	//localization
	private static MutableResourceBundle bundle;
	/**
	 * @throws IllegalStateException if the bundle is not loaded
	 * @return the resource bundle used for translations
	 */
	public static MutableResourceBundle bundle() {
		if(bundle == null) throw new IllegalArgumentException("No bundle loaded!");
		return bundle;
	}
	/**
	 * Gets the translated string
	 * @param s dictionary key
	 * @return
	 */
	@Nonnull public static String $res(String s) {
		if(!Main.isRunning()) return s;
		return bundle().getString(s);
	}
	@Nonnull public static String $res1(String s) {
		if(bundle == null || !bundle.containsKey(s)) return "UNTRANSLATABLE "+s;
		return bundle.getString(s);
	}
	@Nonnull public static String $str(String s) {
		if(s.charAt(0) == '#')
			return bundle().getString(s.substring(1));
		return s;
	}
	@Nonnull public static String $str1(String s) {
		if(s.charAt(0) == '#')
			return $res1(s.substring(1));
		return s;
	}
	
	
	private static boolean hasInited = false;
	/** Initializes global properties and translations. Needed only by the {@link Main} class, has no effect when used elsewhere */
	public static void init() {
		if(hasInited) return;
		Settings.addSettingString("lang", "en", lang);
		Settings.addSettingString("country", "US", country);
		Settings.addSettingBool("logExcessiveBlockTime", false, logExcessiveTime);
		Settings.addSettingDbl("scale", 1, uiScale);
		Settings.addSettingInt("circleAccuracy", 32, circleAccuracy);
		debug.printl("Language: "+lang.get());
		bundle = new MutableResourceBundle(ResourceBundle.getBundle("mmb/bundle", locale()));
		hasInited = true;
	}
	
	//hacking the ResourceBundle 
	/**
	 * Injects a resource bundle
	 * @param src resource bundle
	 */
	public static void injectResources(ResourceBundle src) {
		bundle().add(src);
	}
	
}
