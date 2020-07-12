/**
 * 
 */
package mmb.addon.data;

import java.time.Instant;
import java.util.*;

/**
 * @author oskar
 *
 */
public class ModMetadata {
	public String author= "";
	public Date release = Date.from(Instant.now());
	public String description = "";
	public String name = "";
}
