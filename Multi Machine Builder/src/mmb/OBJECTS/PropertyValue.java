/**
 * 
 */
package mmb.OBJECTS;

/**
 * @author oskar
 *
 */
public interface PropertyValue {
	/**
	 * @return the expected type of value
	 */
	public Class<?> expectedType();
	/**
	 * @return the value contained in this property value
	 */
	public Object getValue();
	/**
	 * Sets the value in this property value
	 * @param value the new value
	 * @throws ClassCastException if value is of wrong type
	 * @throws NullPointerException if value is null and the property value does not allow null values
	 */
	public void setValue(Object value);
	/**
	 * @return is this property value nullable?
	 */
	public boolean isNullable();
}
