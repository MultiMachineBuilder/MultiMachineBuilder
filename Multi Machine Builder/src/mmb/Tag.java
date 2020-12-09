/**
 * 
 */
package mmb;
import static java.util.Random.*;

import java.util.Random;
/**
 * @author oskar
 *
 */
public class Tag {
	private static Random rand = new Random();;
	public final long tagID;
	
	/**
	 * Create a brand new tag
	 */
	public Tag() {
		tagID = rand.nextLong();
	}

	/**
	 * Create a tag from existing one.
	 * @param tagID
	 */
	public Tag(long tagID) {
		super();
		this.tagID = tagID;
	}

}
