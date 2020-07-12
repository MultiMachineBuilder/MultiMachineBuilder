/**
 * 
 */
package mmb.partfiles;

/**
 * @author oskar
 *
 */
public class ConfigClass extends ConfigElements {
	public static final Class classtype = new ConfigClass().getClass();
	public static boolean isThisType(Object obj) {
		return classtype.isInstance(obj);
	}
	
	/**
	 * 
	 */
	public ConfigClass() {
		// TODO Auto-generated constructor stub
	}

}
