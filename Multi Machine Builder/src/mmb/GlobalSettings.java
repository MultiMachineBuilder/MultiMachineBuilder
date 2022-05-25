/**
 * 
 */
package mmb;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import mmb.DATA.Settings;
import mmb.DATA.variables.ListenableValue;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class GlobalSettings {
	private GlobalSettings() {};
	
	@Nonnull public static final ListenableValue<String> country = new ListenableValue<>("US");
	@Nonnull public static final ListenableValue<String> lang = new ListenableValue<>("en");
	@Nonnull private static final Debugger debug = new Debugger("SETTINGS LIST");
	
	public static Locale locale() {
		return new Locale(lang.get(), country.get());
	}
	
	//localization
	private static ResourceBundle bundle;
	public static ResourceBundle bundle() {
		if(bundle == null) throw new IllegalArgumentException("No bundle loaded!");
		return bundle;
	}
	public static String $res(String s) {
		if(!Main.isRunning()) return s;
		return bundle().getString(s);
	}
	public static String $res1(String s) {
		if(bundle == null || !bundle.containsKey(s)) return "UNTRANSLATABLE "+s;
		return bundle.getString(s);
	}
	public static String $str(String s) {
		if(s.charAt(0) == '#')
			return bundle().getString(s.substring(1));
		return s;
	}
	public static String $str1(String s) {
		if(s.charAt(0) == '#')
			return $res1(s.substring(1));
		return s;
	}
	
	/**
	 * Initializes global properties. Needed only by the {@link Loading} class, has no effect when used elsewhere
	 */
	private static boolean hasInited = false;
	public static void init() {
		if(hasInited) return;
		Settings.addSettingString("lang", "en", lang);
		Settings.addSettingString("country", "US", country);
		debug.printl("Language: "+lang.get());
		bundle = ResourceBundle.getBundle("mmb/bundle", locale());
		hasInited = true;
	}
	
}
