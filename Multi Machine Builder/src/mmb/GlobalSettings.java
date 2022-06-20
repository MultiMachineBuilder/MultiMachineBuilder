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
		Settings.addSettingBool("logExcessiveBlockTime", false, logExcessiveTime);
		debug.printl("Language: "+lang.get());
		bundle = ResourceBundle.getBundle("mmb/bundle", locale());
		hasInited = true;
	}
	
	//hacking the ResourceBundle
	private static final Field lookupField;
	static {
		Field field0 = Sneaky.sneak(() -> PropertyResourceBundle.class.getDeclaredField("lookup"));
		field0.setAccessible(true);
		lookupField = field0;
	}
	private static Map<String, Object> getMap4RB(ResourceBundle bundle) {
		return Sneaky.sneak(() -> (Map<String, Object>) lookupField.get(bundle));
	}
	private static void inject2resourceBundle(ResourceBundle tgt, ResourceBundle src) {
		Map<String, Object> map = getMap4RB(tgt);
		Enumeration<String> iter = src.getKeys();
		while(iter.hasMoreElements()) {
			String key = iter.nextElement();
			map.put(key, src.getObject(key));
		}
	}
	public static void injectResources(ResourceBundle src) {
		inject2resourceBundle(bundle(), src);
	}
	
}
