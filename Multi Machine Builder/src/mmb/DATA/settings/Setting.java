/**
 * 
 */
package mmb.DATA.settings;

/**
 * @author oskar
 * Represents a setting
 * @param <T> the type of the value
 */
public abstract class Setting<T> {
	/**
	 * @return value of this setting
	 */
	public abstract T get();
	/**
	 * @return the int value of the setting
	 */
	@SuppressWarnings("static-method")
	public int getInt() {
		throw new IllegalStateException("This setting does not support ints");
	}
	/**
	 * @return the long value of the setting
	 */
	@SuppressWarnings("static-method")
	public long getLong() {
		throw new IllegalStateException("This setting does not support longs");
	}
	/**
	 * @return the float value of the setting
	 */
	@SuppressWarnings("static-method")
	public double getFloat() {
		throw new IllegalStateException("This setting does not support floats");
	}
	/**
	 * @return the char value of the setting
	 */
	@SuppressWarnings("static-method")
	public char getChar() {
		throw new IllegalStateException("This setting does not support chars");
	}
	/**
	 * @return the boolean value of the setting
	 */
	@SuppressWarnings("static-method")
	public boolean getBoolean() {
		throw new IllegalStateException("This setting does not support boolean");
	}
	/**
	 * @return the string value of the setting
	 */
	public String getString() {
		return get().toString();
	}
	
}
