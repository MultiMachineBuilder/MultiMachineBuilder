package mmb.beans;

import mmb.engine.settings.GlobalSettings;

/**
 * Assigns a description to an object.
 * Optionally allows the description to be changed
 */
public interface IDescription {
	/**
	 * Gets the description of the object
	 * @return the object's description
	 */
	public String getDescription();
	/**
	 * Sets the description. If the target object does not override this method, it will fail.
	 * @param descr new description
	 * @throws UnsupportedOperationException when the target object does not implement this method
	 */
	public default void setDescription(String descr) {
		throw new UnsupportedOperationException(this.getClass().getCanonicalName() + " does not implement description setting");
	}
	/**
	 * Sets the description from a translation key
	 * @param description description translation string
	 */
	public default void translateDescription(String description) {
		setDescription(GlobalSettings.$res1(description));
	}
}
