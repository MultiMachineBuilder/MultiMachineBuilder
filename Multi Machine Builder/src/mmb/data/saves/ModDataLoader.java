/**
 * 
 */
package mmb.data.saves;

import java.util.Hashtable;

/**
 * @author oskar
 *
 */
public interface ModDataLoader {
	public String mainDBMapKey();
	public void apply(Hashtable<String,String> data);
}
