/**
 * 
 */
package monniasza.collects.selfset;

import java.util.NavigableSet;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 * A self-set that keeps ordering of keys by natural ordering or comparator
 * @param <K> type of keys
 * @param <V> type of values
 */
public interface SortedSelfSet<K, V extends Identifiable<K>> extends SelfSet<K, V>{

	@Override
	public NavigableSet<K> keys();

}
