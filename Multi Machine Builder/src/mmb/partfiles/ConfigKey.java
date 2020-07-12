package mmb.partfiles;

public class ConfigKey {
	public String id = "";
	public String value = "";
	public static final Class classtype = new ConfigKey().getClass();
	public static boolean isThisType(Object obj) {
		return classtype.isInstance(obj);
	}
	public ConfigKey() {
		// TODO Auto-generated constructor stub
	}

}
