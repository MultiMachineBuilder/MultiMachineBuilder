/**
 * 
 */
package mmb.content.wireworld;

/**
 * @author oskar
 *
 */
public interface TextMessageProvider {
	/**
	 * Get the contained message
	 * @return a {@link String} with this {@code TextMessageProvider}'s message
	 */
	public String getMessage();
	/**
	 * Set the contained message
	 * @param msg new message
	 */
	public void setMessage(String msg);
}
