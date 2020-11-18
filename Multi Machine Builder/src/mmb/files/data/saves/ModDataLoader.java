/**
 * 
 */
package mmb.files.data.saves;

import java.util.Hashtable;

/**
 * @author oskar
 *
 */
public interface ModDataLoader {
	String mainDBMapKey();
	void apply(Hashtable<String, String> data);
}
