/**
 * 
 */
package mmb.OBJECTS;

/**
 * @author oskar
 * An abstract object model
 */
public interface ObjectModel{
	/**
	 * Gets allowed object type for given variable
	 * @param prop property to check
	 * @return the expected type og
	 */
	public Class<?> getExpectedType(Property prop);
	public boolean isNullable(Property prop);
	
	/**
	 * 
	 * @param prop the name of property
	 * @return the exisitng property, or null if not found
	 */
	public PropertyValue get(Property prop);
	/**
	 * @param prop the property tag
	 * @param nullable are null values allowed
	 * @param type the expected type of property
	 * @return a new property, or existing if it already exists
	 */
	public PropertyValue create(Property prop, boolean nullable, Class<?> type);
}
