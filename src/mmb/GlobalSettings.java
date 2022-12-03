/**
 * 
 */
package mmb;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

import mmb.data.Settings;
import mmb.data.variables.ListenableDouble;
import mmb.data.variables.ListenableInt;
import mmb.data.variables.ListenableValue;
import mmb.data.variables.ListenerBooleanVariable;
import mmb.debug.Debugger;
import monniasza.collects.Collects;

/**
 * @author oskar
 *
 */
public class GlobalSettings {
	private GlobalSettings() {}
	@Nonnull private static final Debugger debug = new Debugger("SETTINGS LIST");
	
	/** Is the game full screen? */
	public static final ListenerBooleanVariable fullScreen = new ListenerBooleanVariable();
	/** The country used for translation */
	@Nonnull public static final ListenableValue<String> country = new ListenableValue<>("US");
	/** The game's UI language */
	@Nonnull public static final ListenableValue<String> lang = new ListenableValue<>("en");
	/** Should blocks be logged for execution time? */
	@Nonnull public static final ListenerBooleanVariable logExcessiveTime = new ListenerBooleanVariable();
	/** Should the game use native scaling */
	@Nonnull public static final ListenerBooleanVariable sysscale = new ListenerBooleanVariable();
	/** Should items be sorted in inventory lists? */
	@Nonnull public static final ListenerBooleanVariable sortItems = new ListenerBooleanVariable();
	/** The accuracy of circles rendered using OpenGL*/
	@Nonnull public static final ListenableInt circleAccuracy = new ListenableInt(32);
	/** The UI scale mutiplier*/
	@Nonnull public static final ListenableDouble uiScale = new ListenableDouble(1);
	/** Should all modded resource bundles be dumped? */
	@Nonnull public static final ListenerBooleanVariable dumpBundles = new ListenerBooleanVariable();
	
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
	 * @return a translated string
	 */
	@Nonnull public static String $res(String s) {
		if(!Main.isRunning()) return s;
		return bundle().getString(s);
	}
	@Nonnull public static String $res1(String s) {
		if(bundle == null || !bundle.containsKey(s)) {
			debug.printl("Missed translation: "+s);
			return "UNTRANSLATABLE "+s;
		}
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
	
	
	private static int hasInited = 0;
	/** Initializes global properties. Needed only by the {@link Main} class, has no effect when used elsewhere */
	public static void init() {
		if(hasInited != 0) return;
		Settings.addSettingString("lang", "en", lang);
		Settings.addSettingString("country", "US", country);
		Settings.addSettingBool("logExcessiveBlockTime", false, logExcessiveTime);
		Settings.addSettingBool("swingDPI", false, sysscale);
		Settings.addSettingBool("sortItems", true, sortItems);
		Settings.addSettingBool("dumpBundles", false, dumpBundles);
		Settings.addSettingDbl("scale", 1, uiScale);
		Settings.addSettingInt("circleAccuracy", 32, circleAccuracy);
		hasInited = 1;
	}
	/** Initializes global translations. Needed only by the {@link Main} class, has no effect when used elsewhere */
	public static void translate() {
		if(hasInited != 1) return;
		debug.printl("Language: "+lang.get());
		Locale locale = locale();
		JComponent.setDefaultLocale(locale);
		Locale.setDefault(locale);
		ResourceBundle actualBundle = ResourceBundle.getBundle("mmb/bundle", locale);
		bundle = new MutableResourceBundle(actualBundle);
		hasInited = 2;
	}
	
	//hacking the ResourceBundle 
	/**
	 * Injects a resource bundle
	 * @param src resource bundle
	 */
	public static void injectResources(ResourceBundle src) {
		if(dumpBundles.getValue()) dumpBundle(src);
		bundle().add(src);
	}
	public static void dumpBundle(ResourceBundle src) {
		debug.printl("vvvv Resource bundle start "+src.getBaseBundleName()+" vvv");
		for(String key: Collects.iter(src.getKeys())) {
			Object value = src.getObject(key);
			debug.printl(key+"="+value);
		}
		debug.printl("^^^^ Resource bundle end ^^^");
	}
}
