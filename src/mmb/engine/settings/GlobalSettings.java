/**
 * 
 */
package mmb.engine.settings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import javax.swing.JComponent;

import mmb.Main;
import mmb.NN;
import mmb.data.variables.ListenableDouble;
import mmb.data.variables.ListenableInt;
import mmb.data.variables.ListenableValue;
import mmb.data.variables.ListenableBoolean;
import mmb.engine.MutableResourceBundle;
import mmb.engine.debug.Debugger;
import mmbtool.garbler.Garbler;
import monniasza.collects.Collects;

/**
 * Game settings and trnaslation
 * @author oskar
 */
public class GlobalSettings {
	private GlobalSettings() {}
	@NN private static final Debugger debug = new Debugger("SETTINGS LIST");
	
	/** Is the game full screen? */
	public static final ListenableBoolean fullScreen = new ListenableBoolean();
	/** The country used for translation */
	@NN public static final ListenableValue<String> country = new ListenableValue<>("US");
	/** The game's UI language */
	@NN public static final ListenableValue<String> lang = new ListenableValue<>("en");
	/** Should blocks be logged for execution time? */
	@NN public static final ListenableBoolean logExcessiveTime = new ListenableBoolean();
	/** Should the game use native scaling */
	@NN public static final ListenableBoolean sysscale = new ListenableBoolean();
	/** Should items be sorted in inventory lists? */
	@NN public static final ListenableBoolean sortItems = new ListenableBoolean();
	/** The accuracy of circles rendered using OpenGL*/
	@NN public static final ListenableInt circleAccuracy = new ListenableInt(32);
	/** The UI scale mutiplier */
	@NN public static final ListenableDouble uiScale = new ListenableDouble(1);
	/** Should all modded resource bundles be dumped? */
	@NN public static final ListenableBoolean dumpBundles = new ListenableBoolean();
	
	//localization
	/** @return the locale object for current language and country */
	@NN public static Locale locale() {
		return new Locale(lang.get(), country.get());
	}
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
	 * @throws MissingResourceException when defined string is not present
	 */
	@NN public static String $res(String s) {
		if(!Main.isRunning()) return s;
		return bundle().getString(s);
	}
	/**
	 * Gets the translated string or a placeholder 
	 * @param s dictionary key
	 * @return a translated string or a placeholder
	 */
	@NN public static String $res1(String s) {
		if(bundle == null || !bundle.containsKey(s)) {
			debug.printl("Missed translation: "+s);
			return "UNTRANSLATABLE "+s;
		}
		return bundle.getString(s);
	}
	/**
	 * Converts a string template.
	 * If a template begins with a '#', it is the looked up
	 * @param s string template
	 * @return a translated string
	 * @throws MissingResourceException when defined string is a key is not present
	 */
	@NN public static String $str(String s) {
		if(s.charAt(0) == '#')
			return bundle().getString(s.substring(1));
		return s;
	}
	/**
	 * Converts a string template.
	 * If a template begins with a '#', it is the looked up
	 * @param s string template
	 * @return a translated string or a placeholder
	 */
	@NN public static String $str1(String s) {
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
		ResourceBundle actualBundle;
		bundle = new MutableResourceBundle();
		actualBundle = ResourceBundle.getBundle("mmb/bundle");
		injectResources(actualBundle);
		hasInited = 2;
	}
	
	//hacking the ResourceBundle 
	/**
	 * Injects a resource bundle
	 * @param src resource bundle
	 */
	public static void injectResources(ResourceBundle src) {
		Map<String, String> map2 = new HashMap<>();
		bundle.add(src);
		if(dumpBundles.getValue()) dumpBundle(src);
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
