/**
 * 
 */
package mmb.data.saves.basic;

import java.util.Hashtable;

import mmb.data.saves.ModDataLoader;

/**
 * @author oskar
 *
 */
public class BasicDataLoader implements ModDataLoader {

	/**
	 * 
	 */
	public BasicDataLoader() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see mmb.data.saves.ModDataLoader#mainDBMapKey()
	 */
	@Override
	public String mainDBMapKey() {
		return "game";
	}

	/* (non-Javadoc)
	 * @see mmb.data.saves.ModDataLoader#apply(java.util.Hashtable)
	 */
	@Override
	public void apply(Hashtable<String, String> data) {
		// TODO Auto-generated method stub
		
	}

}
