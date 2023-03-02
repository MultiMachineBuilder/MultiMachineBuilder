/**
 * 
 */
package mmb.engine.mods;

import java.time.Instant;
import java.util.*;

/**
 * Extra information about a mod
 * @author oskar
 */
public class ModMetadata {
	/** Mod author */
	public String author= "";
	/** Mod release date */
	public Date release = Date.from(Instant.now());
	/** Mod description */
	public String description = "";
	/** Mod name */
	public String name = "";
}
