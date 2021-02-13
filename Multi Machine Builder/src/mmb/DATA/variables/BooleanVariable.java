/**
 * 
 */
package mmb.DATA.variables;

/**
 * @author oskar
 *
 */
public class BooleanVariable {
	boolean value;
	/**
	 * @return the value
	 */
	public boolean getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(boolean value) {
		this.value = value;
	}
	/**
	 * Create a boolean container object using a value
	 * @param value
	 */
	public BooleanVariable(boolean value) {
		this.value = value;
	}
	/** Create a boolean container with default value({@code false})*/
	public BooleanVariable() {}
	
}
