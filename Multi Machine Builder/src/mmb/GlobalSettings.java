/**
 * 
 */
package mmb;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import com.rainerhahnekamp.sneakythrow.Sneaky;

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
	
	@Nonnull public static final ListenableValue<String> country = new ListenableValue<>("US");
	@Nonnull public static final ListenableValue<String> lang = new ListenableValue<>("en");
	@Nonnull public static final ListenerBooleanVariable logExcessiveTime = new ListenerBooleanVariable();
	@Nonnull public static final ListenableInt circleAccuracy = new ListenableInt(32);
	@Nonnull public static final ListenableDouble uiScale = new ListenableDouble(1);
	@Nonnull private static final Debugger debug = new Debugger("SETTINGS LIST");
	
	public static Locale locale() {
		return new Locale(lang.get(), country.get());
	}
	
	//localization
	private static MutableResourceBundle bundle;
	public static MutableResourceBundle bundle() {
		if(bundle == null) throw new IllegalArgumentException("No bundle loaded!");
		return bundle;
	}
	@Nonnull public static String $res(String s) {
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
	 * Initializes global properties. Needed only by the {@link Main} class, has no effect when used elsewhere
	 */
	private static boolean hasInited = false;
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
	public static void injectResources(ResourceBundle src) {
		bundle().add(src);
	}
	
}
