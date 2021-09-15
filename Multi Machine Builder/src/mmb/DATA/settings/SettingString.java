/**
 * 
 */
package mmb.DATA.settings;

/**
 * @author oskar
 *
 */
public class SettingString extends Setting<String> {
	private String value;
	private String lvc; //Last Valid Cache
	
	private Long cacheInteger;
	private void calc() {
		if(value != lvc) cacheInteger = null; //NOSONAR 
		if(cacheInteger == null) {
			cacheInteger = Long.valueOf(value);
			lvc = value;
		}
	}
	@Override
	public int getInt() {
		calc();
		return cacheInteger.intValue();
	}
	@Override
	public long getLong() {
		calc();
		return cacheInteger.longValue();
	}
	
	private Double cacheFloat;
	private void calcf() {
		if(value != lvc) cacheFloat = null; //NOSONAR 
		if(cacheFloat == null) {
			cacheFloat = Double.valueOf(value);
			lvc = value;
		}
	}
	@Override
	public double getFloat() {
		calcf();
		return cacheFloat.doubleValue();
	}
	/**
	 * Returns first character, or if empty U+0000
	 */
	@Override
	public char getChar() {
		if(value.isEmpty()) return '\u0000';
		return value.charAt(0);
	}
	/**
	 * Converts a string to a boolean.
	 */
	@Override
	public boolean getBoolean() {
		return Boolean.parseBoolean(value);
	}
	@Override
	public String getString() {
		return value;
	}
	@Override
	public String get() {
		return value;
	}

}
