/**
 * 
 */
package monniasza.collects.alloc;

import monniasza.collects.datalayer.IndexedDatalayerMap;

/**
 * @author oskar
 *
 */
public interface Indexable {
	/**
	 * @return the index. Used by the {@link IndexedDatalayerMap}
	 */
	public int ordinal();
	
	/**
	 * @return the indexer. Used by the {@link IndexedDatalayerMap}
	 */
	public Object index();
}
