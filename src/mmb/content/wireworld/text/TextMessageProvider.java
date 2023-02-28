/**
 * 
 */
package mmb.content.wireworld.text;

import mmb.Nil;

/**
 * A bean interface used by the chatter.
 * The bean provides two methods, {@link #getMessage()} and {@link #setMessage(String)} to get and set the message respectively.
 * @author oskar
 * @see TextChatter
 * @see TextEditor
 */
public interface TextMessageProvider {
	/**
	 * Get the contained message
	 * @return a {@link String} with this {@code TextMessageProvider}'s message
	 */
	public @Nil String getMessage();
	/**
	 * Set the contained message
	 * @param msg new message
	 */
	public void setMessage(@Nil String msg);
}
